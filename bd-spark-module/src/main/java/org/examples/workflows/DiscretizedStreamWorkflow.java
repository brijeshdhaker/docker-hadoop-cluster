package org.examples.workflows;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.shaded.org.apache.commons.collections.ListUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;
import org.apache.spark.util.LongAccumulator;
import org.examples.config.KafkaConfig;
import org.examples.config.WorkflowConfig;
import org.examples.models.KafkaOffset;
import org.examples.processor.AvroJobProcessor;
import org.examples.processor.StreamJobProcessor;
import org.examples.service.OffsetService;
import org.examples.service.ServiceProvider;
import org.examples.service.TopicService;
import org.examples.utils.KafkaUtil;
import org.examples.utils.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
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
        return new AvroJobProcessor(workflowConfig.sparkConf());
    }

    @Override
    public void startWorkflow() throws Exception {
        if(this.workflowConfig != null){
            try {

                SparkConf sparkConf = workflowConfig.sparkConf();

                List<String> topics = ListUtil.listFromStrings("spark.confluent.kafka.topics");
                String group = sparkConf.get("spark.confluent.kafka.group");

                ServiceProvider serviceProvider = ServiceProvider.getInstance(sparkConf);
                OffsetService offsetService = serviceProvider.offsetService();
                TopicService topicService = serviceProvider.topicService(topics);

                //
                SparkSession spark = SparkSession
                        .builder()
                        .master("local[*]")  // // spark://spark-iceberg.sandbox.net:7077")
                        .appName("Java Spark SQL basic example")
                        .config(sparkConf)
                        .getOrCreate();

                spark.sparkContext().setLogLevel("ERROR");

                JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
                JavaStreamingContext ssc = new JavaStreamingContext(jsc, Durations.seconds(30));

                //
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

                //
                stream.foreachRDD(inputRDD -> {

                    long startBatchTime = System.currentTimeMillis();
                    OffsetRange[] offsetRanges = ((HasOffsetRanges) inputRDD.rdd()).offsetRanges();
                    logger.info("Offset Range Details : {} ", Arrays.toString(offsetRanges));

                    boolean isAny = !inputRDD.isEmpty();
                    if(isAny){
                        Pair<Long, Long> ids = new Pair<Long, Long>() {
                            @Override
                            public Long getLeft() {
                                return 100L;
                            }

                            @Override
                            public Long getRight() {
                                return 100L;
                            }

                            @Override
                            public Long setValue(Long aLong) {
                                return 100L;
                            }
                        };

                    }

                    try{

                        long count = KafkaUtil.count(offsetRanges);
                        if(inputRDD.getNumPartitions() > 3){
                            inputRDD = inputRDD.coalesce(3);
                        }

                        List<KafkaOffset> offsets = KafkaUtil.wrap(group, offsetRanges);

                        JavaRDD<Row> outputRDD = streamProcessor().process(inputRDD, 1L, 1L);

                        String path = null;
                        if(count > 0) {

                            path = streamProcessor().save(outputRDD);

                        }

                        batchAccumulator.add(1L);
                        recordAccumulator.add(count);




                    } catch( Exception e){

                        ssc.stop();

                    } finally {

                    }


                });

                ssc.start();

                checkToStop(ssc);

            } catch (Exception e){

            }
        }
    }


    @Override
    protected Map<String, Object> kafkaConfig() {
        Map<String, Object> kafkaConfig = KafkaConfig.dstreamConfig(workflowConfig.sparkConf(), StringDeserializer.class, ByteArrayDeserializer.class );
        return kafkaConfig;
    }

    @Override
    protected void checkToStop(JavaStreamingContext ssc) {
        long checkIntervalMillis = 0;

        boolean markerExists;
        boolean isStopped = false;

        while(!isStopped){
            try{
                ssc.awaitTerminationOrTimeout(10000);
            } catch(InterruptedException e){

            }

            if(isStopped){

            }

            markerExists = Boolean.TRUE;
        }

    }
}
