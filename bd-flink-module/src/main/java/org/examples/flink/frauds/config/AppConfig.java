package org.examples.flink.frauds.config;

import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class AppConfig {

    public static final String BOOTSTRAP_URL = "kafka-broker.sandbox.net:9092";

    public static final String TRANSACTIONS_TOPIC = "transaction-json-topic";
    public static final String ALERTS_TOPIC = "fraud-alerts-topic";

    public static final String CONSUMER_ID = "fn.consumer";
    public static final int TOTAL_CUSTOMERS = 1_000_000;

    public static Properties buildProducerProps() {
        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_URL);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getCanonicalName());
        properties.put(ProducerConfig.ACKS_CONFIG, "1");

        return buildSecurityProps(properties);
    }

    public static Properties buildSecurityProps(Properties properties) {
        /*
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("sasl.jaas.config", "");
        */
        return properties;
    }
}