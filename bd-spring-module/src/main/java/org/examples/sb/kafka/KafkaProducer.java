package org.examples.sb.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.examples.sb.models.avro.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Profile("k8s")
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Transaction> kafkaTemplate;
    //private final KafkaCustomProperties kafkaCustomProperties;

    @Value("${spring.kafka.transaction-topic}")
    private String kafkaTopic;

    public void sendMessage(Transaction transaction) {

        ProducerRecord<String, Transaction> producerRecord = new ProducerRecord<>(kafkaTopic, transaction.getUuid(), transaction);
        CompletableFuture<SendResult<String, Transaction>> completableFuture = kafkaTemplate.send(producerRecord);
        log.info("Sending kafka message on topic {}", kafkaTopic);
        completableFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Kafka message successfully sent on topic {}, partition {}, with key {}, value {}", kafkaTopic,
                        result.getProducerRecord().partition(),result.getProducerRecord().key(),
                        result.getProducerRecord().value().toString()
                );
            } else {
                log.error("An error occurred while sending kafka message for event with value {}", producerRecord);
            }
        });
    }
}
