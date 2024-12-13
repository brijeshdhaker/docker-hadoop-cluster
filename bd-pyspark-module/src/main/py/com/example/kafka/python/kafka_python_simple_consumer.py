from kafka import KafkaConsumer

"""
The simplest way to use the iterator API is to complete the processing and persisting of the result data of the message 
each iteration. This will give you at-least-once behaviour as the latest processed offset may or may not be committed to 
kafka before each iteration meaning that in the event of a failure the consumer will restart from the last comitted 
offset which may not be the latest message that was successfully processed.
"""

RUNNING = True
TOPIC = "kafka-simple-topic"
MIN_COMMIT_COUNT = 10

def str_deserializer(str):
    if(str):
        mval = str.decode('utf-8')
    else:
        mval = ""
    return mval

key_deserializer = lambda k: k.decode("utf-8")
value_deserializer = lambda v: v.decode("utf-8")

consumer = KafkaConsumer(
    bootstrap_servers='kafkabroker.sandbox.net:9092',
    client_id='kafka_simple_consumer',
    group_id='kafka_simple_cg',
    key_deserializer=str_deserializer,
    value_deserializer=str_deserializer,
    auto_offset_reset='earliest',
    enable_auto_commit=True
)
""" 
Partitions will be dynamically assigned via a group coordinator. 
Topic subscriptions are not incremental: this list will replace the current assignment (if there is one).
"""
consumer.subscribe(TOPIC)

def shutdown():
    RUNNING = False

def msg_process(msg):
    print('Key     : {}'.format(msg.key))
    print('Message : {}'.format(msg.value))

def consume_messages():
    for m in consumer:
        # message value and key are raw bytes -- decode if necessary!
        # e.g., for unicode: `message.value.decode('utf-8')`
        print("----------------------")
        print("Topic : %s \nPartition: %d  \nOffset : %d" % (m.topic, m.partition, m.offset))
        msg_process(m)
        print("----------------------")
        print("")

if __name__ == '__main__':
    consume_messages()
    shutdown()
