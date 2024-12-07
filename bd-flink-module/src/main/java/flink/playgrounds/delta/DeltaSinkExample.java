package flink.playgrounds.delta;

import flink.playgrounds.utils.Utils;
import flink.playgrounds.utils.jobs.DeltaSinkLocalJobExampleBase;
import io.delta.flink.sink.DeltaSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;
import flink.playgrounds.utils.DeltaExampleSourceFunction;


/**
 * Demonstrates how the Flink Delta Sink can be used to write data to Delta table.
 * <p>
 * If you run this example then application will spawn example local Flink job generating data to
 * the underlying Delta table under directory of "src/main/resources/example_table". The job will be
 * run in a daemon thread while in the main app's thread there will Delta Standalone application
 * reading and printing all the data to the std out.
 */
public class DeltaSinkExample extends DeltaSinkLocalJobExampleBase {

    static String TABLE_PATH = Utils.resolveExampleTableAbsolutePath("example_table");

    public static void main(String[] args) throws Exception {
        new DeltaSinkExample().run(TABLE_PATH);
    }

    /**
     * An example of using Flink Delta Sink in streaming pipeline.
     */
    @Override
    public StreamExecutionEnvironment createPipeline(
            String tablePath,
            int sourceParallelism,
            int sinkParallelism) {

        DeltaSink<RowData> deltaSink = getDeltaSink(tablePath);
        StreamExecutionEnvironment env = getStreamExecutionEnvironment();

        // Using Flink Delta Sink in processing pipeline
        env
                .addSource(new DeltaExampleSourceFunction())
                .setParallelism(sourceParallelism)
                .sinkTo(deltaSink)
                .name("MyDeltaSink")
                .setParallelism(sinkParallelism);

        return env;
    }

    /**
     * An example of Flink Delta Sink configuration.
     */
    @Override
    public DeltaSink<RowData> getDeltaSink(String tablePath) {
        return DeltaSink
                .forRowData(
                        new Path(TABLE_PATH),
                        new Configuration(),
                        Utils.FULL_SCHEMA_ROW_TYPE)
                .build();
    }
}
