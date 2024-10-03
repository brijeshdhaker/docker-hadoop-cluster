package org.examples.sb.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.examples.sb.config.KafkaCustomProperties;
import org.examples.sb.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Transaction> kafkaTemplate;
    //private final KafkaCustomProperties kafkaCustomProperties;

    @Value("${spring.kafka.transaction-topic}")
    private String kafkaTopic;

    public void sendMessage(Transaction transactionEvent) {
        ProducerRecord<String, Transaction> producerRecord = new ProducerRecord<>(kafkaTopic, transactionEvent);
        CompletableFuture<SendResult<String, Transaction>> completableFuture = kafkaTemplate.send(producerRecord);
        log.info("Sending kafka message on topic {}", kafkaTopic);
        completableFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Kafka message successfully sent on topic {} and value {}", kafkaTopic, result.getProducerRecord().value().toString());
            } else {
                log.error("An error occurred while sending kafka message for event with value {}", producerRecord);
            }
        });
    }
}
