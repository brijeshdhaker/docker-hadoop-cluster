package org.examples.workflows;

import org.apache.hadoop.shaded.org.apache.commons.collections.ListUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
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
import org.apache.spark.util.LongAccumulator;
import org.examples.config.WorkflowConfig;
import org.examples.processor.AvroJobProcessor;
import org.examples.processor.StreamJobProcessor;
import org.examples.service.OffsetService;
import org.examples.service.ServiceProvider;
import org.examples.service.TopicService;
import org.examples.utils.KafkaUtil;
import org.examples.utils.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DiscretizedStreamWorkflow extends AbstractStreamWorkflow<String, byte[], Row> {


    private static final Logger logger = LoggerFactory.getLogger(DiscretizedStreamWorkflow.class);

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

                List<String> topics = ListUtil.listFromStrings("spark.kafka.topics");
                String group = sparkConf.get("spark.kafka.group");

                ServiceProvider serviceProvider = ServiceProvider.getInstance(sparkConf);
                OffsetService offsetService = serviceProvider.offsetService();
                TopicService topicService = serviceProvider.topicService(topics);


                SparkSession spark = SparkSession
                        .builder()
                        .master("local[*]")  // // spark://spark-iceberg.sandbox.net:7077")
                        .appName("Java Spark SQL basic example")
                        .config(sparkConf)
                        .getOrCreate();

                spark.sparkContext().setLogLevel("ERROR");

                JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
                JavaStreamingContext ssc = new JavaStreamingContext(jsc, Durations.seconds(30));



                Map<TopicPartition, Long> initialOffsets = KafkaUtil.partitions(topicService.partitions())
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), t -> offsetService.offset(group,t)));
                logger.info("Initial offsets : {}", initialOffsets);



                JavaInputDStream<ConsumerRecord<String, byte[]>> stream = KafkaUtils.createDirectStream(
                        ssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaConfig(), initialOffsets)
                );

                LongAccumulator batchAccumulator = ssc.sparkContext().sc().longAccumulator("count-of-batches");
                LongAccumulator recordAccumulator = ssc.sparkContext().sc().longAccumulator("count-of-records");

                String appId = ssc.sparkContext().sc().applicationId();

                stream.foreachRDD(inputRDD -> {

                });

                ssc.start();

                checkToStop(ssc);

            } catch (Exception e){

            }
        }
    }


    @Override
    protected Map<String, Object> kafkaConfig() {


        return Map.of();
    }
}
