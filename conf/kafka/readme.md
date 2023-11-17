
docker compose -f docker-compose.yml exec -it kafkabroker bash

# Topic - Creation

docker compose -f docker-compose.yml exec -T kafkabroker echo $HOSTNAME

docker compose exec kafkabroker sh -c "kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:19091 --partitions 1 --replication-factor 1 --topic kafka-python-simple-topic --if-not-exists"

docker compose exec kafkabroker sh -c "kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:19091 --partitions 4 --replication-factor 1 --topic kafka-python-partitioned-topic"

# Topic - List
docker compose exec kafkabroker sh -c "kafka-topics --list --bootstrap-server kafkabroker.sandbox.net:19091"

# Topic - Describe
docker compose exec kafkabroker sh -c "kafka-topics --describe --topic kafka-python-simple-topic --bootstrap-server kafkabroker.sandbox.net:19091"

# Topic - Alter
docker compose exec kafkabroker sh -c "kafka-topics --alter --topic kafka-python-partitioned-topic --partitions 5 --bootstrap-server kafkabroker.sandbox.net:19091"

# Topic - Delete
docker compose exec kafkabroker sh -c "kafka-topics --delete --topic kafka-python-simple-topic --bootstrap-server kafkabroker.sandbox.net:19091"

# Topic - Check current Retention period
docker compose exec kafkabroker sh -c "kafka-configs –zookeeper zookeeper.sandbox.net:2181 –describe –entity-type topics –entity-name <topic name>"


# Kafka - Broker Console
docker-compose -f docker-compose.yml exec kafkabroker bash

### Producer :
docker-compose -f docker-compose.yml exec kafkabroker bash

kafka-console-producer --topic kafka-python-simple-topic --broker-list kafkabroker.sandbox.net:19091

#### With Key
kafka-console-producer \
--topic kafka-python-simple-topic
--broker-list kafkabroker.sandbox.net:19091 \
--property parse.key=true \
--property key.separator=":" \


### Consumer :
kafka-console-consumer \
--topic kafka-python-simple-topic \
--group test-cg \
--bootstrap-server kafkabroker.sandbox.net:19091

#### With Key
kafka-console-consumer \
--topic kafka-python-simple-topic \
--group test-cg \
--bootstrap-server kafkabroker.sandbox.net:19091 \
--from-beginning \
--property print.key=true \
--property key.separator="-"


docker system prune -a --volumes --filter "label=io.confluent.docker"

# Application Setup
docker-compose up -d
docker-compose exec broker kafka-topics --create \
--topic users-topic-avro \
--bootstrap-server kafkabroker.sandbox.net:19091 \
--partitions 1 \
--replication-factor 1
--if-not-exists


#
##  schema-registry
#
docker-compose exec schemaregistry bash

kafka-avro-console-producer --topic users-topic-avro \
--bootstrap-server kafkabroker.sandbox.net:19091 \
--property value.schema="$(< /opt/app/schema/user.avsc)"

# Register a new version of a schema under the subject "Kafka-key"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://localhost:8081/subjects/auditlog-topic-avro-value/versions

# Register a new version of a schema under the subject "Kafka-value"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://localhost:8081/subjects/auditlog-topic-avro-value/versions

# List all subjects
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
https://localhost:8081/subjects

# List all schema versions registered under the subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://localhost:8081/subjects/auditlog-topic-avro-value/versions

# Fetch a schema by globally unique id 1
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://localhost:8081/schemas/ids/1

# Fetch version 1 of the schema registered under subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://localhost:8081/subjects/auditlog-topic-avro-value/versions/1

# Fetch the most recently registered schema under subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://localhost:8081/subjects/auditlog-topic-avro-value/versions/latest

# Check whether a schema has been registered under subject "Kafka-key"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://localhost:8081/subjects/Kafka-key

# Test compatibility of a schema with the latest schema under subject "Kafka-value"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://localhost:8081/compatibility/subjects/auditlog-topic-avro-value/versions/latest

# Get top level config
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://localhost:8081/config

# Update compatibility requirements globally
$ curl -X PUT -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"compatibility": "NONE"}' \
http://localhost:8081/config

# Update compatibility requirements under the subject "Kafka-value"
$ curl -X PUT -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"compatibility": "BACKWARD"}' \
http://localhost:8081/config

kafka-avro-console-consumer --topic users \
--bootstrap-server kafkabroker.sandbox.net:19091

kafka-avro-console-consumer --topic users \
--bootstrap-server kafkabroker.sandbox.net:19091 \
--property schema.registry.url=http://schema-registry:8081 \
--from-beginning

http://dockerhost:8081/subjects
["users-value","order-updated-value","order-created-value"]

http://dockerhost:8081/schemas/types

http://dockerhost:8081/subjects/order-updated-value/versions
http://dockerhost:8081/schemas/ids/1

http://dockerhost:8081/subjects?deleted=true



docker-compose exec connect sh -c "curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_pageviews_cos.config"
docker-compose exec connect sh -c "curl -X POST -H 'Content-Type: application/json' --data @connector_pageviews_cos.config http://localhost:8083/connectors"

docker-compose exec connect sh -c "curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_users_cos.config"
docker-compose exec connect sh -c "curl -X POST -H 'Content-Type: application/json' --data @connector_users_cos.config http://localhost:8083/connectors"

docker container stop $(docker container ls -a -q -f "label=io.confluent.docker")
docker container stop $(docker container ls -a -q -f "label=io.confluent.docker") && docker system prune -a -f --volumes


./kafka-avro-console-producer --broker-list localhost:9093 --topic myTopic \
--producer.config ~/ect/kafka/producer.properties --property value.schema=‘{“type”:“record”,“name”:“myrecord”,“fields”:[{“name”:“f1”,“type”:“string”}]}’ \
--property schema.registry.url=https://localhost:8081 --property schema.registry.ssl.truststore.location=/etc/kafka/security/schema.registry.client.truststore.jks --property schema.registry.ssl.truststore.password=myTrustStorePassword


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

### Change Kafka Retention Time
kafka-topics --zookeeper zookeeper:2181 --alter --topic users-topic-avro --config retention.ms=1000

### Setup Default  7 days (168 hours , retention.ms= 604800000)

### Get Detail Info about Your Consumer Group –
docker-compose -f docker-compose.yml exec -T kafkabroker kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --list
docker-compose -f docker-compose.yml exec -T kafkabroker kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --describe --group test-avro-cg

kafka-consumer-groups --describe --bootstrap-server localhost:9092 --group test-taxi-rides-cg
kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --list

#### Delete Offset
kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --delete --group group_name

#### Reset Offset
kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --reset-offsets --to-earliest --all-topics --execute --group test-taxi-rides-cg

##### --shift-by :- Reset the offset by incrementing the current offset position by take both +ve or -ve number
kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --group test-taxi-rides-cg --reset-offsets --shift-by 10 --topic sales_topic --execute

##### --to-datetime :- Reset offsets to offset from datetime. Format: ‘YYYY-MM-DDTHH:mm:SS.sss’
kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --group test-taxi-rides-cg --reset-offsets --to-datetime 2020-11-01T00:00:00Z --topic sales_topic --execute

##### --to-earliest :- Reset offsets to earliest (oldest) offset available in the topic.
kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --group test-taxi-rides-cg --reset-offsets --to-earliest --topic sales_topic --execute

##### --to-latest :- Reset offsets to latest (recent) offset available in the topic.
kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:19091 --group test-taxi-rides-cg --reset-offsets --to-latest --topic taxi-rides --execute

### Get Detail Info about Your Consumer Group –

### View Only 10  Messages on the Terminal –

```shell
#!/bin/bash
echo "Enter name of topic to empty:"
read topicName
kafka-configs --zookeeper localhost:2181 --alter --entity-type topics --entity-name $topicName --add-config retention.ms=1000
sleep 5
kafka-configs --zookeeper localhost:2181 --alter --entity-type topics --entity-name $topicName --delete-config retention.ms
```

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
--volume ./conf/kafka:/etc/kafka/secrets \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:9093 -d all -L \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/etc/kerberos/keytabs/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET'


#
# Producer
#
docker run -it --rm \
--network sandbox.net \
--volume ./conf/kafka:/etc/kafka/secrets \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:9093 \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/etc/kerberos/keytabs/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-t test_topic \
-P -l /etc/kafka/secrets/data/messages.txt

#
#
#
docker run -it --rm \
--network sandbox.net \
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19092 -C \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/etc/kerberos/keytabs/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-K: -f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\n\Partition: %p\tOffset: %o\n--\n' \
-t test_topic



#
# List Topics Using SASL_SSL
#
docker run -it --rm \
--network sandbox.net \
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19093 -L -J \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/etc/kerberos/keytabs/kafkaclient.keytab' \
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
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19093 -P -t test_topic \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/etc/kerberos/keytabs/kafkaclient.keytab' \
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
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19093 -C -t test_topic -o beginning \
-f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n' -e \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/etc/kerberos/keytabs/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-X 'ssl.key.location=/etc/kafka/secrets/clients.key' \
-X 'ssl.key.password=confluent' \
-X 'ssl.certificate.location=/etc/kafka/secrets/clients-signed.crt' \
-X 'ssl.ca.location=/etc/kafka/secrets/sandbox-ca.pem'


docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kafka/data:/etc/kafka/data \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /etc/kafka/secrets/cnf/librdkafka.config -C -t test_topic -o -10 \
-f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n'


docker run --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kafka/data:/etc/kafka/data \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
--env KAFKA_OPTS="-Djava.security.auth.login.config=/etc/kafka/secrets/jaas/kafkaclients_jaas.conf -Djava.security.krb5.conf=/etc/kerberos/krb5.conf -Dsun.security.krb5.debug=false" \
confluentinc/cp-server:7.5.0 \
sh -c "kafka-console-producer --topic kcat-test-topic \
--broker-list kafkabroker.sandbox.net:19093 \
--producer.config /etc/kafka/secrets/cnf/client-ssl.properties \
--property parse.key=true \
--property key.separator=, < /etc/kafka/secrets/data/kcat_messages.txt"


docker compose exec kafkabroker sh -c "kafka-console-producer --topic kcat-test-topic \
--broker-list kafkabroker.sandbox.net:19093 \
--producer.config /etc/kafka/secrets/cnf/client-ssl.properties \
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