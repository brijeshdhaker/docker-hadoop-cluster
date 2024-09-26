package org.examples.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.examples.context.RawContext;
import org.examples.enums.MessageSource;
import org.examples.enums.MessageType;
import org.examples.service.ServiceProvider;
import org.examples.service.TopicService;
import org.examples.utils.ListUtil;
import org.examples.writers.DataWriter;
import org.examples.writers.ParquetWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

public abstract class KafkaJobProcessor<Key, Value> implements StreamJobProcessor <Key, Value, Row> {


    private static final Logger logger = LoggerFactory.getLogger(KafkaJobProcessor.class);

    protected SparkConf sparkConf;
    protected StructType schema;

    private DataWriter dataWriter;
    protected TopicService topicService;

    public KafkaJobProcessor(SparkConf sparkConf, StructType schema){

        this.sparkConf = sparkConf;
        this.schema = schema;
        //
        List<String> topics = ListUtil.listFromStrings(sparkConf.get("spark.confluent.kafka.topics"));
        ServiceProvider serviceProvider = ServiceProvider.getInstance(sparkConf);
        topicService = serviceProvider.topicService(topics);

        //
        this.dataWriter = new ParquetWriter();

        //
    }


    @Override
    public String save(JavaRDD<Row> rdd) {

        SparkSession spark = SparkSession
                .builder()
                .config(this.sparkConf)
                .getOrCreate();

        String path = path(this.sparkConf);
        rdd.foreach(row -> {
            String key   = (String)row.get(0);
            byte[] value = (byte[])row.get(1);
            System.out.println("Key : " + key + " *******  Value : []" + value.length);
        });
        //this.dataWriter.write(spark.createDataFrame(rdd, schema),path);
        return path;

    }

    @Override
    public JavaRDD<Row> process(JavaRDD<ConsumerRecord<Key, Value>> rdd, long jobId, long jobStepID) {
        try {

            RawContext rawContext = RawContext.as()
                    .jobId(jobId)
                    .jobStepId(jobStepID)
                    .transactionTime(LocalDateTime.now())
                    .messageSource(MessageSource.KAFKA_AVRO)
                    .messageType(MessageType.TRANSACTIONS)
                    .schema(schema);

            return rdd.mapPartitions(partitionProcessor(rawContext));

        } catch (Exception e) {
            logger.error("Unable to map partition");
            throw new RuntimeException("Unable to map partitions", e);

        }

    }


    protected abstract FlatMapFunction<Iterator<ConsumerRecord<Key,Value>>, Row> partitionProcessor(RawContext rawContext);
}
