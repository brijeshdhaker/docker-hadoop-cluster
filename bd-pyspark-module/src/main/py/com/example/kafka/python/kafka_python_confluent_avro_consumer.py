from com.example.utils.AvroUtils import load_avro_json
from confluent_kafka.avro import SerializerError
from fastavro import schemaless_reader, parse_schema
from io import BytesIO
from kafka import KafkaConsumer
from os.path import expanduser
from pathlib import Path

"""
The simplest way to use the iterator API is to complete the processing and persisting of the result data of the message 
each iteration. This will give you at-least-once behaviour as the latest processed offset may or may not be committed to 
kafka before each iteration meaning that in the event of a failure the consumer will restart from the last comitted 
offset which may not be the latest message that was successfully processed.
"""
AVRO_PATH = Path(expanduser("~"), "IdeaProjects", "spark-python-examples", "resources", "avro",
                 "transaction-record.avsc")
key_schema, value_schema = load_avro_json(AVRO_PATH)

key_deserializer = lambda k: k.decode("utf-8")


def avroValueDeSerializer(raw_bytes):
    if len(raw_bytes) <= 5:
        raise SerializerError("message is too small to decode")
    message_bytes = BytesIO(raw_bytes)
    message_bytes.seek(5)
    # rb = BytesIO(message_bytes)
    record = schemaless_reader(message_bytes, parse_schema(value_schema))
    return record



RUNNING = True
TOPIC = "txn-avro-stream-topic"
MIN_COMMIT_COUNT = 10

consumer = KafkaConsumer(
    bootstrap_servers='kafkabroker.sandbox.net:9092',
    client_id='kafka_python_avro_consumer-client',
    group_id='kafka_python_avro_consumer-cg',
    key_deserializer=key_deserializer,
    value_deserializer=avroValueDeSerializer
)
consumer.subscribe(TOPIC)

def shutdown():
    RUNNING = False

def msg_process(msg):
    print('Received message: {}'.format(msg.value))

def consume_messages():
    for message in consumer:
        msg_process(message)

if __name__ == '__main__':
    consume_messages()
    shutdown()
