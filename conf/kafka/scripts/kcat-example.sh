#!/bin/bash

# set -eu

P_CONFIG_FILE=librdkafka_producer.config
C_CONFIG_FILE=librdkafka_consumer.config

# Set topic name
topic_name=kcat-test-topic


# Produce messages
num_messages=10
#(for i in `seq 1 $num_messages`; do echo "alice,{\"count\":${i}}" ; done) | \

   docker run --rm \
   --hostname=clients.sandbox.net \
   --network sandbox.net \
   --volume ./conf/kafka/secrets:/etc/kafka/secrets \
   --volume ./conf/kafka/data:/etc/kafka/data \
   --volume ./conf/kerberos:/etc/kerberos \
   --env KRB5_CONFIG=/etc/kerberos/krb5.conf \
   brijeshdhaker/kafka-clients:7.5.0 \
   kafkacat -F /etc/kafka/secrets/cnf/$P_CONFIG_FILE -K , -t $topic_name -P -l /etc/kafka/data/kcat_messages.txt

  #
  # Consume messages
  #


  # -K ,: pass key and value, separated by a comma
  # -e: exit successfully when last message received

  docker run --rm \
  --hostname=clients.sandbox.net \
  --network sandbox.net \
  --volume ./conf/kafka/secrets:/etc/kafka/secrets \
  --volume ./conf/kafka/data:/etc/kafka/data \
  --volume ./conf/kerberos:/etc/kerberos \
  --env KRB5_CONFIG=/etc/kerberos/krb5.conf \
  brijeshdhaker/kafka-clients:7.5.0 \
  kafkacat -F /etc/kafka/secrets/cnf/$C_CONFIG_FILE -K , -C -t $topic_name -e