package org.examples.config;

import org.apache.spark.SparkConf;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {


    public static Map<String, Object>  config(SparkConf sparkConf){
        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaConfig.put("bootstrap.server", sparkConf.get("spark.confluent.kafka.brokers"));
        return  kafkaConfig;
    }

    public static Map<String, Object>  dstreamConfig(SparkConf sparkConf, Class keyDeserialzer, Class valueDeserialzer) {

        Map<String, Object> kafkaConfig = config(sparkConf);
        kafkaConfig.put("group.id", sparkConf.get("spark.confluent.kafka.group"));
        kafkaConfig.put("enable.auto.commit", Boolean.FALSE);
        kafkaConfig.put("key.deserializer", keyDeserialzer);
        kafkaConfig.put("value.deserializer", valueDeserialzer);

        //'schema.registry.url': 'http://schemaregistry:8081'

        if(sparkConf.contains("spark.confluent.kafka.offset.reset")){
            kafkaConfig.put("auto.offset.reset", sparkConf.get("spark.confluent.kafka.offset.reset"));
        }

        return  kafkaConfig;
    }

    public static Map<String, Object>  sstreamConfig(SparkConf sparkConf){
        Map<String, Object> kafkaConfig = config(sparkConf);

        return  kafkaConfig;
    }
}
