package org.examples.flink.taxi;

import org.examples.flink.config.WorkflowConfig;
import org.examples.flink.taxi.models.TaxiFare;
import org.examples.flink.taxi.source.TaxiFareGenerator;
import org.examples.flink.utils.Constants;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.examples.flink.AbstractFlinkStreamWorkflow;

public class DriverHourlyMaxTips extends AbstractFlinkStreamWorkflow {



    /** Creates a job using the source and sink provided. */
    public DriverHourlyMaxTips(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }

    public static void main(String[] args) throws Exception {

        //
        WorkflowConfig workflowConfig = new WorkflowConfig(args);
        System.out.println("Workflow Id :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_ID));
        System.out.println("Workflow Name :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_NAME));

        DriverHourlyMaxTips job = new DriverHourlyMaxTips(workflowConfig);
        JobExecutionResult result = job.startWorkflow("Hourly Tips");
        System.out.println(result.toString());
    }

    /**
     * Create and execute the hourly tips pipeline.
     *
     * @return {JobExecutionResult}
     * @throws Exception which occurs during job execution.
     */
    @Override
    public StreamExecutionEnvironment createPipeline() throws Exception {
        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final SourceFunction<TaxiFare> source = new TaxiFareGenerator();
        final SinkFunction<Tuple3<Long, Long, Float>> sink = new PrintSinkFunction<>();

        // start the data generator and arrange for watermarking
        DataStream<TaxiFare> fares =
                env.addSource(source)
                        .assignTimestampsAndWatermarks(
                                // taxi fares are in order
                                WatermarkStrategy.<TaxiFare>forMonotonousTimestamps()
                                        .withTimestampAssigner(
                                                (fare, t) -> fare.getEventTimeMillis()));

        // compute tips per hour for each driver
        DataStream<Tuple3<Long, Long, Float>> hourlyTips =
                fares.keyBy((TaxiFare fare) -> fare.driverId)
                        .window(TumblingEventTimeWindows.of(Time.hours(1)))
                        .process(new AddTips());

        // find the driver with the highest sum of tips for each hour
        DataStream<Tuple3<Long, Long, Float>> hourlyMax =
                hourlyTips.windowAll(TumblingEventTimeWindows.of(Time.hours(1))).maxBy(2);

        /* You should explore how this alternative (commented out below) behaves.
         * In what ways is the same as, and different from, the solution above (using a windowAll)?
         */

        // DataStream<Tuple3<Long, Long, Float>> hourlyMax = hourlyTips.keyBy(t -> t.f0).maxBy(2);

        hourlyMax.addSink(sink);

        return env;
    }

    /*
     * Wraps the pre-aggregated result into a tuple along with the window's timestamp and key.
     */
    public static class AddTips
            extends ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Float>, Long, TimeWindow> {

        @Override
        public void process(
                Long key,
                Context context,
                Iterable<TaxiFare> fares,
                Collector<Tuple3<Long, Long, Float>> out) {

            float sumOfTips = 0F;
            for (TaxiFare f : fares) {
                sumOfTips += f.tip;
            }
            out.collect(Tuple3.of(context.window().getEnd(), key, sumOfTips));
        }
    }
}
