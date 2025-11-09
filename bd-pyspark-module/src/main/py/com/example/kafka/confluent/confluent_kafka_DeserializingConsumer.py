#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# confluent_kafka_DeserializingConsumer.py -b kafka-broker.sandbox.net:9092 -s http://schema-registry:8081 -t test-avro-topic -g test-avro-cg
#

from com.example.models.User import User

from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ConsumerFactory import ConsumerFactory


#
#
#
def serializer_source(args):

    topic = args["topic"]
    consumer_conf = KafkaConfigFactory.consumer(args["auth_type"])
    consumer = ConsumerFactory.serializer(consumer_conf,"resources/avro/user-record.avsc", User.to_obj)

    consumer.subscribe([topic])
    while True:
        try:
            # SIGINT can't be handled when polling, limit timeout to 1 second.
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            key = msg.key()
            user = msg.value()
            if user is not None:
                print("Key : {} User : {} ".format(msg.key(), user.name))

        except KeyboardInterrupt:
            break

    consumer.close()


if __name__ == '__main__':
    args = {
        'topic': "kafka-avro-topic",
        'auth_type': "PLAINTEXT"
    }
    serializer_source(args)
