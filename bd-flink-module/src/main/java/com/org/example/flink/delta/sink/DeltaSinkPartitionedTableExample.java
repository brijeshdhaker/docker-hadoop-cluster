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

import com.org.example.flink.utils.DeltaExampleSourceFunction;
import io.delta.flink.sink.DeltaSink;
import io.delta.flink.sink.RowDataDeltaSinkBuilder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;
import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.DeltaSinkClusterJobExampleBase;
import org.apache.flink.api.java.utils.ParameterTool;

/**
 * Demonstrates how the Flink Delta Sink can be used to write data to a partitioned Delta table.
 * <p>
 * If you run this example then application will spawn example local Flink job generating data to
 * the underlying Delta table under directory of "src/main/resources/sink_delta_table". The job will be
 * run in a daemon thread while in the main app's thread there will Delta Standalone application
 * reading and printing all the data to the std out.
 */
public class DeltaSinkPartitionedTableExample extends DeltaSinkClusterJobExampleBase {

    static String TABLE_PATH = Utils.resolveTableAbsolutePath("data/sink_delta_partitioned_table", "Cluster");

    public static void main(String[] args) throws Exception {

        ParameterTool params = ParameterTool.fromArgs(args);
        //String TABLE_PATH = params.get("table-path", "data/sink_delta_partitioned_table");
        new DeltaSinkPartitionedTableExample().run(TABLE_PATH);

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
     * An example of Flink Delta Sink configuration with partition column.
     */
    @Override
    public DeltaSink<RowData> getDeltaSink(String tablePath) {
        String[] partitionCols = {"f1"};

        Configuration configuration = Utils.getHadoopFsConfiguration(getRunnerType());
        RowDataDeltaSinkBuilder deltaSinkBuilder = DeltaSink.forRowData(
                new Path(tablePath),
                configuration,
                Utils.FULL_SCHEMA_ROW_TYPE
        );
        deltaSinkBuilder.withPartitionColumns(partitionCols);
        return deltaSinkBuilder.build();
    }
}
