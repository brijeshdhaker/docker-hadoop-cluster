package org.examples.flink.taxi;
import org.examples.flink.config.WorkflowConfig;
import org.examples.flink.taxi.models.TaxiRide;
import org.examples.flink.taxi.source.TaxiRideGenerator;
import org.examples.flink.utils.Constants;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import org.apache.flink.util.Collector;
import org.examples.flink.AbstractFlinkStreamWorkflow;

import java.time.Duration;

public class TaxiLongRidesAlerts extends AbstractFlinkStreamWorkflow {


    /** Creates a job using the source and sink provided. */
    public TaxiLongRidesAlerts(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }

    @Override
    public StreamExecutionEnvironment createPipeline() throws Exception {
        final SourceFunction<TaxiRide> source = new TaxiRideGenerator();
        final SinkFunction<Long> sink = new PrintSinkFunction<>();

        StreamExecutionEnvironment env = flinkSession();

        // start the data generator
        DataStream<TaxiRide> rides = env.addSource(source);

        // the WatermarkStrategy specifies how to extract timestamps and generate watermarks
        WatermarkStrategy<TaxiRide> watermarkStrategy =
                WatermarkStrategy.<TaxiRide>forBoundedOutOfOrderness(Duration.ofSeconds(60))
                        .withTimestampAssigner(
                                (ride, streamRecordTimestamp) -> ride.getEventTimeMillis());

        // create the pipeline
        rides.assignTimestampsAndWatermarks(watermarkStrategy)
                .keyBy(ride -> ride.rideId)
                .process(new AlertFunction())
                .addSink(sink);

        return env;
    }

    /**
     * Main method.
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        // Long Taxi Rides
        WorkflowConfig workflowConfig = new WorkflowConfig(args);
        System.out.println("Workflow Id :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_ID));
        System.out.println("Workflow Name :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_NAME));

        TaxiLongRidesAlerts job = new TaxiLongRidesAlerts(workflowConfig);
        JobExecutionResult result = job.startWorkflow("Long Taxi Rides");
        System.out.println(result.toString());

    }

    @VisibleForTesting
    public static class AlertFunction extends KeyedProcessFunction<Long, TaxiRide, Long> {

        private ValueState<TaxiRide> rideState;

        @Override
        public void open(Configuration config) {
            ValueStateDescriptor<TaxiRide> rideStateDescriptor =
                    new ValueStateDescriptor<>("ride event", TaxiRide.class);
            rideState = getRuntimeContext().getState(rideStateDescriptor);
        }

        @Override
        public void processElement(TaxiRide ride, Context context, Collector<Long> out)
                throws Exception {

            TaxiRide firstRideEvent = rideState.value();

            if (firstRideEvent == null) {
                // whatever event comes first, remember it
                rideState.update(ride);

                if (ride.isStart) {
                    // we will use this timer to check for rides that have gone on too long and may
                    // not yet have an END event (or the END event could be missing)
                    context.timerService().registerEventTimeTimer(getTimerTime(ride));
                }
            } else {
                if (ride.isStart) {
                    if (rideTooLong(ride, firstRideEvent)) {
                        out.collect(ride.rideId);
                    }
                } else {
                    // the first ride was a START event, so there is a timer unless it has fired
                    context.timerService().deleteEventTimeTimer(getTimerTime(firstRideEvent));

                    // perhaps the ride has gone on too long, but the timer didn't fire yet
                    if (rideTooLong(firstRideEvent, ride)) {
                        out.collect(ride.rideId);
                    }
                }

                // both events have now been seen, we can clear the state
                // this solution can leak state if an event is missing
                // see DISCUSSION.md for more information
                rideState.clear();
            }
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext context, Collector<Long> out)
                throws Exception {

            // the timer only fires if the ride was too long
            out.collect(rideState.value().rideId);

            // clearing now prevents duplicate alerts, but will leak state if the END arrives
            rideState.clear();
        }

        private boolean rideTooLong(TaxiRide startEvent, TaxiRide endEvent) {
            return Duration.between(startEvent.eventTime, endEvent.eventTime)
                    .compareTo(Duration.ofHours(2))
                    > 0;
        }

        private long getTimerTime(TaxiRide ride) throws RuntimeException {
            if (ride.isStart) {
                return ride.eventTime.plusSeconds(120 * 60).toEpochMilli();
            } else {
                throw new RuntimeException("Can not get start time from END event.");
            }
        }
    }
}
