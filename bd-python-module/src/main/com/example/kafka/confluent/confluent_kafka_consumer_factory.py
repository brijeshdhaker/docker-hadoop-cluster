#
#
#

from confluent_kafka import Consumer,DeserializingConsumer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.avro import AvroConsumer
from confluent_kafka.avro.serializer import SerializerError
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import StringDeserializer
from confluent_kafka_usecase import commit_completed

import json

#
class ConsumerFactory(object):
    # Class Variable
    consumer_type = None

    def __init__(self) :
        consumer_type = ""

    def switch(self,consumer_type):
        switch={
            "simple": "",
            "deserializer": "deserializer",
        }
        return switch.get(consumer_type, "Invalid input")

    #
    #
    #
    @staticmethod
    def simple(consumer_config):
        consumer = Consumer(consumer_config)
        return consumer

    #
    #
    #
    @staticmethod
    def avro(consumer_config):

        consumer_config.update({
            'schema.registry.url': 'http://schemaregistry:8081',
            'auto.offset.reset': "earliest"
        })
        consumer = AvroConsumer(consumer_config)
        return consumer


    #
    #
    #
    @staticmethod
    def json(consumer_config):
        string_deserializer = StringDeserializer('utf_8')
        json_deserializer = lambda v: json.loads(v.decode("utf-8"))
        consumer_config.update({
            'key.deserializer': string_deserializer,
            'value.deserializer': json_deserializer,
            'auto.offset.reset': "earliest"
        })
        consumer = DeserializingConsumer(consumer_config)
        return consumer

    #
    #
    #
    @staticmethod
    def setup(consumer_type, consumer_config):
        consumer = None
        if(consumer_type=="simple"):
            consumer = ConsumerFactory.simple(consumer_config)
        elif(consumer_type=="avro"):
            consumer = ConsumerFactory.avro(consumer_config)
        elif(consumer_type=="json"):
            consumer = ConsumerFactory.json(consumer_config)
        else:
            print("Give input of numbers from 1 to 3")

        return consumer