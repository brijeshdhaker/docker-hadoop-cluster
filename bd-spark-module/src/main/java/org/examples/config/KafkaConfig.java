package org.examples.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {


    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    public static Map<String, Object>  config(SparkConf sparkConf){
        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, sparkConf.get("spark.confluent.kafka.brokers"));
        return  kafkaConfig;
    }

    public static Map<String, Object>  dstreamConfig(SparkConf sparkConf, Class keyDeserialzer, Class valueDeserialzer) {
        /**

         import org.apache.kafka.common.serialization.ByteArrayDeserializer;
         import org.apache.kafka.common.serialization.StringDeserializer;

         **/
        Map<String, Object> kafkaConfig = config(sparkConf);
        kafkaConfig.put(ConsumerConfig.CLIENT_ID_CONFIG,sparkConf.get("spark.confluent.kafka.client"));
        kafkaConfig.put(ConsumerConfig.GROUP_ID_CONFIG, sparkConf.get("spark.confluent.kafka.group"));
        kafkaConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserialzer);
        kafkaConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserialzer);
        kafkaConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        kafkaConfig.put("schema.registry.url", sparkConf.get("spark.confluent.kafka.schema.registry.url"));

        if(sparkConf.contains("spark.confluent.kafka.offset.reset")){
            kafkaConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, sparkConf.get("spark.confluent.kafka.offset.reset"));
        }

        if(logger.isDebugEnabled()) {
            kafkaConfig.forEach((k, v) -> logger.info("***** Property {} set to ****** {}", k, v));
        }
        return  kafkaConfig;
    }

    public static Map<String, Object>  sstreamConfig(SparkConf sparkConf){
        Map<String, Object> kafkaConfig = config(sparkConf);

        return  kafkaConfig;
    }
}
