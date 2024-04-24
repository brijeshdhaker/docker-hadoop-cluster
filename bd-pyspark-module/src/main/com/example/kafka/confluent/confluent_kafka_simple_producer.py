#!/usr/bin/env python
# =============================================================================
#
# Produce messages to Confluent Cloud
# Using Confluent Python Client for Apache Kafka
#
# =============================================================================
from time import sleep
import random
from confluent_kafka_ProducerFactory import ProducerFactory
from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_usecase import synchronous_produce, delivered_records
#
#
#


if __name__ == '__main__':

    TOPIC = "kafka-simple-topic"
    # Create Producer instance
    consumer_config = KafkaConfigFactory.consumer('PLAINTEXT')
    producer = ProducerFactory.simple(consumer_config)
    produced_records = 0

    while True:
        #
        produced_records += 1
        #
        KEYS = ["A", "B", "C", "D"]
        record_key = random.choice(KEYS)
        # record_value = json.dumps({'key': record_key, 'index': produced_records})
        record_value = "This is test event {} of type {}".format(produced_records, record_key)
        event = {"key": record_key, "value": record_value}
        #
        synchronous_produce(producer,TOPIC,event)
        sleep(1)

    print("{} messages were produced to topic {}!".format(delivered_records, topic))

