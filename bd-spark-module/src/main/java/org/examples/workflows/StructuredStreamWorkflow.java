package org.examples.workflows;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.examples.config.WorkflowConfig;
import org.examples.processor.StreamJobProcessor;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;

import java.util.Arrays;
import java.util.Iterator;

public class StructuredStreamWorkflow extends AbstractStreamWorkflow<String, byte[], Row> {

    public StructuredStreamWorkflow(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }

    @Override
    public void startWorkflow() throws Exception {

        if (this.workflowConfig != null) {
            try {

                SparkConf sparkConf = workflowConfig.sparkConf();

                SparkSession spark = SparkSession
                        .builder()
                        .master("local[*]")  // // spark://spark-iceberg.sandbox.net:7077")
                        .appName("Java Spark SQL basic example")
                        .config(sparkConf)
                        .getOrCreate();

                //spark.sparkContext().setLogLevel("ERROR");

                // Subscribe to 1 topic
                Dataset<Row> df = spark
                        .readStream()
                        .format("kafka")
                        .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
                        .option("subscribe", "topic1")
                        .option("includeHeaders", "true")
                        .load();

                df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");



            } catch (Exception e) {

            }
        }

    }

    @Override
    protected StreamJobProcessor<String, byte[], Row> streamProcessor() {
        return null;
    }
}
