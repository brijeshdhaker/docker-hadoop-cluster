docker compose -f  docker-sandbox/docker-compose.yaml exec kafkaclient sh -c "kafkacat -V"

docker-compose -f dc-kafka.yml exec kafkabroker.sandbox.net bash

# Kafka - Broker Console
```shell

docker-compose -f dc-kafka.yml exec kafkabroker /bin/bash

```

# Topic - Creation
```shell

docker compose -f dc-kafka.yml exec kafkabroker /bin/bash

kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 1 --replication-factor 1 --topic kafka-simple-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 4 --replication-factor 1 --topic kafka-partitioned-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic kafka-avro-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic kafka-json-topic --if-not-exists

kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-text-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-json-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-avro-topic --if-not-exists


```

# Topic - List
kafka-topics --list --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-topics --list --bootstrap-server kafkabroker.sandbox.net:9092"

# Topic - Describe
kafka-topics --describe --topic kafka-simple-topic --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-topics --describe --topic kafka-simple-topic --bootstrap-server kafkabroker.sandbox.net:9092 "

# Topic - Alter
kafka-topics --alter --topic kafka-partitioned-topic --partitions 5 --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f  dc-kafka.yml exec kafkabroker sh -c "kafka-topics --alter --topic kafka-partitioned-topic --partitions 5 --bootstrap-server kafkabroker.sandbox.net:9092 "

# Topic - Delete
kafka-topics --delete --topic kafka-simple-topic --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f  dc-kafka.yml exec kafkabroker sh -c "kafka-topics --delete --topic kafka-simple-topic --bootstrap-server kafkabroker.sandbox.net:9092 "

# Topic - Check Retention period
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-configs --bootstrap-server kafkabroker.sandbox.net:9092 --entity-type topics --entity-name kafka-simple-topic --describe "
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-configs --bootstrap-server kafkabroker.sandbox.net:9092 --entity-type topics --entity-default --alter --add-config delete.retention.ms=172800000 "

### Change Kafka Retention Time
docker compose -f  dc-kafka.yml exec kafkabroker sh -c "kafka-topics --bootstrap-server kafkabroker.sandbox.net:9092 --alter --topic kafka-avro-topic --config retention.ms=1000 "
docker compose -f  dc-kafka.yml exec kafkabroker sh -c "kafka-topics --bootstrap-server localhost:9092 --topic kafka-simple-topic --create --partitions 3 --replication-factor 1 "

### Setup Default  7 days (168 hours , retention.ms= 604800000)

#
### Producer :
#
```shell

docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-console-producer --topic kafka-simple-topic --broker-list kafkabroker.sandbox.net:9092"

#### With Key
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-console-producer \
--topic kafka-simple-topic
--broker-list kafkabroker.sandbox.net:9092 \
--property parse.key=true \
--property key.separator=':' \
"
```

#
### Consumer :
#
```shell

docker compose -f  dc-kafka.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic kafka-simple-topic \
--group kafka-simple-cg \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--timeout-ms 5000 2>/dev/null"

docker compose -f  dc-kafka.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic kafka-simple-topic \
--bootstrap-server kafkabroker.sandbox.net:19092 \
--offset 600 \
--partition 0 \
--property print.key=true \
--property key.separator=' - ' \
--timeout-ms 5000 2>/dev/null"

docker compose -f  dc-kafka.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic kafka-simple-topic \
--group kafka-simple-cg \
--bootstrap-server kafkabroker.sandbox.net:19092 \
--from-beginning \
--property print.key=true \
--property key.separator=\",\" \
--timeout-ms 5000 2>/dev/null"

```
docker system prune -a --volumes --filter "label=io.confluent.docker"

# Application Setup
docker-compose up -d
docker-compose exec broker kafka-topics --create \
--topic users-topic-avro \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--partitions 1 \
--replication-factor 1
--if-not-exists


# To check the end offset set parameter time to value -1
kafka-run-class kafka.tools.GetOffsetShell \
--broker-list localhost:9092 \
--topic users-topic-avro \
--time -1

# To check the start offset, use --time -2
kafka-run-class kafka.tools.GetOffsetShell \
--broker-list localhost:9092 \
--topic users-topic-avro \
--time -2


### Get Detail Info about Your Consumer Group –
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --list"
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --describe --group kafka-simple-cg "

#### Delete Offset
docker-compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --delete --group kafka-simple-cg "

#### Reset Offset
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --reset-offsets --to-earliest --all-topics --execute --group kafka-simple-cg "

##### --shift-by :- Reset the offset by incrementing the current offset position by take both +ve or -ve number
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --shift-by 10 --topic sales_topic --execute "

##### --to-datetime :- Reset offsets to offset from datetime. Format: ‘YYYY-MM-DDTHH:mm:SS.sss’
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --to-datetime 2020-11-01T00:00:00Z --topic sales_topic --execute "

##### --to-earliest :- Reset offsets to earliest (oldest) offset available in the topic.
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --to-earliest --topic sales_topic --execute "

##### --to-latest :- Reset offsets to latest (recent) offset available in the topic.
docker compose -f dc-kafka.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --to-latest --topic taxi-rides --execute "

### View Only 10  Messages on the Terminal –

```shell
#!/bin/bash
echo "Enter name of topic to empty:"
read topicName
kafka-configs --zookeeper zookeeper.sandbox.net:2181 --alter --entity-type topics --entity-name $topicName --add-config retention.ms=1000
sleep 5
kafka-configs --zookeeper zookeeper.sandbox.net:2181 --alter --entity-type topics --entity-name $topicName --delete-config retention.ms
```

#
##  schemaregistry
#
docker compose -f dc-kafka.yml exec schemaregistry /bin/bash


kafka-avro-console-producer --topic users-topic-avro \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--property value.schema="$(< /opt/app/schema/user.avsc)"

# Register a new version of a schema under the subject "Kafka-key"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry:8081/subjects/auditlog-topic-avro-value/versions

# Register a new version of a schema under the subject "Kafka-value"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry:8081/subjects/auditlog-topic-avro-value/versions

# List all subjects
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry:8081/subjects

# List all schema versions registered under the subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry:8081/subjects/auditlog-topic-avro-value/versions

# Fetch a schema by globally unique id 1
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry:8081/schemas/ids/1

# Fetch version 1 of the schema registered under subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry:8081/subjects/auditlog-topic-avro-value/versions/1

# Fetch the most recently registered schema under subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry:8081/subjects/auditlog-topic-avro-value/versions/latest

# Check whether a schema has been registered under subject "Kafka-key"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry:8081/subjects/Kafka-key

# Test compatibility of a schema with the latest schema under subject "Kafka-value"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry:8081/compatibility/subjects/auditlog-topic-avro-value/versions/latest

# Get top level config
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry:8081/config

# Update compatibility requirements globally
$ curl -X PUT -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"compatibility": "NONE"}' \
http://schemaregistry:8081/config

# Update compatibility requirements under the subject "Kafka-value"
$ curl -X PUT -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"compatibility": "BACKWARD"}' \
http://schemaregistry:8081/config

#
# Avro Producer & Consumer
#
kafka-avro-console-consumer --topic users \
--bootstrap-server kafkabroker.sandbox.net:9092

kafka-avro-console-consumer --topic users \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--property schema.registry.url=http://schemaregistry:8081 \
--from-beginning

http://schemaregistry:8081/subjects
["users-value","order-updated-value","order-created-value"]

http://schemaregistry:8081/schemas/types

http://schemaregistry:8081/subjects/order-updated-value/versions
http://schemaregistry:8081/schemas/ids/1

http://schemaregistry:8081/subjects?deleted=true



docker-compose exec connect sh -c "curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_pageviews_cos.config"
docker-compose exec connect sh -c "curl -X POST -H 'Content-Type: application/json' --data @connector_pageviews_cos.config http://localhost:8083/connectors"

docker-compose exec connect sh -c "curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_users_cos.config"
docker-compose exec connect sh -c "curl -X POST -H 'Content-Type: application/json' --data @connector_users_cos.config http://localhost:8083/connectors"

docker container stop $(docker container ls -a -q -f "label=io.confluent.docker")
docker container stop $(docker container ls -a -q -f "label=io.confluent.docker") && docker system prune -a -f --volumes


./kafka-avro-console-producer --broker-list localhost:9093 --topic myTopic \
--producer.config ~/ect/kafka/producer.properties --property value.schema=‘{“type”:“record”,“name”:“myrecord”,“fields”:[{“name”:“f1”,“type”:“string”}]}’ \
--property schema.registry.url=https://localhost:8081 --property schema.registry.ssl.truststore.location=/etc/kafka/security/schema.registry.client.truststore.jks --property schema.registry.ssl.truststore.password=myTrustStorePassword

# List Topics

docker run --rm \
--network sandbox.net \
confluentinc/cp-kcat \
kcat -b kafkabroker.sandbox.net:19092 -L -J | jq .

Or 

docker run -it \
--network sandbox.net \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19092 -L -J | jq .


docker run --tty --rm \
--network sandbox.net \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:9093 -d all -L \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET'


#
# Producer
#
docker run -it --rm \
--network sandbox.net \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:9093 \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-t test_topic \
-P -l /etc/kafka/secrets/data/messages.txt

#
#
#
docker run -it --rm \
--network sandbox.net \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19092 -C \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-K: -f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\n\Partition: %p\tOffset: %o\n--\n' \
-t test_topic



#
# List Topics Using SASL_SSL
#
docker run -it --rm \
--network sandbox.net \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19093 -L -J \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-X 'ssl.key.location=/etc/kafka/secrets/clients.key' \
-X 'ssl.key.password=confluent' \
-X 'ssl.certificate.location=/etc/kafka/secrets/clients-signed.crt' \
-X 'ssl.ca.location=/etc/kafka/secrets/sandbox-ca.pem'

#
# Producer Using SASL_SSL
#
docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19093 -P -t test_topic \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-X 'ssl.key.location=/etc/kafka/secrets/clients.key' \
-X 'ssl.key.password=confluent' \
-X 'ssl.certificate.location=/etc/kafka/secrets/clients-signed.crt' \
-X 'ssl.ca.location=/etc/kafka/secrets/sandbox-ca.pem' \
-l /etc/kafka/secrets/data/messages.txt

# Producing messages inline from a script
docker run --interactive \
--network docker-compose_default \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafka:29092 \
-t test \
-K: \
-P <<EOF
1:FOO
2:BAR
EOF

#
# Consumer Using SASL_SSL
#
docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19093 -C -t test_topic -o beginning \
-f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n' -e \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-X 'ssl.key.location=/etc/kafka/secrets/clients.key' \
-X 'ssl.key.password=confluent' \
-X 'ssl.certificate.location=/etc/kafka/secrets/clients-signed.crt' \
-X 'ssl.ca.location=/etc/kafka/secrets/sandbox-ca.pem'


docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume ./conf/kafka/data:/etc/kafka/data \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /etc/kafka/secrets/cnf/librdkafka.config -C -t test_topic -o -10 \
-f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n'


docker run --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume ./conf/kafka/data:/etc/kafka/data \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
--env KAFKA_OPTS="-Djava.security.auth.login.config=/etc/kafka/secrets/jaas/kafkaclients_jaas.conf -Djava.security.krb5.conf=/etc/krb5.conf -Dsun.security.krb5.debug=false" \
confluentinc/cp-server:7.5.0 \
sh -c "kafka-console-producer --topic kcat-test-topic \
--broker-list kafkabroker.sandbox.net:19093 \
--producer.config /apps/sandbox/kafka/cnf/client-ssl.properties \
--property parse.key=true \
--property key.separator=, < /etc/kafka/secrets/data/kcat_messages.txt"


docker compose exec kafkabroker sh -c "kafka-console-producer --topic kcat-test-topic \
--broker-list kafkabroker.sandbox.net:19093 \
--producer.config /apps/sandbox/kafka/cnf/client-ssl.properties \
--property parse.key=true \
--property key.separator=, < /etc/kafka/secrets/kcat_messages.txt 2>/dev/null"


# Consume messages
```shell
echo -e "\n# Consume messages from $topic_name"

docker compose exec kafkabroker sh -c "kafka-console-consumer \
--topic kcat-test-topic \
--bootstrap-server kafkabroker.sandbox.net:19093 \
--consumer.config /etc/kafka/secrets/cnf/client-ssl.properties \
--from-beginning \
--property print.key=true \
--property key.separator=\",\" \
--timeout-ms 5000 2>/dev/null"
```

docker run --rm \
confluentinc/cp-server:7.5.0 sh -c "/bin/kafka-storage random-uuid"

zookeeper-shell zookeeper:2181 ls /