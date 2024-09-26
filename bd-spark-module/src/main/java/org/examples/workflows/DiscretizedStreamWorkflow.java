package org.examples.workflows;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import org.examples.schema.SchemaProvider;
import org.examples.service.OffsetService;
import org.examples.service.ServiceProvider;
import org.examples.service.TopicService;
import org.examples.utils.HadoopFileSystemUtil;
import org.examples.utils.KafkaUtil;
import org.examples.utils.ListUtil;
import org.examples.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
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
        StructType schemaType = null;
        try{
            schemaType = SchemaProvider.structType("transaction-avro-topic");
        } catch(Exception e){
            logger.error("error occurred while loadin schema details for topic [{}]", "transaction-avro-topic", e);
        }
        return new AvroJobProcessor(workflowConfig.sparkConf(), schemaType);
    }

    @Override
    public void startWorkflow() throws Exception {

        LocalDateTime startTime = LocalDateTime.now();

        //validateParams();

        if(this.workflowConfig != null){

            SparkConf sparkConf = workflowConfig.sparkConf();
            String currentJobName = sparkConf.get("workflow.app.name");
            String currentJobId = sparkConf.get("workflow.app.id");
            Boolean jobRunningCheckEnabled = sparkConf.getBoolean("workflow.app.running.check.enabled", false);
            List<String> topics = ListUtil.listFromStrings(sparkConf.get("spark.confluent.kafka.topics"));
            String group = sparkConf.get("spark.confluent.kafka.group");

            try {

                ServiceProvider serviceProvider = ServiceProvider.getInstance(sparkConf);
                OffsetService offsetService = serviceProvider.offsetService();
                TopicService topicService = serviceProvider.topicService(topics);

                //
                SparkSession spark = SparkSession
                        .builder()
                        .appName("AppLauncher::DiscretizedStreamWorkflow")
                        .config(sparkConf)
                        .getOrCreate();

                //spark.sparkContext().setLogLevel("ERROR");
                JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
                JavaStreamingContext streamingContext = new JavaStreamingContext(jsc, Durations.seconds(30));

                //
                Map<TopicPartition, Long> initialOffsets = KafkaUtil.partitions(topicService.partitions())
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), t -> offsetService.offset(group,t)));
                logger.info("Initial offsets : {}", initialOffsets);

                JavaInputDStream<ConsumerRecord<String, byte[]>> stream = KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaConfig(), initialOffsets) //
                );

                LongAccumulator batchAccumulator = streamingContext.sparkContext().sc().longAccumulator("count-of-batches");
                LongAccumulator recordAccumulator = streamingContext.sparkContext().sc().longAccumulator("count-of-records");

                String streamAppId = streamingContext.sparkContext().sc().applicationId();
                /*
                JavaPairDStream<String, byte[]> s1  = stream.mapToPair(record -> {
                    return new Tuple2<>(record.key(), record.value());
                });
                SchemaRegistryClient sclient = new CachedSchemaRegistryClient("http://schemaregistry:8081", 128);
                AvroDeserializer avroDeserializer = new AvroDeserializer(sclient);
                s1.foreachRDD(r -> {
                    List<Tuple2<String, byte[]>> touples = r.collect();
                    touples.forEach(c -> {
                        String key = avroDeserializer.deserialize(c._1().getBytes());
                        String value = avroDeserializer.deserialize(c._2());
                        System.out.println("Key : " + key + " Value " +  value );

                        //System.out.println(c._1 + " ******* " +  new String(c._2, StandardCharsets.UTF_8));
                    });
                });
                */
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
                        logger.error("DStream batch failed for jobStepId {}", currentJobId, e);
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
        Map<String, Object> kafkaConfig = KafkaConfig.dstreamConfig(workflowConfig.sparkConf(), StringDeserializer.class, ByteArrayDeserializer.class );
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
