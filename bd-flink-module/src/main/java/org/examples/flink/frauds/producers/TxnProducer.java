package org.examples.flink.frauds.producers;

import org.examples.flink.frauds.config.AppConfig;
import org.examples.flink.frauds.datagen.DataGenerator;
import org.examples.flink.frauds.models.Transaction;
import org.examples.flink.frauds.utils.StreamingUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.examples.flink.frauds.utils.StreamingUtils.closeProducer;

public class TxnProducer {

    private static final Logger logger = LoggerFactory.getLogger(TxnProducer.class);

    public static void main(String[] args) {

        var properties = AppConfig.buildProducerProps();
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "64000");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        logger.info("Starting Kafka Producers  ...");

        var txnProducer = new KafkaProducer<String, Transaction>(properties);
        var totalTransactions= 10_000_000;

        logger.info("Generating {} transactions ...", totalTransactions);
        var count = 0;

        for (int i = 0; i < totalTransactions; i ++) {
            var transaction = DataGenerator.generateTransaction(AppConfig.TOTAL_CUSTOMERS);
            StreamingUtils.handleMessage(txnProducer, AppConfig.TRANSACTIONS_TOPIC, transaction.getUserId(), transaction);
            count++;
            if (count % 1000000 == 0) {
                logger.info("Total so far {}.", count);
            }
        }
        closeProducer(txnProducer);
    }
}
