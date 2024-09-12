#
#
#

import json
from com.example.utils.AvroUtils import load_avro_str
from confluent_kafka import Consumer, DeserializingConsumer
from confluent_kafka.avro import AvroConsumer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import StringDeserializer


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
        string_deserializer = StringDeserializer('utf_8')
        consumer_config.update({
            'key.deserializer': string_deserializer,
            'value.deserializer': string_deserializer,
            'group.id': 'kafka_simple_cg'
        })
        consumer = Consumer(consumer_config)
        return consumer

    #
    #
    #
    @staticmethod
    def avro(consumer_config):
        consumer_config.update({
            'schema.registry.url': 'http://schemaregistry:8081',
            'auto.offset.reset': "earliest",
            'group.id': 'kafka_avro_cg'
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
            'auto.offset.reset': "earliest",
            'group.id': 'kafka_json_cg'
        })
        consumer = DeserializingConsumer(consumer_config)
        return consumer


    @staticmethod
    def serializer(consumer_config, schema_file, deserializer_ctx):
        #
        key_str, schema_str = load_avro_str(schema_file)
        schema_registry_client = SchemaRegistryClient({'url': 'http://schemaregistry:8081'})
        custom_deserializer = AvroDeserializer(schema_registry_client, schema_str, deserializer_ctx)
        string_deserializer = StringDeserializer('utf_8')
        consumer_config.update({
            'key.deserializer': string_deserializer,
            'value.deserializer': custom_deserializer,
            'group.id': 'kafka_avro_cg'
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