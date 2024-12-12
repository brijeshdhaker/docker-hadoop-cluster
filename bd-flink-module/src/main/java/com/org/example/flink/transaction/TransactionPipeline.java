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

import com.org.example.flink.transaction.functions.DeltaTransactionMapFunction;
import com.org.example.flink.transaction.functions.DeltaTransactionSourceFunction;
import com.org.example.flink.transaction.models.raw.RawTransaction;
import com.org.example.flink.transaction.source.TransactionGenerator;
import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.LocalFlinkJobRunnerBase;
import io.delta.flink.sink.DeltaSink;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.util.HadoopUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.logical.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.flink.api.java.tuple.Tuple2;

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
AWS_ACCESS_KEY_ID=pgm2H2bR7a5kMc5XCYdO;AWS_SECRET_ACCESS_KEY=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG;AWS_REGION=us-east-1;AWS_S3_ENDPOINT=http://minio.sandbox.net:9010
AWS_ACCESS_KEY_ID=admin;AWS_SECRET_ACCESS_KEY=passwprd;AWS_REGION=us-east-1;AWS_S3_ENDPOINT=http://minio.sandbox.net:9010
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
        String TABLE_PATH = Utils.resolveTableAbsolutePath(table_path, engine_type);
        new TransactionPipeline().run(TABLE_PATH);

        // run the cleansing pipeline
        //env.execute("Transactions Pipeline");
    }

    public void run(String tablePath) throws Exception {

        System.out.println("Transaction Pipeline will use delta table " + tablePath);
        StreamExecutionEnvironment env = createPipeline(tablePath, 2, 3);




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

        //runJobInBackground(env);
        env.execute();

    }

    @Override
    public StreamExecutionEnvironment createPipeline(String tablePath, int sourceParallelism, int sinkParallelism) {

        StreamExecutionEnvironment env = getStreamExecutionEnvironment();

        // start the data generator
        DataStream<RawTransaction> raw_txns = env.addSource(new TransactionGenerator()).name("Source Raw Transactions");
        //raw_txns.print();


        // filter VISA Transactions
        DataStream<RawTransaction> raw_visa_txns = raw_txns.filter(new FilterFunction<RawTransaction>() {
            @Override
            public boolean filter(RawTransaction raw_txn) throws Exception {
                return raw_txn.getCardType().equalsIgnoreCase("VISA");
            }
        }).name("Filter VISA Transactions");
        //raw_visa_txns.print();

        // map each txn to a tuple of (card_type, 1)
        DataStream<Tuple2<String, Long>> card_type_touple = raw_txns.map(
                        new MapFunction<RawTransaction, Tuple2<String, Long>>() {
                            @Override
                            public Tuple2<String, Long> map(RawTransaction rt) {
                                return Tuple2.of(rt.getCardType(), 1L);
                            }
                        }).name("Map txn to (card_type, 1)");

        // partition the stream by the CardType
        KeyedStream<Tuple2<String, Long>, String> keyedByCardType = card_type_touple.keyBy(t -> t.f0);
        //keyedByCardType.print();

        // count the Transactions for each card type
        DataStream<Tuple2<String, Long>> cardTypeTxnCounts = keyedByCardType.sum(1).name("Transactions count for each card type");
        cardTypeTxnCounts.print();

        // we could, in fact, print out any or all of these streams
        //cardTypeTxnCounts.print();

        //
        /*
        DataStream<RowData> raw_delta_txns = raw_txns.map(new DeltaTransactionMapFunction());
        raw_delta_txns.print();
        */

        // Using Flink Delta Sink in processing pipeline
        /*

        DeltaSink<RowData> deltaSink = getDeltaSink(tablePath);

        env.addSource(new DeltaTransactionSourceFunction())
                .setParallelism(sourceParallelism)
                .sinkTo(deltaSink)
                .name("Sink Transactions : Delta Table ")
                .setParallelism(sinkParallelism);

        */
        return env;

    }

    @Override
    public DeltaSink<RowData> getDeltaSink(String tablePath) {

        Configuration configuration = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());

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
