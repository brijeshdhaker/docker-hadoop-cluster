package org.examples.sb.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.examples.sb.models.Transaction;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("k8s")
public class KafkaConsumer {

    ExecutorService executorService = Executors.newFixedThreadPool(16);


    //@KafkaListener(topics = "${spring.kafka.transaction-topic}", containerFactory = "kafkaListenerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(Message<Transaction> transactionMessage) {
        log.info("Starting consuming from transaction_events_topic - {}", transactionMessage.toString());
    }

    @KafkaListener(topics = "${spring.kafka.transaction-topic}", containerFactory = "kafkaListenerContainerFactory", groupId = "${spring.kafka.consumer.group-id}", concurrency = "4")
    public void consume(ConsumerRecord<String, String> record, @Headers MessageHeaders headers){

        log.info("### -> Header acquired : {}", headers);
        Acknowledgment ack = headers.get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        // Processing
        executorService.submit(() -> {
            log.info("### -> Key " + String.format("%s",record.key()));
            log.info("### -> Value " + String.format("%s",record.value()));
            System.out.println("Event with key " + record.key() + " successfully processed.");
        });
        // Acknowledgement
        if(Objects.nonNull(ack)){
            ack.acknowledge();
        }
    }

    @DltHandler
    public void dlt(String data, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        log.error("Event from topic {} is dead lettered - event: {}", topic, data);
    }
}
