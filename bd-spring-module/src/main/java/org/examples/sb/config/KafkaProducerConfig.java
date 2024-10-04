package org.examples.sb.config;

import org.examples.sb.models.avro.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Autowired
    private ProducerFactory<Integer, String> producerFactory;


    private Map<String, Object> producerConfig(){
        Map<String, Object> prop = new HashMap<>(producerFactory.getConfigurationProperties());
        return prop;
    }


    @Bean
    public KafkaTemplate<String, Transaction> kafkaTemplate() {
        //Map<String, Object> kafkaPropertiesMap = kafkaProperties.buildProducerProperties(null);
        return new KafkaTemplate<String, Transaction>(new DefaultKafkaProducerFactory<>(producerConfig()));
    }

}
