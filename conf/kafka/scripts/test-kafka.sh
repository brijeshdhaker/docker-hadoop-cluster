
#
# Verify HTTPS on Kafka Broker
#
openssl s_client -connect localhost:19093 -tls1_3 -showcerts

openssl s_client -connect kafkabroker.sandbox.net:9093 -tls1_3 -showcerts

<<comment
 # Schema Registry Test :
comment

:'
echo"This doesnt echo"
echo"Even this doesnt"
'

#
# Verify HTTPS on Schema Registry
#
openssl s_client -connect schemaregistry.sandbox.net:8081 -cert conf/kafka/secrets/clients.certificate.pem -key conf/kafka/secrets/clients.key -tls1_3 -showcerts

#
# Register a new version of a schema under the subject “Kafka-key”
#
curl -v -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' --cert conf/kafka/secrets/clients.certificate.pem --key conf/kafka/secrets/clients.key --tlsv1.2 --cacert conf/kafka/secrets/sandbox-ca.crt https://schemaregistry.sandbox.net:8081/subjects/Kafka-key/versions

#
# List all subjects
#
curl -v -X GET --cert conf/kafka/secrets/clients.certificate.pem --key conf/kafka/secrets/clients.key --tlsv1.2 --cacert conf/kafka/secrets/sandbox-ca.crt https://schemaregistry.sandbox.net:8081/subjects/

curl -X GET --cert conf/kafka/secrets/clients.certificate.pem --key conf/kafka/secrets/clients.key --tlsv1.2 --cacert conf/kafka/secrets/sandbox-ca.crt https://schemaregistry.sandbox.net:8081/subjects/