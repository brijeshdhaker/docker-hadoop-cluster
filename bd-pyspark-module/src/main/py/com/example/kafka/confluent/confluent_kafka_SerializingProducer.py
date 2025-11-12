#!/usr/bin/env python
#
# confluent_kafka_SerializingProducer.py -b kafka-broker.sandbox.net:9092 -s http://schema-registry:8081 -t test-avro-topic
#
#
# This is a simple example of the SerializingProducer using Avro.
#
from com.example.models.User import User
from datetime import datetime
from time import sleep

from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ProducerFactory import ProducerFactory


#
#
#
def serializer_sink(args):

    topic = args['topic']
    producer_conf = KafkaConfigFactory.producer(args['auth_type'])
    producer = ProducerFactory.serializer(producer_conf,"resources/avro/user-record.avsc", User.to_dict)

    #
    print("Producing user records to topic {}. ^C to exit.".format(topic))
    while True:
        # Serve on_delivery callbacks from previous calls to produce()
        producer.poll(0.0)
        try:
            #
            # int(time.timestamp() * 1000)
            event_datetime = datetime.now().timestamp()
            # d_in_ms = int(event_datetime.strftime("%S"))
            user = User.random()
            key = user.uuid
            producer.produce(topic=topic, key=key, value=user)
            sleep(1)
        except KeyboardInterrupt:
            break
        except ValueError:
            print("Invalid input, discarding record...")
            continue

    print("\nFlushing records...")
    producer.flush()


if __name__ != '__main__':
    pass
else:
    args = {
        'topic': "kafka-avro-topic",
        'auth_type': "PLAINTEXT"
    }
    serializer_sink(args)

#