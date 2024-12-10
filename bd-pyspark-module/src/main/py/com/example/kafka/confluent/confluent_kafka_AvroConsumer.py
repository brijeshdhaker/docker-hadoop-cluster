from confluent_kafka.avro.serializer import SerializerError

from confluent_kafka_ConfigFactory import KafkaConfigFactory
from confluent_kafka_ConsumerFactory import ConsumerFactory

consumer_config = KafkaConfigFactory.consumer('PLAINTEXT')
consumer = ConsumerFactory.avro(consumer_config)
consumer.subscribe(['transaction-avro-topic'])

while True:
    try:
        msg = consumer.poll(10)

    except SerializerError as e:
        print("Message deserialization failed for {}: {}".format(msg, e))
        break

    if msg is None:
        continue

    if msg.error():
        print("AvroConsumer error: {}".format(msg.error()))
        continue

    # application-specific processing
    print("----------------------")
    print(msg.value())
    print("----------------------")
    print("")

consumer.close()