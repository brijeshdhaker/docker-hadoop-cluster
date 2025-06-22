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
 * This application is supposed to be run on a Flink cluster. It will try to read Delta table from
 * "/tmp/delta-flink-example/source_table" folder in a streaming job.
 * The Delta table data has to be copied there manually from
 * "src/main/resources/data/source_delta_table" folder.
 * Read records will be printed to log using custom Sink Function.
 * <p>
 * This configuration will read all columns from underlying Delta table from the latest Snapshot.
 * If any of the columns was a partition column, connector will automatically detect it.
 */
public class DeltaContinuousSourceClusterExample extends FlinkJobRunnerBase {

    private static final String TABLE_PATH = Utils.resolveTableAbsolutePath("data/source_delta_table", "Local");

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

        params.put("source-parallelism", "1");
        params.put("sink-parallelism", "1");
        new DeltaContinuousSourceClusterExample().run(params);
    }

    public void run(Map<String, String> params) throws Exception {

        String src_tbl_path = params.get("source-table-path");
        if(params.containsKey("source-table-path")) {
            System.out.println("sink table path : " + src_tbl_path);
            Utils.prepareDirs(src_tbl_path);
        }

        String sink_tab_path = params.get("sink-table-path");
        if(params.containsKey("sink-table-path")) {
            System.out.println("sink table path : " + sink_tab_path);
            //Utils.prepareDirs(sink_tab_path);
        }

        System.out.println("Will use table path: " + sink_tab_path);
        Utils.prepareDirs(src_tbl_path, sink_tab_path);
        StreamExecutionEnvironment env = createPipeline(params);

        // Just to have better visual representation of Job on FLink's UI
        env.disableOperatorChaining();

        String engine_type = params.get("engine_type");
        if(engine_type.equalsIgnoreCase("mini-cluster")) {
            runJobInBackground(env, params);
        }else{
            env.executeAsync("Continuous Example Job");
        }
        Utils.runSourceTableUpdater(sink_tab_path).get();
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
                .name("Console Sink")
                .setParallelism(1);

        return env;
    }

    /**
     * An example of Flink Delta Source configuration that will read all columns from Delta table
     * using the latest snapshot. The {@code .forContinuousRowData(...) } creates Delta Flink
     * source that will monitor delta table for any new changes.
     */
    public DeltaSource<RowData> getDeltaSource(String tablePath) {

        Configuration configuration = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());

        return DeltaSource.forContinuousRowData(
                new Path(tablePath),
                configuration
        ).build();
    }
}
