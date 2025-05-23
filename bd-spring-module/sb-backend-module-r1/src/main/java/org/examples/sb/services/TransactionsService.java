package org.examples.sb.services;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.converters.RandomItem;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.exceptions.ExceptionType;

import org.examples.sb.kafka.KafkaProducer;
import org.examples.sb.avro.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Profile("k8s")
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
    public void generateAndSendTransaction(Integer count) throws AppException {
        try{

            Faker faker = new Faker(Locale.US);
            for (int i = 1; i <= count; i++) {

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
