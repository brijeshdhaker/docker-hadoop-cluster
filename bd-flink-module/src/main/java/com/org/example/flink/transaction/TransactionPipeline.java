/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.org.example.flink.transaction;

import com.org.example.flink.example.models.Person;
import com.org.example.flink.transaction.models.raw.RawTransaction;
import com.org.example.flink.transaction.source.TransactionGenerator;
import com.org.example.flink.utils.DeltaExampleSourceFunction;
import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.LocalFlinkJobRunner;
import com.org.example.flink.utils.jobs.LocalFlinkJobRunnerBase;
import io.delta.flink.sink.DeltaSink;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.logical.*;
import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;


/**
 * Example that counts the rides for each driver.
 *
 * <p>Note that this is implicitly keeping state for each driver. This sort of simple, non-windowed
 * aggregation on an unbounded set of keys will use an unbounded amount of state. When this is an
 * issue, look at the SQL/Table API, or ProcessFunction, or state TTL, all of which provide
 * mechanisms for expiring state for stale keys.
 */

/*
--engine-type=local --table-path=pipelines/raw/transactions
 */
public class TransactionPipeline extends LocalFlinkJobRunnerBase {

    /**
     * Main method.
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        ParameterTool params = ParameterTool.fromArgs(args);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        String engine_type  = params.get("engine-type", "local");
        String table_path  = params.get("table-path", "pipelines/raw/transactions");
        String TABLE_PATH = Utils.resolveExampleTableAbsolutePath(table_path, engine_type);
        new TransactionPipeline().run(TABLE_PATH);

        // run the cleansing pipeline
        //env.execute("Transactions Pipeline");
    }

    public void run(String tablePath) throws Exception {

        System.out.println("Pipeline will use table " + tablePath);
        StreamExecutionEnvironment env = createPipeline(tablePath, 2, 3);

        // start the data generator
        DataStream<RawTransaction> raw_txns = env.addSource(new TransactionGenerator());
        //raw_txns.print();

        // filter VISA Transactions
        DataStream<RawTransaction> visa_raw_txns = raw_txns.filter(new FilterFunction<RawTransaction>() {
            @Override
            public boolean filter(RawTransaction raw_txn) throws Exception {
                return raw_txn.getCardType().equalsIgnoreCase("VISA");
            }
        });
        visa_raw_txns.print();

        /*
        // map each ride to a tuple of (driverId, 1)
        DataStream<Tuple2<Long, Long>> tuples =
                rides.map(
                        new MapFunction<TaxiRide, Tuple2<Long, Long>>() {
                            @Override
                            public Tuple2<Long, Long> map(TaxiRide ride) {
                                return Tuple2.of(ride.driverId, 1L);
                            }
                        });

        // partition the stream by the driverId
        KeyedStream<Tuple2<Long, Long>, Long> keyedByDriverId = tuples.keyBy(t -> t.f0);

        // count the rides for each driver
        DataStream<Tuple2<Long, Long>> rideCounts = keyedByDriverId.sum(1);

        // we could, in fact, print out any or all of these streams
        rideCounts.print();
        */

        runFlinkJobInBackground(env);

    }

    @Override
    public StreamExecutionEnvironment createPipeline(String tablePath, int sourceParallelism, int sinkParallelism) {

        DeltaSink<RowData> deltaSink = getDeltaSink(tablePath);

        StreamExecutionEnvironment env = getStreamExecutionEnvironment();

        // Using Flink Delta Sink in processing pipeline
        env
                .addSource(new DeltaExampleSourceFunction())
                .setParallelism(sourceParallelism)
                .sinkTo(deltaSink)
                .name("Sink Transactions : Delta Table ")
                .setParallelism(sinkParallelism);

        return env;

    }

    @Override
    public DeltaSink<RowData> getDeltaSink(String tablePath) {

        Configuration configuration = Utils.getHadoopFsConfiguration(getRunnerType());

        RowType FULL_SCHEMA_ROW_TYPE = new RowType(Arrays.asList(
                new RowType.RowField("id", new BigIntType()),
                new RowType.RowField("uuid", new VarCharType(VarCharType.MAX_LENGTH)),
                new RowType.RowField("cardType", new VarCharType(VarCharType.MAX_LENGTH)),
                new RowType.RowField("website", new VarCharType(VarCharType.MAX_LENGTH)),
                new RowType.RowField("product", new VarCharType(VarCharType.MAX_LENGTH)),
                new RowType.RowField("amount", new FloatType()),
                new RowType.RowField("city", new VarCharType(VarCharType.MAX_LENGTH)),
                new RowType.RowField("country", new VarCharType(VarCharType.MAX_LENGTH)),
                new RowType.RowField("eventTime", new BigIntType())
        ));

        return DeltaSink.forRowData(
                new Path(tablePath),
                configuration,
                FULL_SCHEMA_ROW_TYPE
        ).build();
    }
}
