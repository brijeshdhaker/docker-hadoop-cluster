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

import com.org.example.flink.AbstractFlinkStreamWorkflow;
import com.org.example.flink.config.WorkflowConfig;
import com.org.example.flink.enums.Zone;
import com.org.example.flink.transaction.functions.EnrichDeltaTransactionMapFunction;
import com.org.example.flink.transaction.models.enriched.EnrichedTransaction;
import com.org.example.flink.transaction.models.raw.RawTransaction;
import com.org.example.flink.transaction.models.refined.RefineTransaction;
import com.org.example.flink.transaction.source.TransactionGenerator;
import com.org.example.flink.utils.Constants;
import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.FlinkJobRunnerBase;
import io.delta.flink.sink.DeltaSink;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.hadoop.HadoopUserUtils;
import org.apache.flink.runtime.util.HadoopUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
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

--app-config transactions_workflow.properties --engine-type local-cluster --table-name transactions --config-path /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-flink-module/src/main/resources/local-cluster

AWS_ACCESS_KEY_ID=pgm2H2bR7a5kMc5XCYdO;AWS_SECRET_ACCESS_KEY=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG;AWS_REGION=us-east-1;AWS_S3_ENDPOINT=http://minio.sandbox.net:9010

AWS_ACCESS_KEY_ID=admin;AWS_SECRET_ACCESS_KEY=password;AWS_REGION=us-east-1;AWS_S3_ENDPOINT=http://minio.sandbox.net:9010

*/
public class TransactionPipeline extends AbstractFlinkStreamWorkflow {


    TransactionPipeline(WorkflowConfig workflowConfig){
        super(workflowConfig);
    }

    /**
     * Main method.
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        //
        WorkflowConfig workflowConfig = new WorkflowConfig(args);
        System.out.println("Workflow Id :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_ID));
        System.out.println("Workflow Name :: " + workflowConfig.workflowConf().get(Constants.WORKFLOW_NAME));

        TransactionPipeline streamWorkflow = new TransactionPipeline(workflowConfig);
        JobExecutionResult result = streamWorkflow.startWorkflow();
        System.out.println(result.toString());

        // run the cleansing pipeline

    }

    @Override
    public StreamExecutionEnvironment createPipeline() {


        StreamExecutionEnvironment env = flinkSession();

        // Generate RAW Transactions
        DataStream<RawTransaction> raw_txns = env.addSource(new TransactionGenerator()).name("Raw Transactions");
        //raw_txns.print();

        // filter VISA Transactions
        /*
        DataStream<RawTransaction> raw_visa_txns = raw_txns.filter(new FilterFunction<RawTransaction>() {
            @Override
            public boolean filter(RawTransaction raw_txn) throws Exception {
                return raw_txn.getCardType().equalsIgnoreCase("VISA");
            }
        }).name("Filter VISA Transactions");
        raw_visa_txns.print();
        */

        //DeltaSink<RowData> rawTxnSink = getDeltaSink(Zone.RAW.name().toLowerCase(), params);


        /**
         *
         */
        // Transaction Refinement
        DataStream<RefineTransaction> refine_txns = raw_txns.map(
                new MapFunction<RawTransaction, RefineTransaction>() {
                    @Override
                    public RefineTransaction map(RawTransaction raw_txn) {
                        RefineTransaction refine_txn = RefineTransaction.builder();
                        try {
                            BeanUtils.copyProperties(refine_txn,raw_txn);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        return refine_txn;
                    }
                }).name("Transaction Refinement");
        //DeltaSink<RowData> refineTxnSink = getDeltaSink(Zone.REFINE.name().toLowerCase(), params);

        /**
         *
         */
        // Transaction Enrichment
        DataStream<EnrichedTransaction> enriched_txns = refine_txns.map(
                new MapFunction<RefineTransaction, EnrichedTransaction>() {
                    @Override
                    public EnrichedTransaction map(RefineTransaction refine_txn) {
                        EnrichedTransaction enriched_txn = EnrichedTransaction.builder();
                        try {
                            BeanUtils.copyProperties(enriched_txn,refine_txn);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        return enriched_txn;
                    }
                }).name("Transaction Enrichment");
        //enriched_txns.print();
        DataStream<RowData> enriched_delta_txns = enriched_txns.map(new EnrichDeltaTransactionMapFunction());
        DeltaSink<RowData> enrichTxnDeltaSink = getDeltaSink(Zone.ENRICH.name().toLowerCase(), workflowConfig.workflowConf());
        enriched_delta_txns.sinkTo(enrichTxnDeltaSink).name("Enrich Delta Write");

        /*
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
        */

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

    private DeltaSink<RowData> getDeltaSink(String zone, Map<String, String> params) {

        Configuration configuration = Utils.getHadoopFsConfiguration(workflowConfig.workflowConf().get(Constants.ENGINE_TYPE));
        String table_name = String.format("pipelines/%s/%s", zone, workflowConfig.workflowConf().get(Constants.PARMA_TABLE_NAME));
        String table_path = Utils.resolveTableAbsolutePath(table_name, workflowConfig.workflowConf().get(Constants.ENGINE_TYPE));

        DeltaSink<RowData> deltaSink = null;
        switch (Zone.valueOf(zone.toUpperCase())){
            case RAW:
                params.put("raw-txn-delta-table", table_path);
                System.out.println("raw-txn will save in delta table path : " + table_path);
                deltaSink = DeltaSink.forRowData(
                        new Path(table_path),
                        configuration,
                        Constants.RAW_TXN_SCHEMA_ROW_TYPE
                ).build();
                break;
            case REFINE:
                params.put("refine-txn-delta-table", table_path);
                System.out.println("refine-txn will save in delta table path : " + table_path);
                deltaSink = DeltaSink.forRowData(
                        new Path(table_path),
                        configuration,
                        Constants.REFINED_TXN_SCHEMA_ROW_TYPE
                ).build();
                break;
            case ENRICH:
                params.put("enrich-txn-delta-table", table_path);
                System.out.println("enrich-txn will save in delta table path : " + table_path);
                deltaSink = DeltaSink.forRowData(
                        new Path(table_path),
                        configuration,
                        Constants.ENRICH_TXN_SCHEMA_ROW_TYPE
                ).build();
                break;
        }
        return deltaSink;
    }
}
