import json
from kafka import KafkaConsumer, ConsumerRebalanceListener


def msg_process(msg):
    print('Key     : {}'.format(msg.key))
    print('Message : {}'.format(msg.value))


def commit_completed(err, partitions):
    if err:
        print(str(err))
    else:
        print("Committed partition offsets: " + str(partitions))


running = True


def shutdown():
    running = False


def default_offset_commit_callback(offsets, response):
    print(str(offsets))


"""
#
# Exactly Once Consumer
#
"""


class SaveOffsetsRebalanceListener(ConsumerRebalanceListener):

    def __init__(self, consumer):
        self.consumer = consumer

    def on_partitions_revoked(self, revoked):
        pass

    def on_partitions_assigned(self, assigned):
        pass


"""
#
#
#
"""
MIN_COMMIT_COUNT = 100
TOPIC = "kafka-partitioned-topic"

def val_deserializer(str):
    if(str):
        mval = str.decode('utf-8')
    else:
        mval = ""
    return mval

key_deserializer = lambda k: k.decode("utf-8")
value_deserializer = lambda v: json.loads(v.decode("utf-8"))

consumer = KafkaConsumer(
    bootstrap_servers='kafkabroker.sandbox.net:9092',
    client_id='kafka_partitioned_consumer',
    group_id='kafka_partitioned_cg',
    auto_offset_reset='earliest',
    enable_auto_commit=True,
    default_offset_commit_callback=default_offset_commit_callback,
    key_deserializer=val_deserializer,
    value_deserializer=val_deserializer
)

consumer.subscribe([TOPIC])


try:
    msg_count = 0
    while True:

        # msg = next(consumer)
        results = consumer.poll(timeout_ms=2000)

        if results is None:
            continue
        else:
            # application-specific processing
            for messages in results.values():
                for m in messages:

                    print("----------------------")
                    print("Topic : %s \nPartition: %d  \nOffset : %d" % (m.topic, m.partition, m.offset))
                    msg_process(m)
                    print("----------------------")
                    print("")
                    msg_count += 1
                    if msg_count % MIN_COMMIT_COUNT == 0:
                        a = 0
                        # print("Try for commit.")
                        # commits the latest offsets returned by poll
                        # consumer.commit()
                        # consumer.commit(asynchronous=True)

except:
    print("Something went wrong")

finally:
    # Close down consumer to commit final offsets.
    consumer.close()
