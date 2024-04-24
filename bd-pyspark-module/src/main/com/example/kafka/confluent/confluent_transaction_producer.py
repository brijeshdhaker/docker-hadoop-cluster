#!/usr/bin/python2

from time import sleep
from com.example.models.Transaction import Transaction
from confluent_kafka_ProducerFactory import ProducerFactory
from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_usecase import synchronous_produce, delivered_records

def sink_transaction(args):

    # Read arguments and configurations and initialize
    topic = args['topic']

    # Create Producer instance
    producer_conf = KafkaConfigFactory.producer(args["auth_type"])
    producer = ProducerFactory.json(producer_conf)

    #
    while True:
        producer.poll(0.0)
        try:

            transaction = Transaction.random()

            # json.dumps() function converts a Python object into a json string.
            # record_value = json.dumps({'count': random.randint(1000, 5000)})
            event = {
                "key": str(transaction.uuid),
                "value": str(transaction.to_dict())
            }
            #
            synchronous_produce(producer, topic, event)
            print("Produced Record: {}\t\t{}".format(event['key'], event['value']))
            sleep(2)

        except KeyboardInterrupt:
            break
        except ValueError:
            print("Invalid input, discarding record...")
            continue


if __name__ == '__main__':
    args = {
        'topic': "spark-text-txn-topic",
        'auth_type': "PLAINTEXT"
    }
    sink_transaction(args)