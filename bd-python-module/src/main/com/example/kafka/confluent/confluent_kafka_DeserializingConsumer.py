#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# Copyright 2020 Confluent Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# confluent_kafka_DeserializingConsumer.py -b kafkabroker.sandbox.net:9092 -s http://schemaregistry:8081 -t test-avro-topic -g test-avro-cg
#

#
# This is a simple example of the SerializingProducer using Avro.
#
import argparse

from confluent_kafka import DeserializingConsumer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import StringDeserializer
from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ConsumerFactory import ConsumerFactory

class User(object):

    def __init__(self, id, uuid, name, emailAddr, age, dob, height, roles, status, addTs, updTs):
        self.id = id
        self.uuid = uuid
        self.name = name
        self.emailAddr = emailAddr
        self.age = age
        self.dob = dob
        self.height = height
        self.roles = roles
        self.status = status
        self.addTs = addTs
        self.updTs = updTs


def dict_to_user(obj, ctx):
    if obj is None:
        return None

    return User(
        id=obj['id'],
        uuid=obj['uuid'],
        name=obj['name'],
        emailAddr=obj['emailAddr'],
        age=obj['age'],
        dob=obj['dob'],
        height=obj['height'],
        roles=obj['roles'],
        status=obj['status'],
        addTs=obj['addTs'],
        updTs=obj['updTs']
    )


def main(args):

    topic = "kafka-avro-topic"
    consumer_conf = KafkaConfigFactory.consumer('PLAINTEXT')
    consumer = ConsumerFactory.serializer(consumer_conf,"resources/avro/user-record.avsc", dict_to_user)

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
    # parser = argparse.ArgumentParser(description="Consumer Example client with serialization capabilities")
    # parser.add_argument('-b', dest="bootstrap_servers", required=True, help="Bootstrap broker(s) (host[:port])")
    # parser.add_argument('-s', dest="schema_registry", required=True, help="Schema Registry (http(s)://host[:port]")
    # parser.add_argument('-t', dest="topic", default="example_serde_avro", help="Topic name")
    # parser.add_argument('-g', dest="group", default="example_serde_avro", help="Consumer group")
    # parser.parse_args()
    main({})
