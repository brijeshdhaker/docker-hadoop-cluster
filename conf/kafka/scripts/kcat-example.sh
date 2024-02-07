#!/bin/bash

# set -eu

P_CONFIG_FILE=librdkafka_sasl_ssl.config
C_CONFIG_FILE=librdkafka_sasl_ssl.config

# Set topic name
topic_name=kcat-test-topic


# Produce messages
echo "#"
echo "# Produce messages"
echo "#"
num_messages=10
#(for i in `seq 1 $num_messages`; do echo "alice,{\"count\":${i}}" ; done) | \

docker run --rm \
--hostname=producer.sandbox.net \
--network sandbox.net \
--volume /apps:/apps \
--volume ./conf/kafka/data:/etc/kafka/data \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /apps/sandbox/kafka/cnf/$P_CONFIG_FILE -K , -t $topic_name -P -l /etc/kafka/data/kcat_messages.txt

echo "#"
echo "# Consume messages"
echo "#"

# -K ,: pass key and value, separated by a comma
# -e: exit successfully when last message received

docker run --rm \
--hostname=consumer.sandbox.net \
--network sandbox.net \
--volume /apps:/apps \
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kafka/data:/etc/kafka/data \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /apps/sandbox/kafka/cnf/$C_CONFIG_FILE -K , -C -t $topic_name -e