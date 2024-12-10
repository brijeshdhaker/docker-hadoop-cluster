package org.examples.sb.services;

import com.github.javafaker.Address;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.examples.sb.converters.RandomItem;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.exceptions.ExceptionType;
import org.examples.sb.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TransactionsService {

    @Autowired
    KafkaTemplate<String, Transaction> kafkaTemplate;

    @Value("${spring.kafka.transaction-topic}")
    private String kafkaTopic;

    @Transactional(transactionManager = "transactionManager")
    public void generateAndSendTransaction() throws AppException {
        try{

            Faker faker = new Faker(Locale.US);
            for (int i = 0; i < 10; i++) {
                //Order t = new Order(id++, i+1, i+2, 1000, "NEW");
                Address address = faker.address();
                List<String> CCTYPES = Arrays.asList("VISA", "Master", "Amex", "RuPay", "Discover");
                String ccType = RandomItem.getRandomItem(CCTYPES);
                Transaction transaction = Transaction.builder().id(i++)
                        .amount(faker.random().nextDouble())
                        .cardtype(ccType)
                        .website(faker.internet().url())
                        .product(faker.commerce().productName())
                        .city(address.cityName())
                        .country(address.country());

                ProducerRecord<String, Transaction> producerRecord = new ProducerRecord<>(kafkaTopic, transaction);
                CompletableFuture<SendResult<String, Transaction>> completableFuture = kafkaTemplate.send(kafkaTopic, transaction.getUuid(), transaction);
                log.info("Sending kafka message on topic {}", kafkaTopic);
                completableFuture.whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Kafka message successfully sent on topic {}, partition {}, with key {}, value {}",
                                kafkaTopic,
                                result.getProducerRecord().partition(),
                                result.getProducerRecord().key(),
                                result.getProducerRecord().value().toString()
                        );
                    } else {
                        log.error("An error occurred while sending kafka message for event with value {}", transaction.toString());
                    }
                });

                Thread.sleep(1000);
            }

        }catch (Exception e){
            throw  new AppException(ExceptionType.SERVICE_EXCEPTION, e.getMessage());
        }

    }
}
