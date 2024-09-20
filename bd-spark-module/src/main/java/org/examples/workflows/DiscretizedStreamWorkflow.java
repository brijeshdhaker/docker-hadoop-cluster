package org.examples.workflows;

import org.apache.hadoop.shaded.org.apache.commons.collections.ListUtils;
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
import org.examples.processor.AvroJobProcessor;
import org.examples.processor.StreamJobProcessor;
import org.examples.utils.ListUtil;

import java.util.List;

public class DiscretizedStreamWorkflow extends AbstractStreamWorkflow<String, byte[], Row> {

    public DiscretizedStreamWorkflow(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }

    @Override
    protected StreamJobProcessor<String, byte[], Row> streamProcessor() {
        AvroJobProcessor processor = new AvroJobProcessor(workflowConfig.sparkConf());
        return processor;
    }

    @Override
    public void startWorkflow() throws Exception {
        if(this.workflowConfig != null){
            try {

                SparkConf sparkConf = workflowConfig.sparkConf();

                SparkSession spark = SparkSession
                        .builder()
                        .master("local[*]")  // // spark://spark-iceberg.sandbox.net:7077")
                        .appName("Java Spark SQL basic example")
                        .config(sparkConf)
                        .getOrCreate();

                spark.sparkContext().setLogLevel("ERROR");

                JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
                JavaStreamingContext ssc = new JavaStreamingContext(jsc, Durations.seconds(30));

                List<String> topics = ListUtil.listFromStrings("");

                JavaInputDStream<ConsumerRecord<String, byte[]>> stream = KafkaUtils.createDirectStream(
                        ssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaConfig(), initialOffset)
                );

            }catch (Exception e){

            }
        }
    }
}
