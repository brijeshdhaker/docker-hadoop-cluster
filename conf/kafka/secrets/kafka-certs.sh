#!/bin/bash

# ./conf/kafka/secrets/kafka-certs.sh

# Cleanup files
rm -f conf/kafka/secrets/*.crt
rm -f conf/kafka/secrets/*.csr
rm -f conf/kafka/secrets/*_creds
rm -f conf/kafka/secrets/*.jks
rm -f conf/kafka/secrets/*.srl
rm -f conf/kafka/secrets/*.key
rm -f conf/kafka/secrets/*.pem
rm -f conf/kafka/secrets/*.der
rm -f conf/kafka/secrets/*.p12

#
# create the certification authority key and certificate
#
openssl req -new -nodes \
   -x509 \
   -days 3650 \
   -newkey rsa:2048 \
   -keyout conf/kafka/secrets/sandbox-ca.key \
   -out conf/kafka/secrets/sandbox-ca.crt \
   -config conf/kafka/secrets/sandbox-ca.cnf

#
# convert ca cert files over to a .pem file
#
cat conf/kafka/secrets/sandbox-ca.crt conf/kafka/secrets/sandbox-ca.key > conf/kafka/secrets/sandbox-ca.pem

#
# Create truststore and import the CA cert.
#
keytool -import -noprompt -trustcacerts -alias sandbox-ca -file conf/kafka/secrets/sandbox-ca.crt -keystore conf/kafka/secrets/ca_truststore.jks -storepass confluent

## Convert JKS to PKcs12 format
keytool -importkeystore \
        -srckeystore conf/kafka/secrets/ca_truststore.jks \
        -destkeystore conf/kafka/secrets/ca_truststore.pkcs12 \
        -deststoretype pkcs12

## verify jks trust store
keytool -list -keystore "conf/kafka/secrets/ca_truststore.jks" -storepass confluent | grep "sandbox-ca"

#
# creating a new key and certificate that is valid for 3650 days, uses the rsa:2048 encryption,
#
openssl req -new -nodes \
    -days 3650 \
    -newkey rsa:2048 \
    -keyout conf/kafka/secrets/kafkabroker.key \
    -out conf/kafka/secrets/kafkabroker.csr \
    -config conf/kafka/secrets/kafkabroker.cnf


#
# Sign the host certificate with the certificate authority (CA)
#
openssl x509 -req \
    -days 3650 \
    -in conf/kafka/secrets/kafkabroker.csr \
    -CA conf/kafka/secrets/sandbox-ca.crt \
    -CAkey conf/kafka/secrets/sandbox-ca.key \
    -CAcreateserial \
    -out conf/kafka/secrets/kafkabroker.crt \
    -extfile conf/kafka/secrets/kafkabroker.cnf \
    -extensions v3_req

#
# convert the server certificate over to the pkcs12 format
#
openssl pkcs12 -export \
    -in conf/kafka/secrets/kafkabroker.crt \
    -inkey conf/kafka/secrets/kafkabroker.key \
    -chain \
    -CAfile conf/kafka/secrets/sandbox-ca.pem \
    -name kafkabroker \
    -out conf/kafka/secrets/kafkabroker.p12 \
    -password pass:confluent


#
# create the kafkabroker PKCS12 keystore and import the certificate:
#
keytool -importkeystore \
    -noprompt \
    -srckeystore conf/kafka/secrets/kafkabroker.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass confluent \
    -destkeystore conf/kafka/secrets/kafkabroker_keystore.pkcs12 \
    -deststoretype PKCS12  \
    -deststorepass confluent

#
# Verify the kafkabroker PKCS12 keystore
#
keytool -list -v \
    -keystore conf/kafka/secrets/kafkabroker_keystore.pkcs12 \
    -storepass confluent

#
# create the kafkabroker JKS keystore and import the certificate:
#
keytool -importkeystore \
    -noprompt \
    -srckeystore conf/kafka/secrets/kafkabroker.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass confluent \
    -destkeystore conf/kafka/secrets/kafkabroker_keystore.jks \
    -deststorepass confluent

## Convert PKCS12 to JKS format
keytool -importkeystore \
        -noprompt \
        -srckeystore conf/kafka/secrets/kafkabroker_keystore.pkcs12 \
        -srcstoretype PKCS12 \
        -srcstorepass confluent \
        -destkeystore conf/kafka/secrets/kafkabroker_keystore.jks \
        -deststoretype JKS \
        -deststorepass confluent

#
# Verify the kafkabroker JKS keystore
#
keytool -list -v \
    -keystore conf/kafka/secrets/kafkabroker_keystore.jks \
    -storepass confluent

#
# save the credentials
#

##
tee conf/kafka/secrets/ca_truststore_creds << EOF >/dev/null
confluent
EOF

tee conf/kafka/secrets/kafkabroker_sslkey_creds << EOF >/dev/null
confluent
EOF

tee conf/kafka/secrets/kafkabroker_keystore_creds << EOF >/dev/null
confluent
EOF

