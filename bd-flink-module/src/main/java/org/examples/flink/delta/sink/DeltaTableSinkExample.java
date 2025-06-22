/*
 * Copyright (2021) The Delta Lake Project Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.examples.flink.delta.sink;

import org.examples.flink.utils.DeltaExampleSourceFunction;
import org.examples.flink.utils.Utils;
import org.examples.flink.utils.jobs.FlinkJobRunnerBase;
import io.delta.flink.sink.DeltaSink;
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
 * Demonstrates how the Flink Delta Sink can be used to write data to Delta table.
 * <p>
 * This application is supposed to be run on a Flink cluster. When run it will start to generate
 * data to the underlying Delta table under directory of `/tmp/delta-flink-example/<UUID>`.
 */
/*

--engine-type=local --sink-table-path=pipelines/raw/transactions
--engine-type=cluster --sink-table-path=pipelines/raw/transactions

*/
public class DeltaTableSinkExample extends FlinkJobRunnerBase {

    static String TABLE_PATH = Utils.resolveTableAbsolutePath("data/sink_delta_table", "cluster");

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

        String sink_tbl_path = "data/sink_delta_table";
        if(arg_params.toMap().containsKey("sink-table-path")){
            sink_tbl_path = Utils.resolveTableAbsolutePath(params.get("sink-table-path"), engine_type);
        }

        params.put("sink-table-path", sink_tbl_path);
        String uuid = UUID.randomUUID().toString().split("-")[0];
        params.put(WORKFLOW_NAME,"delta-table-pipeline-"+uuid);

        params.put("source-parallelism", "2");
        params.put("sink-parallelism", "2");

        new DeltaTableSinkExample().run(params);
    }

    /**
     * An example of using Flink Delta Sink in streaming pipeline.
     */
    @Override
    public StreamExecutionEnvironment createPipeline(Map<String, String> params) {
        int sourceParallelism = 2;
        int sinkParallelism = 2;

        String sink_tbl_path = params.get("sink-table-path");
        DeltaSink<RowData> deltaSink = getDeltaSink(sink_tbl_path);

        StreamExecutionEnvironment env = getStreamExecutionEnvironment(params);

        // Using Flink Delta Sink in processing pipeline
        env.addSource(new DeltaExampleSourceFunction())
                .setParallelism(Integer.parseInt(params.get("source-parallelism")))
                .sinkTo(deltaSink)
                .name("sink-delta-table")
                .setParallelism(sinkParallelism);

        return env;
    }

    /**
     * An example of Flink Delta Sink configuration.
     */
    public DeltaSink<RowData> getDeltaSink(String tablePath) {

        Configuration configuration = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());

        return DeltaSink.forRowData(
                        new Path(tablePath),
                        configuration,
                        Utils.FULL_SCHEMA_ROW_TYPE
                ).build();
    }
}
