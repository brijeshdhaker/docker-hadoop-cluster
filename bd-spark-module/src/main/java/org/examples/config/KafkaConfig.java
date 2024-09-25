package org.examples.config;

import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KafkaConfig {


    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    public static Map<String, Object>  config(SparkConf sparkConf){
        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaConfig.put("bootstrap.servers", sparkConf.get("spark.confluent.kafka.brokers"));
        return  kafkaConfig;
    }

    public static Map<String, Object>  dstreamConfig(SparkConf sparkConf, Class keyDeserialzer, Class valueDeserialzer) {

        Map<String, Object> kafkaConfig = config(sparkConf);
        kafkaConfig.put("client.id",sparkConf.get("spark.confluent.kafka.client"));
        kafkaConfig.put("group.id", sparkConf.get("spark.confluent.kafka.group"));
        kafkaConfig.put("key.deserializer", keyDeserialzer);
        kafkaConfig.put("value.deserializer", valueDeserialzer);
        kafkaConfig.put("schema.registry.url", sparkConf.get("spark.confluent.kafka.schema.registry.url"));
        kafkaConfig.put("enable.auto.commit", false);

        if(sparkConf.contains("spark.confluent.kafka.offset.reset")){
            kafkaConfig.put("auto.offset.reset", sparkConf.get("spark.confluent.kafka.offset.reset"));
        }

        kafkaConfig.forEach((k, v) -> logger.info("***** Property {} set to ****** {}", k,  v));

        return  kafkaConfig;
    }

    public static Map<String, Object>  sstreamConfig(SparkConf sparkConf){
        Map<String, Object> kafkaConfig = config(sparkConf);

        return  kafkaConfig;
    }
}
