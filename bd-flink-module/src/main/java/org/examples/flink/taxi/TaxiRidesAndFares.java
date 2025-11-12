package org.examples.flink.taxi;

import org.examples.flink.config.WorkflowConfig;
import org.examples.flink.taxi.models.RideAndFare;
import org.examples.flink.taxi.models.TaxiFare;
import org.examples.flink.taxi.models.TaxiRide;
import org.examples.flink.taxi.source.TaxiFareGenerator;
import org.examples.flink.taxi.source.TaxiRideGenerator;
import org.examples.flink.utils.Constants;
import org.examples.flink.utils.Utils;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import org.apache.flink.util.Collector;
import org.examples.flink.AbstractFlinkStreamWorkflow;


/*
--app-config taxi_workflow.properties --engine-type local-cluster --table-name transactions --config-path ${HOME}/IdeaProjects/docker-hadoop-cluster/bd-flink-module/src/main/resources/local-cluster

*/
public class TaxiRidesAndFares extends AbstractFlinkStreamWorkflow  {


    TaxiRidesAndFares(WorkflowConfig workflowConfig){
        super(workflowConfig);
    }

    @Override
    public StreamExecutionEnvironment createPipeline() throws Exception {
        final SourceFunction<TaxiRide> rideSource = new TaxiRideGenerator();
        final SourceFunction<TaxiFare> fareSource = new TaxiFareGenerator();
        final SinkFunction<RideAndFare> sink = new PrintSinkFunction<>();

        // Setting up checkpointing so that the state can be explored with the State Processor API.
        // Generally it's better to separate configuration settings from the code,
        // but for this example it's convenient to have it here for running in the IDE.
        Configuration conf = new Configuration();
        conf.setString("state.backend", "filesystem");
        String checkpoint_dir = workflowConfig.workflowConf().getOrDefault("workflow.state.checkpoints.dir", "checkpoints");
        conf.setString("state.checkpoints.dir", Utils.resolveTableAbsolutePath(checkpoint_dir, workflowConfig.workflowConf().get(Constants.ENGINE_TYPE)));
        conf.setString("execution.checkpointing.interval", "10s");
        conf.setString("execution.checkpointing.externalized-checkpoint-retention","RETAIN_ON_CANCELLATION");

        StreamExecutionEnvironment env = this.flinkSession();

        // A stream of taxi ride START events, keyed by rideId.
        DataStream<TaxiRide> rides =
                env.addSource(rideSource).filter(ride -> ride.isStart).keyBy(ride -> ride.rideId);

        // A stream of taxi fare events, also keyed by rideId.
        DataStream<TaxiFare> fares = env.addSource(fareSource).keyBy(fare -> fare.rideId);

        // Create the pipeline.
        rides.connect(fares)
                .flatMap(new EnrichmentFunction())
                .uid("enrichment") // uid for this operator's state
                .name("enrichment") // name for this operator in the web UI
                .addSink(sink);

        return env;
    }



    /**
     * Main method.
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        // Join Rides with Fares
        WorkflowConfig workflowConfig = new WorkflowConfig(args);
        System.out.println("Workflow Id :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_ID));
        System.out.println("Workflow Name :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_NAME));

        TaxiRidesAndFares job = new TaxiRidesAndFares(workflowConfig);
        JobExecutionResult result = job.startWorkflow("Rides with Fares");
        System.out.println(result.toString());

    }

    public static class EnrichmentFunction
            extends RichCoFlatMapFunction<TaxiRide, TaxiFare, RideAndFare> {

        private ValueState<TaxiRide> rideState;
        private ValueState<TaxiFare> fareState;

        @Override
        public void open(Configuration config) {

            rideState = getRuntimeContext().getState(new ValueStateDescriptor<>("saved ride", TaxiRide.class));
            fareState = getRuntimeContext().getState(new ValueStateDescriptor<>("saved fare", TaxiFare.class));
        }

        @Override
        public void flatMap1(TaxiRide ride, Collector<RideAndFare> out) throws Exception {

            TaxiFare fare = fareState.value();
            if (fare != null) {
                fareState.clear();
                out.collect(new RideAndFare(ride, fare));
            } else {
                rideState.update(ride);
            }
        }

        @Override
        public void flatMap2(TaxiFare fare, Collector<RideAndFare> out) throws Exception {

            TaxiRide ride = rideState.value();
            if (ride != null) {
                rideState.clear();
                out.collect(new RideAndFare(ride, fare));
            } else {
                fareState.update(fare);
            }
        }
    }

}
