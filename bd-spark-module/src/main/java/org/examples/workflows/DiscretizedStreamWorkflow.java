package org.examples.workflows;

import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;
import org.apache.spark.util.LongAccumulator;
import org.codejargon.fluentjdbc.api.query.Transaction;
import org.examples.config.KafkaConfig;
import org.examples.config.WorkflowConfig;
import org.examples.models.KafkaOffset;
import org.examples.processor.AvroJobProcessor;
import org.examples.processor.StreamJobProcessor;
import org.examples.service.OffsetService;
import org.examples.service.ServiceProvider;
import org.examples.service.TopicService;
import org.examples.utils.HadoopFileSystemUtil;
import org.examples.utils.KafkaUtil;
import org.examples.utils.ListUtil;
import org.examples.utils.TimeUtil;
import org.examples.writers.ConsoleWriter;
import org.examples.writers.DataWriter;
import org.examples.writers.HiveWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DiscretizedStreamWorkflow extends AbstractStreamWorkflow<String, GenericRecord, Row> {


    private static final Logger logger = LoggerFactory.getLogger(DiscretizedStreamWorkflow.class);

    public DiscretizedStreamWorkflow(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }

    @Override
    protected StreamJobProcessor<String, GenericRecord, Row> streamProcessor() {
        DataWriter writer = new HiveWriter();
        return new AvroJobProcessor(sparkSession(), writer);
    }

    @Override
    public void startWorkflow() throws Exception {

        LocalDateTime startTime = LocalDateTime.now();

        //validateParams();

        if(this.workflowConfig != null){

            SparkConf sparkConf = workflowConfig.sparkConf();
            String workflowAppId = sparkConf.get("workflow.app.id");

            List<String> topics = ListUtil.listFromStrings(sparkConf.get("spark.confluent.kafka.topics"));
            String group = sparkConf.get("spark.confluent.kafka.group");

            try {

                ServiceProvider serviceProvider = ServiceProvider.getInstance(sparkConf);
                OffsetService offsetService = serviceProvider.offsetService();
                TopicService topicService = serviceProvider.topicService(topics);

                //
                SparkSession spark = sparkSession();

                //spark.sparkContext().setLogLevel("ERROR");
                JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
                JavaStreamingContext streamingContext = new JavaStreamingContext(jsc, Durations.seconds(30));

                //
                Map<TopicPartition, Long> initialOffsets = KafkaUtil.partitions(topicService.partitions())
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), t -> offsetService.offset(group,t)));
                logger.info("Initial offsets : {}", initialOffsets);

                JavaInputDStream<ConsumerRecord<String, GenericRecord>> stream = KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, GenericRecord>Subscribe(topics, kafkaConfig(), initialOffsets) //
                );

                String streamAppId = streamingContext.sparkContext().sc().applicationId();
                LongAccumulator batchAccumulator = streamingContext.sparkContext().sc().longAccumulator("count-of-batches");
                LongAccumulator recordAccumulator = streamingContext.sparkContext().sc().longAccumulator("count-of-records");

                //
                stream.foreachRDD(inputRDD -> {

                    long startBatchTime = System.currentTimeMillis();
                    OffsetRange[] offsetRanges = ((HasOffsetRanges) inputRDD.rdd()).offsetRanges();
                    logger.info("Offset Range Details : {} ", Arrays.toString(offsetRanges));

                    boolean isAny = !inputRDD.isEmpty();
                    logger.info("Checking {} topic(s) with {} partition(s) in total whether there any data to process --> {}", topics.size(), offsetRanges.length, isAny);


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
                        logger.info("jobId: {} | Starting jobStepId: {} for {} records", 2001,1, count);

                        if(inputRDD.getNumPartitions() > 3){
                            inputRDD = inputRDD.coalesce(3);
                        }

                        List<KafkaOffset> offsets = KafkaUtil.wrap(group, offsetRanges);

                        JavaRDD<Row> outputRDD = streamProcessor().process(inputRDD, 2001L, 1L);

                        String path = null;
                        if(count > 0) {
                            path = streamProcessor().save(outputRDD);
                            logger.info("jobid: {} | Processed {} records and saved to {}", 2001L, count, path);
                        }

                        long startOracleTime = System.currentTimeMillis();
                        final String savePath = path;

                        offsetService.query().transaction()
                                .isolation(Transaction.Isolation.READ_COMMITTED)
                                .inNoResult(() ->{
                                    // Update Job Status

                                    //
                                    offsetService.update(offsets);

                                    // commit async offsets to kafka otherwise we can't use kafka monitoring tool
                                    ((CanCommitOffsets) stream.inputDStream()).commitAsync(offsetRanges);
                                });


                        batchAccumulator.add(1L);
                        recordAccumulator.add(count);

                        logger.info("jobId {} | Finished jobStepId {}, outputCount {}, oracleTime: {}, batchTime {}",
                                2001L, 1L, count,
                                TimeUtil.diffInMinAndSec(startOracleTime, System.currentTimeMillis()),
                                TimeUtil.diffInMinAndSec(startBatchTime, System.currentTimeMillis())
                        );

                    } catch( Exception e){

                        logger.error("DStream batch failed for jobStepId {}", workflowAppId, e);
                        streamingContext.stop();

                    } finally {

                        logger.info("Metrics >> Job started at {} and is running for {}", TimeUtil.toString(startTime), TimeUtil.diffWithNow(startTime));
                        logger.info("Metrics >> Processed batches: {}, total records: {}", batchAccumulator.value(), recordAccumulator.value());

                    }

                });

                streamingContext.start();

                checkToStop(streamingContext);

            } catch (Exception e){

            }
        }
    }


    @Override
    protected Map<String, Object> kafkaConfig() {
        Map<String, Object> kafkaConfig = KafkaConfig.dstreamConfig(
                workflowConfig.sparkConf(),
                io.confluent.kafka.serializers.KafkaAvroDeserializer.class,
                io.confluent.kafka.serializers.KafkaAvroDeserializer.class
        );
        return kafkaConfig;
    }

    @Override
    protected void checkToStop(JavaStreamingContext ssc) {

        long checkIntervalMillis = workflowConfig.sparkConf().getLong("workflow.marker.interval.ms", 10000);
        String shutdownMarker = workflowConfig.sparkConf().get("workflow.marker.file");
        logger.info("Marker file for shout down is [{}], checking with interval {} ms",shutdownMarker, checkIntervalMillis );


        boolean markerExists = false;
        boolean isStopped = false;

        while(!isStopped){

            logger.debug("awaitTerminationOrTimeout {} ms", checkIntervalMillis );

            try{
                isStopped = ssc.awaitTerminationOrTimeout(checkIntervalMillis);
            } catch(InterruptedException e){
                logger.error("JavaStreamContext has been interrupted, stopping gracefully", e);
                ssc.stop(true, true);
                logger.info("JavaStreamContext is stopped.");
                Thread.currentThread().interrupt();
            }

            if(isStopped){
                logger.info("Confirmed! the streaming context is stopped. Exiting application ..");
            }

            try{
                markerExists = HadoopFileSystemUtil.isExists(shutdownMarker, ssc.sparkContext().hadoopConfiguration());
                logger.debug("marker file [{}] exists {}", shutdownMarker, markerExists );
            } catch(Exception e){
                logger.error("Unable to check file existence ", e);
            }

            if(!isStopped && !markerExists){
                logger.info("stopping gracefully as marker file doesn't exist");
                ssc.stop(true, true);
                logger.info("JavaStreamContext is stopped.");
            }
        }

    }
}
