#
#
#
from com.example.utils.AvroUtils import load_avro_schema, load_avro_str
from confluent_kafka import Producer, SerializingProducer
from confluent_kafka.avro import AvroProducer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer
from confluent_kafka.serialization import StringSerializer


#
class ProducerFactory(object):
    # Class Variable
    producer_type = None

    def __init__(self) :
        producer_type = ""

    def switch(self, producer_type):
        switch={
            "simple": "simple",
            "deserializer": "deserializer",
        }
        return switch.get(producer_type, "Invalid input")

    #
    #
    #
    @staticmethod
    def simple(producer_config):
        return Producer(producer_config)

    #
    #
    #
    @staticmethod
    def avro(producer_config, schema_file):

        key_schema, value_schema = load_avro_schema(schema_file)
        #
        producer_config.update({
            'schema.registry.url': 'http://schema-registry.sandbox.net:8081'
        })
        return AvroProducer(producer_config, default_key_schema=key_schema, default_value_schema=value_schema)


    @staticmethod
    def serializer(producer_config, schema_file, serializer_ctx):
        #
        key_str, schema_str = load_avro_str(schema_file)
        schema_registry_client = SchemaRegistryClient({'url': 'http://schema-registry.sandbox.net:8081'})
        custom_serializer = AvroSerializer(schema_registry_client, schema_str, serializer_ctx)
        string_serializer = StringSerializer('utf_8')
        producer_config.update({
            'key.serializer': string_serializer,
            'value.serializer': custom_serializer
        })
        return SerializingProducer(producer_config)

    #
    #
    #
    @staticmethod
    def json(producer_config):
        string_serializer = StringSerializer('utf_8')
        producer_config.update({
            'key.serializer': string_serializer,
            'value.serializer': string_serializer
        })
        return SerializingProducer(producer_config)

    #
    #
    #
    @staticmethod
    def setup(producer_type, producer_config):
        producer = None
        if(producer_type== "simple"):
            producer = ProducerFactory.simple(producer_config)
        elif(producer_type == "avro"):
            producer = ProducerFactory.avro(producer_config)
        elif(producer_type == "json"):
            producer = ProducerFactory.json(producer_config)
        else:
            print("Give input of numbers from 1 to 3")

        return producer