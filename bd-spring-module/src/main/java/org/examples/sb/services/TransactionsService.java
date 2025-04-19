package org.examples.sb.services;

import com.github.javafaker.Address;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.examples.sb.converters.RandomItem;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.exceptions.ExceptionType;

import org.examples.sb.kafka.KafkaProducer;
import org.examples.sb.models.avro.Transaction;
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
    /*
    @Autowired
    KafkaTemplate<String, Transaction> kafkaTemplate;

    @Value("${spring.kafka.transaction-topic}")
    private String kafkaTopic;
    */
    @Autowired
    KafkaProducer kafkaProducer;

    @Transactional(transactionManager = "transactionManager")
    public void generateAndSendTransaction() throws AppException {
        try{

            Faker faker = new Faker(Locale.US);
            for (int i = 0; i < 10; i++) {
                //Order t = new Order(id++, i+1, i+2, 1000, "NEW");
                Address address = faker.address();
                List<String> CCTYPES = Arrays.asList("VISA", "Master", "Amex", "RuPay", "Discover");
                String ccType = RandomItem.getRandomItem(CCTYPES);
                String uuid = UUID.randomUUID().toString();
                Transaction transaction = Transaction.newBuilder().setId(faker.random().nextInt(1,1000))
                        .setUuid(uuid)
                        .setAmount(faker.random().nextDouble())
                        .setCardtype(ccType)
                        .setWebsite(faker.internet().url())
                        .setProduct(faker.commerce().productName())
                        .setCity(address.cityName())
                        .setCountry(address.country())
                        .setAddts(System.currentTimeMillis()).build();

                kafkaProducer.sendMessage(transaction);

            }

        }catch (Exception e){
            throw  new AppException(ExceptionType.SERVICE_EXCEPTION, e.getMessage());
        }

    }
}
