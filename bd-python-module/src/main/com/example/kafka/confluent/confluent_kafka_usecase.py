
import sys
from time import sleep
from confluent_kafka import KafkaException, KafkaError

#
#
#
running = True
MIN_COMMIT_COUNT  = 10
delivered_records = { }


#
#
#
def delivery_report(err, msg):
    global delivered_records
    """Delivery report handler called on successful or failed delivery of message """
    if err is not None:
        print("Failed to deliver message: {}".format(err))
    else:
        delivered_messages = delivered_records.get(str(msg.topic()), 0)
        delivered_messages = delivered_messages+1
        delivered_records.update({ str(msg.topic()) : delivered_messages })
        print("Produced record to topic {} partition [{}] @ offset {}"
              .format(msg.topic(), msg.partition(), msg.offset()))

#
#
#
def asynchronous_produce(producer, topic, event):
    producer.produce(topic, key=event["key"], value=event["value"], callback=delivery_report)
    # Wait up to 1 second for events. Callbacks will be invoked during
    # this method call if the message is acknowledged.
    producer.poll(1)

#
#
# Typically, flush() should be called prior to shutting down the producer to ensure all outstanding/queued/in-flight messages are delivered.
def synchronous_produce(producer, topic, event):
    producer.produce(topic, key=event["key"], value=event["value"])
    producer.flush()

#
#
#
def msg_process(msg):
    if(msg):
        print("Topic   : %s Partition: %d  Offset : %d" % (msg.topic(), msg.partition(), msg.offset()))
        print('Key     : {}'.format(msg.key))
        print('Message : {}'.format(msg.value))
        #print('Received message: {}'.format(msg.value().decode('utf-8')))

#
#
#
def basic_consume_loop(consumer, topics):
    try:
        consumer.subscribe(topics)

        while running:
            msg = consumer.poll(timeout=1.0)
            if msg is None: continue

            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    # End of partition event
                    sys.stderr.write('%% %s [%d] reached end at offset %d\n' %
                                     (msg.topic(), msg.partition(), msg.offset()))
                elif msg.error():
                    raise KafkaException(msg.error())
            else:
                msg_process(msg)
    finally:
        # Close down consumer to commit final offsets.
        consumer.close()

#
#
#
def synchronous_commit_loop(consumer, topics):
    try:
        consumer.subscribe(topics)

        msg_count = 0
        while running:
            msg = consumer.poll(timeout=1.0)
            if msg is None: continue

            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    # End of partition event
                    sys.stderr.write('%% %s [%d] reached end at offset %d\n' %
                                     (msg.topic(), msg.partition(), msg.offset()))
                elif msg.error():
                    raise KafkaException(msg.error())
            else:
                msg_process(msg)
                msg_count += 1
                if msg_count % MIN_COMMIT_COUNT == 0:
                    consumer.commit(asynchronous=False)
    finally:
        # Close down consumer to commit final offsets.
        consumer.close()

#
#
# delivery_guarantees_consume_loop
def at_most_once_consume(consumer, topics):
    try:
        #
        consumer.subscribe(topics)

        #
        while running:
            msg = consumer.poll(timeout=1.0)
            if msg is None: continue

            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    # End of partition event
                    sys.stderr.write('%% %s [%d] reached end at offset %d\n' %
                                     (msg.topic(), msg.partition(), msg.offset()))
                elif msg.error():
                    raise KafkaException(msg.error())
            else:
                consumer.commit(asynchronous=False)
                msg_process(msg)

    except:
        #
        print("Something went wrong")
    finally:
        #
        print("Finally Actions")

#
#
#
def asynchronous_commit_loop(consumer, topics):
    try:
        consumer.subscribe(topics)
        msg_count = 0
        while running:
            msg = consumer.poll(timeout=1.0)
            if msg is None:
                continue

            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    # End of partition event
                    sys.stderr.write('%% %s [%d] reached end at offset %d\n' %(msg.topic(), msg.partition(), msg.offset()))
                elif msg.error():
                    raise KafkaException(msg.error())
            else:
                msg_process(msg)
                msg_count += 1
                if msg_count % MIN_COMMIT_COUNT == 0:
                    consumer.commit(asynchronous=True)
    finally:
        # Close down consumer to commit final offsets.
        consumer.close()

#
#
#
def commit_completed(err, partitions):
    if err:
        print(str(err))
    else:
        print("Committed partition offsets: " + str(partitions))

#
#
#
def shutdown():
    running = False

