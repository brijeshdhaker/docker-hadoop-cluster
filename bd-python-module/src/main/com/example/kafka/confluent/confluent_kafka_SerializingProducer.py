#!/usr/bin/env python
#
# confluent_kafka_SerializingProducer.py -b kafkabroker.sandbox.net:9092 -s http://schemaregistry:8081 -t test-avro-topic
#
#
# This is a simple example of the SerializingProducer using Avro.
#
import random
from datetime import datetime
from time import sleep
import argparse
from uuid import uuid4

from builtins import input

from confluent_kafka import SerializingProducer
from confluent_kafka.serialization import StringSerializer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer
from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ProducerFactory import ProducerFactory
from confluent_kafka_usecase import asynchronous_produce, delivered_records

class User(object):
    """
    User record
    Args:
        name (str): User's name
        favorite_number (int): User's favorite number
        favorite_color (str): User's favorite color
        address(str): User's address; confidential
    """
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


def user_to_dict(user, ctx):
    """
    Returns a dict representation of a User instance for serialization.
    Args:
        user (User): User instance.
        ctx (SerializationContext): Metadata pertaining to the serialization
            operation.
    Returns:
        dict: Dict populated with user attributes to be serialized.
    """
    # User._address must not be serialized; omit from dict
    return dict(
        id=user.id,
        uuid=user.uuid,
        name=user.name,
        emailAddr=user.emailAddr,
        age=user.age,
        dob=user.dob,
        height=user.height,
        roles=user.roles,
        status=user.status,
        addTs=user.addTs,
        updTs=user.updTs
    )


def delivery_report(err, msg):
    """
    Reports the failure or success of a message delivery.
    Args:
        err (KafkaError): The error that occurred on None on success.
        msg (Message): The message that was produced or failed.
    Note:
        In the delivery report callback the Message.key() and Message.value()
        will be the binary format as encoded by any configured Serializers and
        not the same object that was passed to produce().
        If you wish to pass the original object(s) for key and value to delivery
        report callback we recommend a bound callback or lambda where you pass
        the objects along.
    """
    if err is not None:
        print("Delivery failed for User record {}: {}".format(msg.key(), err))
        return
    print('User record {} successfully produced to {} [{}] at offset {}'.format(
        msg.key(), msg.topic(), msg.partition(), msg.offset()))


def main(args):

    topic = "kafka-avro-topic"
    schema_str = """
    {
        "namespace": "confluent.io.examples.serialization.avro",
        "name": "User",
        "type": "record",
        "fields": [
            {"name": "name", "type": "string"},
            {"name": "favorite_number", "type": "int"},
            {"name": "favorite_color", "type": "string"}
        ]
    }
    """
    # schema_registry_conf = {'url': 'http://schemaregistry:8081'}
    # schema_registry_client = SchemaRegistryClient(schema_registry_conf)
    # avro_serializer = AvroSerializer(schema_registry_client, schema_str, user_to_dict)

    producer_conf = KafkaConfigFactory.producer('PLAINTEXT')
    producer = ProducerFactory.serializer(producer_conf,"resources/avro/user-record.avsc", user_to_dict)

    # producer_conf = {'bootstrap.servers': args.bootstrap_servers,
    #                  'key.serializer': StringSerializer('utf_8'),
    #                  'value.serializer': avro_serializer}
    #
    # producer = SerializingProducer(producer_conf)

    print("Producing user records to topic {}. ^C to exit.".format(topic))
    while True:
        # Serve on_delivery callbacks from previous calls to produce()
        producer.poll(0.0)
        u_names = ["Brijesh K", "Neeta K", "Keshvi K", "Tejas K"]
        try:
            #
            # int(time.timestamp() * 1000)
            event_datetime = datetime.now().timestamp()
            # d_in_ms = int(event_datetime.strftime("%S"))
            user = {
                'id': random.randint(1000, 5000),
                'uuid': str(uuid4()),
                'name': random.choice(u_names),
                'emailAddr': "abc@gmail.com",
                'age': random.randint(18, 70),
                'dob': random.randint(18, 70),
                'height': round(random.uniform(5.0, 7.0)),
                'roles': ['admin', 'Technology'],
                'status': 'Active',
                'addTs': int(event_datetime),
                'updTs': int(event_datetime)
            }
            key = user['uuid']
            producer.produce(topic=topic, key=user['uuid'], value=user)
        except KeyboardInterrupt:
            break
        except ValueError:
            print("Invalid input, discarding record...")
            continue

    print("\nFlushing records...")
    producer.flush()


if __name__ == '__main__':
    main({})