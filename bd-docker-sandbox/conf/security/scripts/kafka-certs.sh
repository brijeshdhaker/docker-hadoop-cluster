#!/bin/bash

# ./conf/kafka/secrets/kafka-certs.sh

# Cleanup files
rm -f conf/secrets/ssl/*.crt
rm -f conf/secrets/ssl/*.csr
rm -f conf/secrets/ssl/*_creds
rm -f conf/secrets/ssl/*.jks
rm -f conf/secrets/ssl/*.srl
rm -f conf/secrets/ssl/*.key
rm -f conf/secrets/ssl/*.pem
rm -f conf/secrets/ssl/*.der
rm -f conf/secrets/ssl/*.p12

#
# create the certification authority key and certificate
#
openssl req -new -nodes \
   -x509 \
   -days 3650 \
   -newkey rsa:2048 \
   -keyout conf/secrets/ssl/sandbox-ca.key \
   -out conf/secrets/ssl/sandbox-ca.crt \
   -config conf/secrets/ssl/sandbox-ca.cnf

#
# convert ca cert files over to a .pem file
#
cat conf/secrets/ssl/sandbox-ca.crt conf/secrets/ssl/sandbox-ca.key > conf/secrets/ssl/sandbox-ca.pem

#
# Create truststore and import the CA cert.
#
keytool -import -noprompt -trustcacerts -alias sandbox-ca -file conf/secrets/ssl/sandbox-ca.crt -keystore conf/secrets/ssl/ca_truststore.jks -storepass confluent

## Convert JKS to PKcs12 format
keytool -importkeystore \
        -srckeystore conf/secrets/ssl/ca_truststore.jks \
        -destkeystore conf/secrets/ssl/ca_truststore.pkcs12 \
        -deststoretype pkcs12

## verify jks trust store
keytool -list -keystore "conf/secrets/ssl/ca_truststore.jks" -storepass confluent | grep "sandbox-ca"

#
# creating a new key and certificate that is valid for 3650 days, uses the rsa:2048 encryption,
#
openssl req -new -nodes \
    -days 3650 \
    -newkey rsa:2048 \
    -keyout conf/secrets/ssl/kafka-broker.key \
    -out conf/secrets/ssl/kafka-broker.csr \
    -config conf/secrets/ssl/kafka-broker.cnf


#
# Sign the host certificate with the certificate authority (CA)
#
openssl x509 -req \
    -days 3650 \
    -in conf/secrets/ssl/kafka-broker.csr \
    -CA conf/secrets/ssl/sandbox-ca.crt \
    -CAkey conf/secrets/ssl/sandbox-ca.key \
    -CAcreateserial \
    -out conf/secrets/ssl/kafka-broker.crt \
    -extfile conf/secrets/ssl/kafka-broker.cnf \
    -extensions v3_req

#
# convert the server certificate over to the pkcs12 format
#
openssl pkcs12 -export \
    -in conf/secrets/ssl/kafka-broker.crt \
    -inkey conf/secrets/ssl/kafka-broker.key \
    -chain \
    -CAfile conf/secrets/ssl/sandbox-ca.pem \
    -name kafka-broker \
    -out conf/secrets/ssl/kafka-broker.p12 \
    -password pass:confluent


#
# create the kafka-broker PKCS12 keystore and import the certificate:
#
keytool -importkeystore \
    -noprompt \
    -srckeystore conf/secrets/ssl/kafka-broker.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass confluent \
    -destkeystore conf/secrets/ssl/kafka-broker_keystore.pkcs12 \
    -deststoretype PKCS12  \
    -deststorepass confluent

#
# Verify the kafka-broker PKCS12 keystore
#
keytool -list -v \
    -keystore conf/secrets/ssl/kafka-broker_keystore.pkcs12 \
    -storepass confluent

#
# create the kafka-broker JKS keystore and import the certificate:
#
keytool -importkeystore \
    -noprompt \
    -srckeystore conf/secrets/ssl/kafka-broker.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass confluent \
    -destkeystore conf/secrets/ssl/kafka-broker_keystore.jks \
    -deststorepass confluent

## Convert PKCS12 to JKS format
keytool -importkeystore \
        -noprompt \
        -srckeystore conf/secrets/ssl/kafka-broker_keystore.pkcs12 \
        -srcstoretype PKCS12 \
        -srcstorepass confluent \
        -destkeystore conf/secrets/ssl/kafka-broker_keystore.jks \
        -deststoretype JKS \
        -deststorepass confluent

#
# Verify the kafka-broker JKS keystore
#
keytool -list -v \
    -keystore conf/secrets/ssl/kafka-broker_keystore.jks \
    -storepass confluent

#
# save the credentials
#

##
tee conf/secrets/ssl/ca_truststore_creds << EOF >/dev/null
confluent
EOF

tee conf/secrets/ssl/kafka-broker_sslkey_creds << EOF >/dev/null
confluent
EOF

tee conf/secrets/ssl/kafka-broker_keystore_creds << EOF >/dev/null
confluent
EOF

