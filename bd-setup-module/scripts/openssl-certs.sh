#!/bin/bash

# ./conf/kafka/secrets/openssl-certs.sh

# Cleanup files
rm -f /apps/security/ssl/*.crt
rm -f /apps/security/ssl/*.csr
rm -f /apps/security/ssl/*_creds
rm -f /apps/security/ssl/*.jks
rm -f /apps/security/ssl/*.srl
rm -f /apps/security/ssl/*.key
rm -f /apps/security/ssl/*.pem
rm -f /apps/security/ssl/*.der
rm -f /apps/security/ssl/*.p12

#
# create the certification authority key and certificate
#
openssl req -new -nodes \
   -x509 \
   -days 3650 \
   -newkey rsa:2048 \
   -keyout /apps/security/ssl/sandbox-ca.key \
   -out /apps/security/ssl/sandbox-ca.crt \
   -config /apps/security/ssl/sandbox-ca.cnf

#
# convert ca cert files over to a .pem file
#
cat /apps/security/ssl/sandbox-ca.crt /apps/security/ssl/sandbox-ca.key > /apps/security/ssl/sandbox-ca.pem

#
# Create truststore and import the CA cert.
#
keytool -import -noprompt -trustcacerts -alias sandbox-ca -file /apps/security/ssl/sandbox-ca.crt -keystore /apps/security/ssl/ca_truststore.jks -storepass confluent

## Convert JKS to PKcs12 format
keytool -importkeystore \
        -srckeystore /apps/security/ssl/ca_truststore.jks \
        -destkeystore /apps/security/ssl/ca_truststore.pkcs12 \
        -deststoretype pkcs12

## verify jks trust store
keytool -list -keystore "/apps/security/ssl/ca_truststore.jks" -storepass confluent | grep "sandbox-ca"

#
# creating a new key and certificate that is valid for 3650 days, uses the rsa:2048 encryption,
#
openssl req -new -nodes \
    -days 3650 \
    -newkey rsa:2048 \
    -keyout /apps/security/ssl/kafkabroker.key \
    -out /apps/security/ssl/kafkabroker.csr \
    -config /apps/security/ssl/kafkabroker.cnf


#
# Sign the host certificate with the certificate authority (CA)
#
openssl x509 -req \
    -days 3650 \
    -in /apps/security/ssl/kafkabroker.csr \
    -CA /apps/security/ssl/sandbox-ca.crt \
    -CAkey /apps/security/ssl/sandbox-ca.key \
    -CAcreateserial \
    -out /apps/security/ssl/kafkabroker.crt \
    -extfile /apps/security/ssl/kafkabroker.cnf \
    -extensions v3_req

#
# convert the server certificate over to the pkcs12 format
#
openssl pkcs12 -export \
    -in /apps/security/ssl/kafkabroker.crt \
    -inkey /apps/security/ssl/kafkabroker.key \
    -chain \
    -CAfile /apps/security/ssl/sandbox-ca.pem \
    -name kafkabroker \
    -out /apps/security/ssl/kafkabroker.p12 \
    -password pass:confluent


#
# create the kafkabroker PKCS12 keystore and import the certificate:
#
keytool -importkeystore \
    -noprompt \
    -srckeystore /apps/security/ssl/kafkabroker.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass confluent \
    -destkeystore /apps/security/ssl/kafkabroker_keystore.pkcs12 \
    -deststoretype PKCS12  \
    -deststorepass confluent

#
# Verify the kafkabroker PKCS12 keystore
#
keytool -list -v \
    -keystore /apps/security/ssl/kafkabroker_keystore.pkcs12 \
    -storepass confluent

#
# create the kafkabroker JKS keystore and import the certificate:
#
keytool -importkeystore \
    -noprompt \
    -srckeystore /apps/security/ssl/kafkabroker.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass confluent \
    -destkeystore /apps/security/ssl/kafkabroker_keystore.jks \
    -deststorepass confluent

## Convert PKCS12 to JKS format
keytool -importkeystore \
        -noprompt \
        -srckeystore /apps/security/ssl/kafkabroker_keystore.pkcs12 \
        -srcstoretype PKCS12 \
        -srcstorepass confluent \
        -destkeystore /apps/security/ssl/kafkabroker_keystore.jks \
        -deststoretype JKS \
        -deststorepass confluent

#
# Verify the kafkabroker JKS keystore
#
keytool -list -v \
    -keystore /apps/security/ssl/kafkabroker_keystore.jks \
    -storepass confluent

#
# save the credentials
#

##
tee /apps/security/ssl/ca_truststore_creds << EOF >/dev/null
confluent
EOF

tee /apps/security/ssl/kafkabroker_sslkey_creds << EOF >/dev/null
confluent
EOF

tee /apps/security/ssl/kafkabroker_keystore_creds << EOF >/dev/null
confluent
EOF

