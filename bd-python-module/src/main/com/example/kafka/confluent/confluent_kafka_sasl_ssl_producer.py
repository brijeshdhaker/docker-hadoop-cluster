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
#
#
#
TOPIC = "kafka-simple-topic"

# Create Producer instance
producer = Producer({
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

#
#
#
def acked(err, msg):
    delivered_records = 0
    """Delivery report handler called on successful or failed delivery of message """
    if err is not None:
        print("Failed to deliver message: {}".format(err))
    else:
        delivered_records += 1
        print("Produced record to topic {} partition [{}] @ offset {}"
              .format(msg.topic(), msg.partition(), msg.offset()))

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
    while True:
        #
        produced_records += 1
        #
        KEYS = ["A", "B", "C", "D"]
        record_key = random.choice(KEYS)
        # record_value = json.dumps({'key': record_key, 'index': produced_records})
        record_value = "This is test event {} of type {}".format(produced_records, record_key)
        produce_message({"key": record_key, "value": record_value})
        sleep(1)

    print("{} messages were produced to topic {}!".format(delivered_records, topic))

