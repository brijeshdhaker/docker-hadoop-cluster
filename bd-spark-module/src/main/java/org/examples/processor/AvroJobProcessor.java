package org.examples.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.examples.context.RawContext;

import java.util.Iterator;
import java.util.Map;

public class AvroJobProcessor extends KafkaJobProcessor <String, byte[]>{

    public AvroJobProcessor(SparkConf sparkConf){
        super(sparkConf);
    }

    @Override
    protected FlatMapFunction<Iterator<ConsumerRecord<String, byte[]>>, Row> partitionProcessor(RawContext rawContext) {
        Map<String, String> errorTopics = topicService.error_topics();
        return AvroPartitionProcessor.as(rawContext, errorTopics);
    }

}
