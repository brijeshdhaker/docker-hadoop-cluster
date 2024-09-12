#!/usr/bin/env python
# =============================================================================
#
# Produce messages to Confluent Cloud
# Using Confluent Python Client for Apache Kafka
# Writes Avro data, integration with Confluent Cloud Schema Registry
#
# =============================================================================
import random
from datetime import datetime
from time import sleep
from uuid import uuid4

from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ProducerFactory import ProducerFactory


#
#
#
def unix_time_millis(dt):
    epoch = datetime.utcfromtimestamp(0)
    return (dt - epoch).total_seconds() * 1000.0

#
#
#
if __name__ == '__main__':

    # Read arguments and configurations and initialize
    topic = "kafka-avro-topic"
    producer_config = KafkaConfigFactory.producer('PLAINTEXT')
    avroProducer = ProducerFactory.avro(producer_config,"resources/avro/user-record.avsc")

    u_names = ["Brijesh K", "Neeta K", "Keshvi K", "Tejas K"]

    while True:
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
        # Serve on_delivery callbacks from previous calls to produce()
        avroProducer.poll(0.0)
        #
        print("Avro Record: {}\t{} at time {}".format(user['uuid'], user['name'], user['addTs']))
        avroProducer.produce(topic=topic, key=key, value=user)
        sleep(1)

    avroProducer.flush()
    print("{} messages were produced to topic {}!".format(delivered_records, topic))
