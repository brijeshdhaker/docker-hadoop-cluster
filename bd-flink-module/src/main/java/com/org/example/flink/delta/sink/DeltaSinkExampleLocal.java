package com.org.example.flink.delta.sink;

import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.DeltaSinkLocalJobExampleBase;
import io.delta.flink.sink.DeltaSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;
import com.org.example.flink.utils.DeltaExampleSourceFunction;


/**
 * Demonstrates how the Flink Delta Sink can be used to write data to Delta table.
 * <p>
 * If you run this example then application will spawn example local Flink job generating data to
 * the underlying Delta table under directory of "src/main/resources/sink_delta_table". The job will be
 * run in a daemon thread while in the main app's thread there will Delta Standalone application
 * reading and printing all the data to the std out.
 */
public class DeltaSinkExampleLocal extends DeltaSinkLocalJobExampleBase {

    static String TABLE_PATH = Utils.resolveExampleTableAbsolutePath("data/sink_delta_table", "Local");

    public static void main(String[] args) throws Exception {
        new DeltaSinkExampleLocal().run(TABLE_PATH);
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
                .name("sink-delta-table")
                .setParallelism(sinkParallelism);

        return env;
    }

    /**
     * An example of Flink Delta Sink configuration.
     */
    @Override
    public DeltaSink<RowData> getDeltaSink(String tablePath) {

        Configuration configuration = Utils.getHadoopFsConfiguration(getRunnerType());

        return DeltaSink.forRowData(
                    new Path(TABLE_PATH),
                    configuration,
                    Utils.FULL_SCHEMA_ROW_TYPE
                ).build();
    }
}
