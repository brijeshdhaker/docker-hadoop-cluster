
#
# Verify HTTPS on Kafka Broker
#
openssl s_client -connect localhost:19093 -tls1_3 -showcerts

openssl s_client -connect kafka-broker.sandbox.net:19093 -tls1_3 -showcerts

<<comment
 # Schema Registry Test :
comment

:'
echo"This doesnt echo"
echo"Even this doesnt"
'

#
# Zookeeper Testing
#
zookeeper-shell zookeeper.sandbox.net:2182 -zk-tls-config-file /etc/zookeeper/secrets/cnf/zookeeper-client.config ls /brokers/ids


openssl s_client -connect zookeeper.sandbox.net:28080 -tls1_3 -showcerts -CAfile /etc/zookeeper/secrets/sandbox_ca.crt -cert /etc/zookeeper/secrets/clients.certificate.pem -key /etc/zookeeper/secrets/clients.key

#
# Verify HTTPS on Schema Registry
#
openssl s_client -connect schema-registry.sandbox.net:8081 -cert /apps/security/ssl/clients.certificate.pem -key /apps/security/ssl/clients.key -tls1_3 -showcerts

#
# Register a new version of a schema under the subject “Kafka-key”
#
curl -v -X POST -H "Content-Type: application/vnd.schema-registry.v1+json" --data '{"schema": "{\"type\": \"string\"}"}' --cert /apps/security/ssl/clients.certificate.pem --key /apps/security/ssl/clients.key --tlsv1.2 --cacert /apps/security/ssl/sandbox_ca.crt https://schema-registry.sandbox.net:8081/subjects/Kafka-key/versions

#
# List all subjects
#
curl -v -X GET --cert /apps/security/ssl/clients.certificate.pem \
--key /apps/security/ssl/clients.key \
--tlsv1.2 \
--cacert /apps/security/ssl/sandbox-ca.crt \
https://schema-registry.sandbox.net:8081/subjects/

curl -X GET --cert /apps/security/ssl/clients.certificate.pem \
--key /apps/security/ssl/clients.key \
--tlsv1.2 --cacert /apps/security/ssl/sandbox-ca.crt \
https://schema-registry.sandbox.net:8081/subjects/