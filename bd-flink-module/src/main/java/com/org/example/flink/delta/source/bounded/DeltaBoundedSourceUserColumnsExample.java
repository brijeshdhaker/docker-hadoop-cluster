package com.org.example.flink.delta.source.bounded;


import com.org.example.flink.utils.ConsoleSink;
import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.FlinkJobRunnerBase;
import io.delta.flink.source.DeltaSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.util.HadoopUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.logical.IntType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.types.logical.VarCharType;
import org.apache.hadoop.conf.Configuration;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Demonstrates how the Flink Delta source can be used to read data from Delta table.
 * <p>
 * If you run this example then application will spawn example local Flink batch job that will read
 * data from Delta table placed under "src/main/resources/data/source_delta_table".
 * Read records will be printed to log using custom Sink Function.
 * <p>
 * This configuration will read only columns specified by user.
 * If any of the columns was a partition column, connector will automatically detect it.
 * Source will read data from the latest snapshot.
 */
public class DeltaBoundedSourceUserColumnsExample extends FlinkJobRunnerBase {

    private static final String TABLE_PATH = Utils.resolveTableAbsolutePath("data/source_delta_table", "Local");

    private static final RowType ROW_TYPE = new RowType(Arrays.asList(
            new RowType.RowField("f1", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("f3", new IntType())
    ));

    public static void main(String[] args) throws Exception {

        //String tablePath = params.get("table-path", "s3a://warehouse-flink/delta-flink-example/");
        //String TABLE_PATH = tablePath + UUID.randomUUID().toString().replace("-", "");

        ParameterTool arg_params = ParameterTool.fromArgs(args);
        String engine_type  = arg_params.get("engine-type", "local");

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
        params.put("flink-workflow-name","bounded-delta-table-source-"+uuid);

        params.put("source-parallelism", "2");
        params.put("sink-parallelism", "2");

        new DeltaBoundedSourceUserColumnsExample().run(params);
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
                .fromSource(deltaSink, WatermarkStrategy.noWatermarks(), "source-delta-bounded")
                .setParallelism(2)
                .addSink(new ConsoleSink(ROW_TYPE))
                .setParallelism(1);

        return env;
    }

    // TODO PR 18 implement .option("columnNames", ...) was missed.
    /**
     * An example of Flink Delta Source configuration that will read only columns specified by user.
     * via {@code .columnNames(...)} method. Alternatively, the {@code .option("columnNames",
     * List<String> names} method can be used.
     */
    public DeltaSource<RowData> getDeltaSource(String tablePath) {

        Configuration configuration = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());

        return DeltaSource.forBoundedRowData(
                        new Path(tablePath),
                        configuration
                )
                .columnNames("f1", "f3")
                .build();
    }
}
