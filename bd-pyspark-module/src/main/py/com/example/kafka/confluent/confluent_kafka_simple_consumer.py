import sys
from confluent_kafka import KafkaException, KafkaError

from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ConsumerFactory import ConsumerFactory
from confluent_kafka_usecase import msg_process

#
#
#
TOPIC = "kafka-simple-topic"

#
#     'enable.auto.commit': False,
#
consumer_config = KafkaConfigFactory.consumer('PLAINTEXT')
consumer = ConsumerFactory.simple(consumer_config)
consumer.subscribe([TOPIC])
MIN_COMMIT_COUNT = 10

try:
    msg_count = 0
    while True:

        msg = consumer.poll(1.0)
        if msg is None: continue

        if msg.error():
            if msg.error().code() == KafkaError._PARTITION_EOF:
                # End of partition event
                sys.stderr.write('%% %s [%d] reached end at offset %d\n' %
                                 (msg.topic(), msg.partition(), msg.offset()))
            elif msg.error():
                raise KafkaException(msg.error())
        else:
            # application-specific processing
            print("----------------------")
            msg_process(msg)
            print("----------------------")
            print("")
            msg_count += 1
#            if msg_count % MIN_COMMIT_COUNT == 0:
#                consumer.commit(asynchronous=True)

except:
    print("Something went wrong")

finally:
    # Close down consumer to commit final offsets.
    consumer.close()
