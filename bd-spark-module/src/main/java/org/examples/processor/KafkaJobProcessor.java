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
import org.examples.writers.FileSystemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

public abstract class KafkaJobProcessor<Key, Value> implements StreamJobProcessor <Key, Value, Row> {


    private static final Logger logger = LoggerFactory.getLogger(KafkaJobProcessor.class);

    protected SparkSession spark;
    protected StructType schema;

    protected DataWriter dataWriter;
    protected TopicService topicService;

    public KafkaJobProcessor(SparkSession spark, DataWriter dataWriter){

        this.spark = spark;
        SparkConf sparkConf = spark.sparkContext().conf();
        //
        ServiceProvider serviceProvider = ServiceProvider.getInstance(sparkConf);
        List<String> topics = ListUtil.listFromStrings(sparkConf.get("spark.confluent.kafka.topics"));
        topicService = serviceProvider.topicService(topics);

        //
        this.dataWriter = dataWriter;

        //
    }

    protected abstract FlatMapFunction<Iterator<ConsumerRecord<Key,Value>>, Row> partitionProcessor(RawContext rawContext);
}
