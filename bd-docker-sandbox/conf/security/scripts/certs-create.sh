#!/bin/bash

# ./conf/security/scripts/certs-create.sh


#set -o nounset \
#    -o errexit \
#    -o verbose \
#    -o xtrace

# Cleanup files
rm -f conf/security/ssl/*.crt
rm -f conf/security/ssl/*.csr
rm -f conf/security/ssl/*_creds
rm -f conf/security/ssl/*.jks
rm -f conf/security/ssl/*.srl
rm -f conf/security/ssl/*.key
rm -f conf/security/ssl/*.pem
rm -f conf/security/ssl/*.der
rm -f conf/security/ssl/*.p12
rm -f conf/security/ssl/*.extfile

#
# Generate the certification authority (CA) key and certificate
#
openssl req -new \
  -x509 \
  -keyout conf/security/ssl/sandbox-ca.key \
  -out conf/security/ssl/sandbox-ca.crt \
  -days 365 \
  -subj '/CN=sandbox-ca/OU=Kafka/O=Sandbox/L=Pune/ST=MH/C=IN' \
  -passin pass:confluent \
  -passout pass:confluent

#
# convert CA cert files over to a .pem file
#
cat conf/security/ssl/sandbox-ca.crt conf/security/ssl/sandbox-ca.key > conf/security/ssl/sandbox-ca.pem

#
# Create new keys,certificates, keystore & truststore
#
for i in zookeeper zookeeper-a zookeeper-b zookeeper-b hmaster hregion kafka-broker kafka-broker-a kafka-broker-b kafka-broker-c schema-registry clients restproxy connect controlcenter clientrestproxy ksqldb
do
    echo "------------------------------- $i -------------------------------"

    # Create host keystore
    keytool -genkey -noprompt \
                 -alias $i \
                 -dname "CN=$i,OU=Kafka,O=Sandbox,L=Pune,S=MH,C=IN" \
                 -ext "SAN=dns:$i,dns:$i.sandbox.net,dns:localhost" \
                 -keystore conf/security/ssl/$i.keystore.jks \
                 -keyalg RSA \
                 -storepass confluent \
                 -keypass confluent \
                 -storetype pkcs12

    # Create the certificate signing request (CSR)
    keytool -keystore conf/security/ssl/$i.keystore.jks -alias $i -certreq -file conf/security/ssl/$i.csr -storepass confluent -keypass confluent -ext "SAN=dns:$i,dns:$i.sandbox.net,dns:localhost"
    #openssl req -in $i.csr -text -noout

cat << EOF > conf/security/ssl/$i.extfile
[req]
distinguished_name = req_distinguished_name
x509_extensions = v3_req
prompt = no
[req_distinguished_name]
CN = $i
[v3_req]
subjectAltName = @alt_names
[alt_names]
DNS.1 = $i
DNS.2 = $i.sandbox.net
DNS.3 = localhost
EOF



    # Sign the host certificate with the certificate authority (CA)
    openssl x509 -req -CA conf/security/ssl/sandbox-ca.crt -CAkey conf/security/ssl/sandbox-ca.key -in conf/security/ssl/$i.csr -out conf/security/ssl/$i-signed.crt -days 9999 -CAcreateserial -passin pass:confluent -extensions v3_req -extfile conf/security/ssl/$i.extfile

    #openssl x509 -noout -text -in conf/security/ssl/$i-signed.crt

    # Sign and import the CA cert into the keystore
    keytool -noprompt -keystore conf/security/ssl/$i.keystore.jks -alias sandbox-ca -import -file conf/security/ssl/sandbox-ca.crt -storepass confluent -keypass confluent
    #keytool -list -v -keystore conf/security/ssl/$i.keystore.jks -storepass confluent | grep "sandbox-ca"

    # Sign and import the host certificate into the keystore
    keytool -noprompt -keystore conf/security/ssl/$i.keystore.jks -alias $i -import -file conf/security/ssl/$i-signed.crt -storepass confluent -keypass confluent -ext "SAN=dns:$i,dns:$i.sandbox.net,dns:localhost"
    #keytool -list -v -keystore conf/security/ssl/$i.keystore.jks -storepass confluent

    # Create truststore and import the CA cert
    keytool -noprompt -keystore conf/security/ssl/$i.truststore.jks -alias sandbox-ca -import -file conf/security/ssl/sandbox-ca.crt -storepass confluent -keypass confluent

    # Save creds
    echo  "confluent" > conf/security/ssl/${i}-sslkey-creds
    echo  "confluent" > conf/security/ssl/${i}-keystore-creds
    echo  "confluent" > conf/security/ssl/${i}-truststore-creds

    # Create pem files and keys used for Schema Registry HTTPS testing
    #   openssl x509 -noout -modulus -in client.certificate.pem | openssl md5
    #   openssl rsa -noout -modulus -in client.key | openssl md5
    #   log "GET /" | openssl s_client -connect localhost:8081/subjects -cert client.certificate.pem -key client.key -tls1
    keytool -export -alias $i -file conf/security/ssl/$i.der -keystore conf/security/ssl/$i.keystore.jks -storepass confluent
    openssl x509 -inform der -in conf/security/ssl/$i.der -out conf/security/ssl/$i.certificate.pem
    keytool -importkeystore -srckeystore conf/security/ssl/$i.keystore.jks -destkeystore conf/security/ssl/$i.keystore.p12 -deststoretype PKCS12 -deststorepass confluent -srcstorepass confluent -noprompt
    openssl pkcs12 -in conf/security/ssl/$i.keystore.p12 -nodes -nocerts -out conf/security/ssl/$i.key -passin pass:confluent

    cacerts_path=$(bash -c "find /usr/lib/jvm -name cacerts" | tail -1)
    keytool -noprompt -destkeystore conf/security/ssl/$i.truststore.jks -importkeystore -srckeystore $cacerts_path -srcstorepass changeit -deststorepass confluent

done

# used for other/rest-proxy-security-plugin test
# https://stackoverflow.com/a/8224863
openssl pkcs12 -export -in conf/security/ssl/clientrestproxy-signed.crt -inkey conf/security/ssl/clientrestproxy.key \
               -out conf/security/ssl/clientrestproxy.p12 -name clientrestproxy \
               -CAfile conf/security/ssl/sandbox-ca.crt -caname sandbox-ca -passout pass:confluent

keytool -importkeystore \
        -deststorepass confluent -destkeypass confluent -destkeystore conf/security/ssl/kafka.restproxy.keystore.jks \
        -srckeystore conf/security/ssl/clientrestproxy.p12 -srcstoretype PKCS12 -srcstorepass confluent \
        -alias clientrestproxy