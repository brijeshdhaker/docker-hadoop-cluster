#!/bin/bash

# set -eu

P_CONFIG_FILE=librdkafka_sasl_ssl.config
C_CONFIG_FILE=librdkafka_sasl_ssl.config

# Set topic name
topic_name=kafka-simple-topic


# Produce messages
echo "#"
echo "# Produce messages"
echo "#"
num_messages=10
#(for i in `seq 1 100`; do echo "${i},{\"key\":${i}, \"value\":${i}}" ; done) | \

docker run --rm \
--hostname=producer.sandbox.net \
--network sandbox.net \
--volume /apps:/apps \
--volume ../bd-docker-sandbox/conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /apps/sandbox/kafka/cnf/$P_CONFIG_FILE -P -K '\t' -t $topic_name -l /apps/sandbox/kafka/json_messages.txt

echo "#"
echo "# Consume messages"
echo "#"

# -K ,: pass key and value, separated by a comma
# -e: exit successfully when last message received

docker run --rm \
--hostname=consumer.sandbox.net \
--network sandbox.net \
--volume /apps:/apps \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /apps/sandbox/kafka/cnf/$C_CONFIG_FILE -C -K '\t' -t $topic_name -f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e