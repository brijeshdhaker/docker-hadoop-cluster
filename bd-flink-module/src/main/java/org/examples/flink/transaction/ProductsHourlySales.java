package org.examples.flink.transaction;

import org.examples.flink.config.WorkflowConfig;
import org.examples.flink.transaction.models.raw.RawTransaction;
import org.examples.flink.transaction.source.TransactionGenerator;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.examples.flink.AbstractFlinkStreamWorkflow;

/*

--app-config transactions_workflow.properties --engine-type local-cluster --table-name transactions --config-path ${HOME}/IdeaProjects/docker-hadoop-cluster/bd-flink-module/src/main/resources/local-cluster

*/
public class ProductsHourlySales extends AbstractFlinkStreamWorkflow {


    ProductsHourlySales(WorkflowConfig workflowConfig){
        super(workflowConfig);
    }

    public static void main(String[] args) throws Exception {

        //
        WorkflowConfig workflowConfig = new WorkflowConfig(args);
        System.out.println("Workflow Type :: Flink Stream ");

        ProductsHourlySales streamWorkflow = new ProductsHourlySales(workflowConfig);
        JobExecutionResult result = streamWorkflow.startWorkflow();
        //new ProductsHourlySales().run(params);

    }



    @Override
    public StreamExecutionEnvironment createPipeline() {

        // set up streaming execution environment
        StreamExecutionEnvironment env = flinkSession();

        final SourceFunction<RawTransaction> source = new TransactionGenerator();
        final SinkFunction<Tuple3<Long, Long, Float>> sink = null;

        // start the data generator and arrange for watermarking
        DataStream<RawTransaction> enriched_txns = env.addSource(source)
                .assignTimestampsAndWatermarks(
                                // transactions are in order
                                WatermarkStrategy.<RawTransaction>forMonotonousTimestamps()
                                        .withTimestampAssigner((enrich_txn, t) -> enrich_txn.getEventTime()));


        DataStream<RawTransaction> laptops = enriched_txns.filter(new FilterFunction<RawTransaction>() {
            @Override
            public boolean filter(RawTransaction txn) throws Exception {
                return txn.getProduct().equalsIgnoreCase("Laptop");
            }
        });
        laptops.print();

        return env;
    }
}
