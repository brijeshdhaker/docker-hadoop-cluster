package org.examples.flink.delta.source.continuous;

import org.examples.flink.utils.ConsoleSink;
import org.examples.flink.utils.Utils;
import org.examples.flink.utils.jobs.FlinkJobRunnerBase;
import io.delta.flink.source.DeltaSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.util.HadoopUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Demonstrates how the Flink Delta source can be used to read data from Delta table.
 * <p>
 * If you run this example then application will spawn example local Flink streaming job that will
 * read changes from Delta table placed under "src/main/resources/data/source_delta_table"
 * starting from version specified by user via {@code .startingVersion(long)} method.
 * After that, source connector will start to actively monitor this table for any new changes.
 * Read records will be printed to log using custom Sink Function.
 */
public class DeltaContinuousSourceStartingVersionExample extends FlinkJobRunnerBase {

    private static final String TABLE_PATH =
            Utils.resolveTableAbsolutePath("data/source_delta_table", "Local");

    public static void main(String[] args) throws Exception {
        //String tablePath = params.get("table-path", "s3a://defaultfs/delta-flink-example/");
        //String TABLE_PATH = tablePath + UUID.randomUUID().toString().replace("-", "");

        ParameterTool arg_params = ParameterTool.fromArgs(args);
        String engine_type  = arg_params.get("engine-type", "local-cluster");

        Map<String, String> params = new LinkedHashMap<>();
        arg_params.toMap().forEach((key, value) -> params.put(key.toString(), value.toString()));

        if(!params.containsKey("engine-type")){
            params.put("engine-type", engine_type);
        }

        String source_tbl_path = "data/source_delta_table";
        if(arg_params.toMap().containsKey("source-table-path")){
            source_tbl_path = Utils.resolveTableAbsolutePath(params.get("source-table-path"), engine_type);
        }
        params.put("source-table-path", source_tbl_path);

        String uuid = UUID.randomUUID().toString().split("-")[0];
        params.put(WORKFLOW_NAME,"bounded-delta-table-source-"+uuid);

        params.put("source-parallelism", "2");
        params.put("sink-parallelism", "2");
        new DeltaContinuousSourceStartingVersionExample().run(params);
    }

    /**
     * An example of using Flink Delta Source in streaming pipeline.
     */
    @Override
    public StreamExecutionEnvironment createPipeline(Map<String, String> params) {

        int sourceParallelism = Integer.parseInt(params.get("source-parallelism"));
        int sinkParallelism = Integer.parseInt(params.get("sink-parallelism"));

        String source_tbl_path = params.get("source-table-path");

        DeltaSource<RowData> deltaSink = getDeltaSource(source_tbl_path);
        StreamExecutionEnvironment env = getStreamExecutionEnvironment(params);

        env
                .fromSource(deltaSink, WatermarkStrategy.noWatermarks(), "source-delta-continuous")
                .setParallelism(sourceParallelism)
                .addSink(new ConsoleSink(Utils.FULL_SCHEMA_ROW_TYPE))
                .setParallelism(1);

        return env;
    }

    /**
     * An example of Flink Delta Source configuration that will read all columns from Delta table.
     * This source will read only changes added to the table starting from version specified via
     * {@code .startingVersion(long)} method.
     * Alternatively the {@code .option("startingVersion", Long)} or {@code .option
     * ("startingVersion", String)} options can be used.
     * <p>
     * The {@code .forContinuousRowData(...)} creates Delta Flink source that will monitor
     * delta table for any new changes.
     */
    public DeltaSource<RowData> getDeltaSource(String tablePath) {

        Configuration configuration = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());

        return DeltaSource.forContinuousRowData(
                        new Path(tablePath),
                        configuration
                )
                .startingVersion(10)
                .build();
    }
}