#!/bin/bash

# conf/kafka/scripts/certs-verify.sh

set -o nounset \
    -o errexit \
    -o verbose

# See what is in each keystore and truststore
for i in zookeeper zookeeper_a zookeeper_b zookeeper_c kafka-broker kafka-broker_a kafka-broker_b kafka-broker_c schema-registry clients restproxy connect controlcenter clientrestproxy ksqldb
do
        echo " "
        echo "------------------------------- $i keystore -------------------------------"
	      keytool -list -v -keystore conf/secrets/ssl/$i.keystore.jks -storepass confluent | grep -e Alias -e Entry

        echo " "
        echo "------------------------------- $i truststore -------------------------------"
	      keytool -list -v -keystore conf/secrets/ssl/$i.truststore.jks -storepass confluent | grep -e Alias -e Entry
done