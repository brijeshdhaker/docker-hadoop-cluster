#!/bin/bash

set -o nounset \
    -o errexit \
    -o verbose \
    -o xtrace

# Generate CA key
openssl req -new -x509 -keyout sandbox-ca.key -out sandbox-ca.crt -days 3650 -subj '/CN=ca.sandbox.net/OU=TEST/O=CONFLUENT/L=Pune/S=MH/C=IN' -passin pass:confluent -passout pass:confluent

# Kafkacat
openssl genrsa -des3 -passout "pass:confluent" -out kafkacat.client.key 1024
openssl req -passin "pass:confluent" -passout "pass:confluent" -key kafkacat.client.key -new -out kafkacat.client.req -subj '/CN=ca.sandbox.net/OU=TEST/O=CONFLUENT/L=Pune/S=MH/C=IN'
openssl x509 -req -CA sandbox-ca.crt -CAkey sandbox-ca.key -in kafkacat.client.req -out kafkacat-ca1-signed.pem -days 9999 -CAcreateserial -passin "pass:confluent"


for i in zookeeper kafkabroker schemaregistry producer consumer
do
	echo $i
	# Create keystores
	keytool -genkey -noprompt \
				 -alias $i \
				 -dname "CN=$i.sandbox.net, OU=TEST, O=CONFLUENT, L=Pune, S=MH, C=IN" \
				 -keystore $i.keystore.jks \
				 -keyalg RSA \
				 -storepass confluent \
				 -keypass confluent

	# Create CSR, sign the key and import back into keystore
	keytool -keystore $i.keystore.jks -alias $i -certreq -file $i.csr -storepass confluent -keypass confluent

	openssl x509 -req -CA sandbox-ca.crt -CAkey sandbox-ca.key -in $i.csr -out $i-ca1-signed.crt -days 9999 -CAcreateserial -passin pass:confluent

	keytool -keystore $i.keystore.jks -alias CARoot -import -file sandbox-ca.crt -storepass confluent -keypass confluent

	keytool -keystore $i.keystore.jks -alias $i -import -file $i-ca1-signed.crt -storepass confluent -keypass confluent

	# Create truststore and import the CA cert.
	keytool -keystore $i.truststore.jks -alias CARoot -import -file sandbox-ca.crt -storepass confluent -keypass confluent

  echo "confluent" > ${i}_sslkey_creds
  echo "confluent" > ${i}_keystore_creds
  echo "confluent" > ${i}_truststore_creds
done

## Verify Certificate
keytool -list -v \
    -keystore /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/conf/kafka/kafkabroker.keystore.jks \
    -storepass confluent

## Convert JKS to PKcs12 format
keytool -importkeystore -srckeystore /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/conf/kafka/kafkabroker.keystore.jks -destkeystore /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/conf/kafka/kafkabroker.keystore.pkcs12 -deststoretype pkcs12


keytool -list -v \
    -keystore /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/conf/kafka/kafkabroker.keystore.pkcs12 \
    -storepass confluent