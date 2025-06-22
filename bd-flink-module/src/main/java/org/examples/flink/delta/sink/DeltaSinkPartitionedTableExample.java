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

import org.examples.flink.utils.Constants;
import org.examples.flink.utils.DeltaExampleSourceFunction;
import org.examples.flink.utils.Utils;
import org.examples.flink.utils.jobs.FlinkJobRunnerBase;
import io.delta.flink.sink.DeltaSink;
import io.delta.flink.sink.RowDataDeltaSinkBuilder;
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
 * Demonstrates how the Flink Delta Sink can be used to write data to a partitioned Delta table.
 * <p>
 * If you run this example then application will spawn example local Flink job generating data to
 * the underlying Delta table under directory of "src/main/resources/sink_delta_table". The job will be
 * run in a daemon thread while in the main app's thread there will Delta Standalone application
 * reading and printing all the data to the std out.
 */
/*

--engine-type local-cluster --table-path data/sink_delta_partitioned_table
--engine-type remote-cluster --table-path data/sink_delta_partitioned_table

*/
public class DeltaSinkPartitionedTableExample extends FlinkJobRunnerBase {


    public static void main(String[] args) throws Exception {

        //String tablePath = params.get("table-path", "s3a://defaultfs/delta-flink-example/");
        //String TABLE_PATH = tablePath + UUID.randomUUID().toString().replace("-", "");

        ParameterTool arg_params = ParameterTool.fromArgs(args);
        String engine_type  = arg_params.get(ENGINE_TYPE, Constants.LOCAL_CLUSTER);

        Map<String, String> params = new LinkedHashMap<>();
        arg_params.toMap().forEach((key, value) -> params.put(key.toString(), value.toString()));

        if(!params.containsKey(ENGINE_TYPE)){
            params.put(ENGINE_TYPE, Constants.LOCAL_CLUSTER);
        }

        String sink_tbl_path = "data/sink_delta_partitioned_table";
        if(arg_params.toMap().containsKey("sink-table-path")){
            sink_tbl_path = Utils.resolveTableAbsolutePath(params.get("sink-table-path"), Constants.LOCAL_CLUSTER);
        }
        params.put("sink-table-path", sink_tbl_path);

        String uuid = UUID.randomUUID().toString().split("-")[0];
        params.put(WORKFLOW_NAME,"partitioned-delta-table-pipeline-"+uuid);

        params.put("source-parallelism", "2");
        params.put("sink-parallelism", "2");

        new DeltaSinkPartitionedTableExample().run(params);

    }

    /**
     * An example of using Flink Delta Sink in streaming pipeline.
     */
    @Override
    public StreamExecutionEnvironment createPipeline(Map<String, String> params) {

        int sourceParallelism = Integer.parseInt(params.get("source-parallelism"));
        int sinkParallelism = Integer.parseInt(params.get("sink-parallelism"));

        String sink_tbl_path = params.get("sink-table-path");
        DeltaSink<RowData> deltaSink = getDeltaSink(sink_tbl_path);

        StreamExecutionEnvironment env = getStreamExecutionEnvironment(params);

        // Using Flink Delta Sink in processing pipeline
        env.addSource(new DeltaExampleSourceFunction())
                .setParallelism(sourceParallelism)
                .sinkTo(deltaSink)
                .name("sink-delta-table")
                .setParallelism(sinkParallelism);

        return env;

    }

    /**
     * An example of Flink Delta Sink configuration with partition column.
     */
    public DeltaSink<RowData> getDeltaSink(String tablePath) {
        String[] partitionCols = {"f1"};

        Configuration configuration = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());
        RowDataDeltaSinkBuilder deltaSinkBuilder = DeltaSink.forRowData(
                new Path(tablePath),
                configuration,
                Utils.FULL_SCHEMA_ROW_TYPE
        );
        deltaSinkBuilder.withPartitionColumns(partitionCols);
        return deltaSinkBuilder.build();
    }
}
