package com.org.example.flink.delta.source.bounded;

import com.org.example.flink.utils.ConsoleSink;
import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.bounded.DeltaBoundedSourceClusterJobExampleBase;
import io.delta.flink.source.DeltaSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;


/**
 * Demonstrates how the Flink Delta source can be used to read data from Delta table.
 * <p>
 * This application is supposed to be run on a Flink cluster. It will try to read Delta table from
 * "/tmp/delta-flink-example/source_table" folder in a batch job.
 * The Delta table data has to be copied there manually from
 * "src/main/resources/data/source_delta_table" folder.
 * Read records will be printed to log using custom Sink Function.
 * <p>
 * This configuration will read all columns from underlying Delta table from the latest Snapshot.
 * If any of the columns was a partition column, connector will automatically detect it.
 */
public class DeltaBoundedSourceClusterExample extends DeltaBoundedSourceClusterJobExampleBase {

    private static final String TABLE_PATH = Utils.resolveTableAbsolutePath("data/source_delta_table", "Local");

    public static void main(String[] args) throws Exception {
        new DeltaBoundedSourceClusterExample().run(TABLE_PATH);
    }

    /**
     * An example of using Flink Delta Source in streaming pipeline.
     */
    @Override
    public StreamExecutionEnvironment createPipeline(
            String tablePath,
            int sourceParallelism,
            int sinkParallelism) {

        DeltaSource<RowData> deltaSink = getDeltaSource(tablePath);
        StreamExecutionEnvironment env = getStreamExecutionEnvironment();

        env
                .fromSource(deltaSink, WatermarkStrategy.noWatermarks(), "source-delta-bounded")
                .setParallelism(sourceParallelism)
                .addSink(new ConsoleSink(Utils.FULL_SCHEMA_ROW_TYPE))
                .setParallelism(1);

        return env;
    }

    /**
     * An example of Flink Delta Source configuration that will read all columns from Delta table
     * using the latest snapshot.
     */
    @Override
    public DeltaSource<RowData> getDeltaSource(String tablePath) {

        Configuration configuration = Utils.getHadoopFsConfiguration(getRunnerType());
        return DeltaSource.forBoundedRowData(
                new Path(tablePath),
                configuration
        ).build();
    }
}
