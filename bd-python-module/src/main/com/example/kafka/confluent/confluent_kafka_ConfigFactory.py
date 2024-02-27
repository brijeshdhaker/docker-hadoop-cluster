#
#
#

from confluent_kafka import Consumer,DeserializingConsumer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.avro import AvroConsumer
from confluent_kafka.avro.serializer import SerializerError
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import StringDeserializer
from confluent_kafka_usecase import commit_completed, delivery_report

import json
import socket
#
class KafkaConfigFactory(object):
    # Class Variable
    kafka_config = None

    def __init__(self) :
        self.kafka_config = {
            'bootstrap.servers': 'kafkabroker.sandbox.net:9092',
            'group.id': 'kafka_simple_cg'
        }


    def switch(self,consumer_type):
        switch={
            "simple": "",
            "deserializer": "deserializer",
        }
        return switch.get(consumer_type, "Invalid input")


    @staticmethod
    def producer(connection_type):
        kafka_config = KafkaConfigFactory.__setup(connection_type)
        kafka_config.update({
            'on_delivery': delivery_report
        })
        return kafka_config


    @staticmethod
    def consumer(connection_type):
        kafka_config = KafkaConfigFactory.__setup(connection_type)
        kafka_config.update({
            'enable.auto.commit': False,
            'on_commit': commit_completed
        })
        return kafka_config

    @staticmethod
    def __setup(connection_type):
        kafka_config = {
            'client.id': socket.gethostname(),
        }

        if(connection_type=="PLAINTEXT"):
            kafka_config.update({
                'bootstrap.servers': 'kafkabroker.sandbox.net:9092',
            })
        elif(connection_type=="SASL_PLAINTEXT"):
            kafka_config.update({
                'bootstrap.servers': 'kafkabroker.sandbox.net:19092',
                'sasl.mechanism': 'PLAIN',
                'sasl.username': 'admin',
                'sasl.password': 'admin-secret',
                'security.protocol': 'SASL_PLAINTEXT',
                'session.timeout.ms': 10000
            })
        elif(connection_type=="SASL_SSL"):
            kafka_config.update({
                'bootstrap.servers': 'kafkabroker.sandbox.net:19093',
                'sasl.mechanism': 'GSSAPI',
                'security.protocol': 'SASL_SSL',
                'sasl.kerberos.service.name': 'kafka',
                'sasl.kerberos.keytab': '/apps/security/keytabs/services/kafkaclient.keytab',
                'sasl.kerberos.principal': 'kafkaclient@SANDBOX.NET',
                'ssl.key.location': '/apps/security/ssl/clients.key',
                'ssl.key.password': 'confluent',
                'ssl.certificate.location': '/apps/security/ssl/clients-signed.crt',
                'ssl.ca.location': '/apps/security/ssl/sandbox-ca.pem'
            })
        elif(connection_type=="SSL"):
            kafka_config.update({
                'bootstrap.servers': 'kafkabroker.sandbox.net:19094',
                'security.protocol': 'SSL',
                'ssl.key.location': '/apps/security/ssl/clients.key',
                'ssl.key.password': 'confluent',
                'ssl.certificate.location': '/apps/security/ssl/clients-signed.crt',
                'ssl.ca.location': '/apps/security/ssl/sandbox-ca.pem'
            })

        return kafka_config