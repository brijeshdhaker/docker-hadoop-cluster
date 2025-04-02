#!/bin/bash



#set -o nounset \
#    -o errexit \
#    -o verbose \
#    -o xtrace

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
rm -f /apps/security/ssl/*.extfile

#
# Generate the certification authority (CA) key and certificate
#
openssl req -new \
  -x509 \
  -keyout /apps/security/ssl/sandbox-ca.key \
  -out /apps/security/ssl/sandbox-ca.crt \
  -days 7300 \
  -subj '/CN=sandbox-ca/OU=Kafka/O=Sandbox/L=Pune/ST=MH/C=IN' \
  -passin pass:confluent \
  -passout pass:confluent

#
# convert CA cert files over to a .pem file
#
cat /apps/security/ssl/sandbox-ca.crt /apps/security/ssl/sandbox-ca.key > /apps/security/ssl/sandbox-ca.pem

#
# Create new keys,certificates, keystore & truststore
#
for i in zookeeper zookeeper-a zookeeper-b zookeeper-b hmaster hregion kafkabroker kafkabroker-a kafkabroker-b kafkabroker-c schemaregistry clients restproxy connect controlcenter clientrestproxy ksqldb
do
    echo "------------------------------- $i -------------------------------"

    # Create host keystore
    keytool -genkey \
      -noprompt \
      -alias $i \
      -dname "CN=$i,OU=Kafka,O=Sandbox,L=Pune,S=MH,C=IN" \
      -ext "SAN=dns:$i,dns:$i.sandbox.net,dns:localhost" \
      -keystore /apps/security/ssl/$i.keystore.jks \
      -keyalg RSA \
      -storepass confluent \
      -keypass confluent \
      -storetype pkcs12

    # Create the certificate signing request (CSR)
    keytool -certreq \
      -keystore /apps/security/ssl/$i.keystore.jks \
      -alias $i \
      -file /apps/security/ssl/$i.csr \
      -storepass confluent \
      -keypass confluent \
      -ext "SAN=dns:$i,dns:$i.sandbox.net,dns:localhost"

    #openssl req -in $i.csr -text -noout

cat << EOF > /apps/security/ssl/$i.extfile
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
IP.1  = 127.0.0.1
IP.2  = 192.168.*.*
EOF



    # Sign the host certificate with the certificate authority (CA)
    openssl x509 -req \
      -CA /apps/security/ssl/sandbox-ca.crt \
      -CAkey /apps/security/ssl/sandbox-ca.key \
      -in /apps/security/ssl/$i.csr \
      -out /apps/security/ssl/$i-signed.crt \
      -days 9999 \
      -CAcreateserial \
      -passin pass:confluent \
      -extensions v3_req \
      -extfile /apps/security/ssl/$i.extfile

    #openssl x509 -noout -text -in /apps/security/ssl/$i-signed.crt

    # Import the CA Root cert into the keystore
    keytool -import\
      -noprompt \
      -keystore /apps/security/ssl/$i.keystore.jks \
      -alias sandbox-ca  \
      -file /apps/security/ssl/sandbox-ca.crt \
      -storepass confluent \
      -keypass confluent

    #keytool -list -v -keystore /apps/security/ssl/$i.keystore.jks -storepass confluent | grep "sandbox-ca"

    # Import the signed host certificate into the keystore
    keytool -noprompt \
    -keystore /apps/security/ssl/$i.keystore.jks \
    -alias $i -import \
    -file /apps/security/ssl/$i-signed.crt \
    -storepass confluent \
    -keypass confluent \
    -ext "SAN=dns:$i,dns:$i.sandbox.net,dns:localhost"

    #keytool -list -v -keystore /apps/security/ssl/$i.keystore.jks -storepass confluent

    # Create truststore and import the CA cert
    keytool -noprompt \
    -keystore /apps/security/ssl/$i.truststore.jks \
    -alias sandbox-ca -import \
    -file /apps/security/ssl/sandbox-ca.crt \
    -storepass confluent \
    -keypass confluent

    # Save creds
    echo  "confluent" > /apps/security/ssl/${i}-sslkey-creds
    echo  "confluent" > /apps/security/ssl/${i}-keystore-creds
    echo  "confluent" > /apps/security/ssl/${i}-truststore-creds

    # Create pem files and keys used for Schema Registry HTTPS testing
    #   openssl x509 -noout -modulus -in client.certificate.pem | openssl md5
    #   openssl rsa -noout -modulus -in client.key | openssl md5
    #   log "GET /" | openssl s_client -connect localhost:8081/subjects -cert client.certificate.pem -key client.key -tls1
    keytool -export \
      -alias $i \
      -file /apps/security/ssl/$i.der \
      -keystore /apps/security/ssl/$i.keystore.jks \
      -storepass confluent

    openssl x509 -inform der \
      -in /apps/security/ssl/$i.der \
      -out /apps/security/ssl/$i.certificate.pem

    keytool -importkeystore -noprompt \
      -srckeystore /apps/security/ssl/$i.keystore.jks \
      -destkeystore /apps/security/ssl/$i.keystore.p12 \
      -deststoretype PKCS12 \
      -deststorepass confluent \
      -srcstorepass confluent

    openssl pkcs12 \
      -nodes \
      -nocerts \
      -passin pass:confluent \
      -in /apps/security/ssl/$i.keystore.p12 \
      -out /apps/security/ssl/$i.key


    cacerts_path=$(bash -c "find /usr/lib/jvm -name cacerts" | tail -1)

    keytool -noprompt -importkeystore \
      -srcstorepass changeit \
      -srckeystore $cacerts_path \
      -destkeystore /apps/security/ssl/$i.truststore.jks \
      -deststorepass confluent

    #

done

# used for other/rest-proxy-security-plugin test
# https://stackoverflow.com/a/8224863
openssl pkcs12 -export \
  -in /apps/security/ssl/clientrestproxy-signed.crt \
  -inkey /apps/security/ssl/clientrestproxy.key \
  -out /apps/security/ssl/clientrestproxy.p12 \
  -name clientrestproxy \
  -CAfile /apps/security/ssl/sandbox-ca.crt \
  -caname sandbox-ca \
  -passout pass:confluent


keytool -importkeystore \
  -alias clientrestproxy \
  -srcstorepass confluent \
  -srcstoretype PKCS12 \
  -srckeystore /apps/security/ssl/clientrestproxy.p12 \
  -deststorepass confluent \
  -destkeypass confluent \
  -destkeystore /apps/security/ssl/kafka.restproxy.keystore.jks



