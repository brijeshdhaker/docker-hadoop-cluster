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
package com.org.example.flink.delta.sink;

import io.delta.flink.sink.DeltaSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;
import com.org.example.flink.utils.DeltaExampleSourceFunction;
import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.DeltaSinkClusterJobExampleBase;
import org.apache.flink.api.java.utils.ParameterTool;
/**
 * Demonstrates how the Flink Delta Sink can be used to write data to Delta table.
 * <p>
 * This application is supposed to be run on a Flink cluster. When run it will start to generate
 * data to the underlying Delta table under directory of `/tmp/delta-flink-example/<UUID>`.
 */
public class DeltaSinkExampleCluster extends DeltaSinkClusterJobExampleBase {

    static String TABLE_PATH = Utils.resolveTableAbsolutePath("data/sink_delta_table", "Cluster");

    public static void main(String[] args) throws Exception {
        ParameterTool params = ParameterTool.fromArgs(args);
        //String tablePath = params.get("table-path", "s3a://warehouse-flink/delta-flink-example/");
        //String TABLE_PATH = tablePath + UUID.randomUUID().toString().replace("-", "");
        new DeltaSinkExampleCluster().run(TABLE_PATH);
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
        env.addSource(new DeltaExampleSourceFunction())
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
                        new Path(tablePath),
                        configuration,
                        Utils.FULL_SCHEMA_ROW_TYPE
                ).build();
    }
}
