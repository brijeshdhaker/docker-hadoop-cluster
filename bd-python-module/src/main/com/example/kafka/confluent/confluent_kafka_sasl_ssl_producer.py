#!/usr/bin/env python
# =============================================================================
#
# Produce messages to Confluent Cloud
# Using Confluent Python Client for Apache Kafka
# /apps/sandbox/kafka/py/confluent_kafka_sasl_ssl_producer.py
# =============================================================================
from time import sleep
from confluent_kafka import Producer, KafkaError
import random
from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ProducerFactory import ProducerFactory
from confluent_kafka_usecase import asynchronous_produce, delivered_records
#
#
#
TOPIC = "kafka-simple-topic"

# Create Producer instance
producer_config = KafkaConfigFactory.producer('SASL_SSL')
producer = ProducerFactory.simple(producer_config)

#
#
#
def produce_message(event):
    #
    producer.produce(TOPIC, key=event["key"], value=event["value"], on_delivery=acked)
    # from previous produce() calls.
    producer.poll(0)
    # flush the message buffer to force message delivery to broker on each iteration
    producer.flush()


if __name__ == '__main__':
    produced_records = 0
    while produced_records <= 10 :
        #
        produced_records += 1
        #
        KEYS = ["A", "B", "C", "D"]
        record_key = random.choice(KEYS)
        # record_value = json.dumps({'key': record_key, 'index': produced_records})
        record_value = "This is test event {} of type {}".format(produced_records, record_key)
        event = {"key": record_key, "value": record_value}
        asynchronous_produce(producer, TOPIC, event)
        sleep(1)

    print("{} messages ere produced to topic {}!".format(delivered_records.get(str(TOPIC), 0), TOPIC))

