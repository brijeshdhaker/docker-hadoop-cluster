#
# Start Kafka Cluster
#
```shell
docker compose -f  bd-hadoop-sandbox/docker-compose.yml up -d
```

# Kafka - Broker Validations
```shell
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkaclient sh -c "kafkacat -V"
```

### Topic - Actions :
```shell
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker /bin/bash

kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic kafka-simple-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic kafka-partitioned-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic kafka-avro-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic kafka-json-topic --if-not-exists

kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-text-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-json-topic --if-not-exists
kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-avro-topic --if-not-exists

docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-topics --create --bootstrap-server kafkabroker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-avro-topic --if-not-exists"

# Topic - List
kafka-topics --list --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-topics --list --bootstrap-server kafkabroker.sandbox.net:9092"

# Topic - Describe
kafka-topics --describe --topic transaction-avro-topic --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f  bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-topics --describe --topic transaction-avro-topic --bootstrap-server kafkabroker.sandbox.net:9092 "

# Topic - Alter
kafka-topics --alter --topic transaction-avro-topic --partitions 3 --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f  bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-topics --alter --topic transaction-avro-topic --partitions 3 --bootstrap-server kafkabroker.sandbox.net:9092 "

# Topic - Delete
kafka-topics --delete --topic transaction-avro-topic --bootstrap-server kafkabroker.sandbox.net:9092
docker compose -f  bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-topics --delete --topic transaction-avro-topic --bootstrap-server kafkabroker.sandbox.net:9092 "

# Topic - Check Retention period
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-configs --bootstrap-server kafkabroker.sandbox.net:9092 --entity-type topics --entity-name kafka-simple-topic --describe "
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-configs --bootstrap-server kafkabroker.sandbox.net:9092 --entity-type topics --entity-default --alter --add-config delete.retention.ms=172800000 "

### Change Kafka Retention Time
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-configs --bootstrap-server kafkabroker.sandbox.net:9092 --alter --topic transaction-avro-topic --add-config retention.ms=1000"



### Setup Default  7 days (168 hours , retention.ms= 604800000)
```

### Producer :
```shell
docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume /apps:/apps \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -P -b kafkabroker.sandbox.net:19093 -t kafka-simple-topic \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-X 'ssl.key.location=/apps/security/ssl/clients.key' \
-X 'ssl.key.password=confluent' \
-X 'ssl.certificate.location=/apps/security/ssl/clients-signed.crt' \
-X 'ssl.ca.location=/apps/security/ssl/sandbox-ca.pem' \
-K '\t' \
-l /apps/sandbox/kafka/json_messages.txt


docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-producer \
--topic kafka-simple-topic \
--broker-list kafkabroker.sandbox.net:9092"

#### With Key
#### Note : \t is default key seperator
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-producer \
--topic kafka-simple-topic \
--broker-list kafkabroker.sandbox.net:9092 \
--producer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--property parse.key=true \
< /apps/sandbox/kafka/json_messages.txt \
2>/dev/null"

# --property parse.key=true \
```

#
### Consumer :
#
```shell

docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume /apps:/apps \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -C -b kafkabroker.sandbox.net:19093 -t kafka-simple-topic -o beginning \
-K '\t' \
-f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e \
-X 'security.protocol=SASL_SSL' \
-X 'sasl.mechanisms=GSSAPI' \
-X 'sasl.kerberos.service.name=kafka' \
-X 'sasl.kerberos.keytab=/apps/security/keytabs/services/kafkaclient.keytab' \
-X 'sasl.kerberos.principal=kafkaclient@SANDBOX.NET' \
-X 'ssl.key.location=/apps/security/ssl/clients.key' \
-X 'ssl.key.password=confluent' \
-X 'ssl.certificate.location=/apps/security/ssl/clients-signed.crt' \
-X 'ssl.ca.location=/apps/security/ssl/sandbox-ca.pem'



docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume /apps:/apps \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_ssl.config -C -t kafka-simple-topic -o beginning \
-K '\t' \
-f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e

docker compose -f  bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic kafka-simple-topic \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--timeout-ms 5000 2>/dev/null"

#
docker compose -f  bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic kafka-simple-topic \
--bootstrap-server kafkabroker.sandbox.net:19092 \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--offset 0 \
--partition 0 \
--property print.key=true \
--property key.separator=' - ' \
--timeout-ms 5000 2>/dev/null"

docker compose -f  bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic kafka-simple-topic \
--group kafka-simple-cg \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--property print.key=true \
--property key.separator='  -  ' \
--timeout-ms 5000 2>/dev/null"

```
docker system prune -a --volumes --filter "label=io.confluent.docker"

# Application Setup


# To check the end offset set parameter time to value -1
kafka-run-class kafka.tools.GetOffsetShell \
--broker-list kafkabroker.sandbox.net:9092 \
--topic transaction-avro-topic \
--time -1

# To check the start offset, use --time -2
kafka-run-class kafka.tools.GetOffsetShell \
--broker-list kafkabroker.sandbox.net:9092 \
--topic transaction-avro-topic \
--time -2


### Get Detail Info about Your Consumer Group –
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --list"
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --describe --group transaction-avro-cg --members"

#### Delete Offset
docker-compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --delete --group kafka-simple-cg "

#### Reset Offset
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --reset-offsets --to-earliest --all-topics --execute --group kafka-simple-cg "

##### --shift-by :- Reset the offset by incrementing the current offset position by take both +ve or -ve number
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --shift-by 10 --topic sales_topic --execute "

##### --to-datetime :- Reset offsets to offset from datetime. Format: ‘YYYY-MM-DDTHH:mm:SS.sss’
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --to-datetime 2020-11-01T00:00:00Z --topic sales_topic --execute "

##### --to-earliest :- Reset offsets to earliest (oldest) offset available in the topic.
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --to-earliest --topic sales_topic --execute "

##### --to-latest :- Reset offsets to latest (recent) offset available in the topic.
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-consumer-groups --bootstrap-server kafkabroker.sandbox.net:9092 --group kafka-simple-cg --reset-offsets --to-latest --topic taxi-rides --execute "

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
# Avro Producer & Consumer
#
```shell

docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-avro-console-producer \
--topic transaction-avro-topic \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--property value.schema='$(< /opt/app/schema/user.avsc)'"

docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-avro-console-consumer 
--topic transaction-avro-topic \
--bootstrap-server kafkabroker.sandbox.net:9092 "

docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-avro-console-consumer \
--topic transaction-avro-topic \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--from-beginning \
--property schema.registry.url=http://schemaregistry:8081 "

```

#
##  schemaregistry
#
```shell
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec schemaregistry /bin/bash

# Register a new version of a schema under the subject "Kafka-key"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry.sandbox.net:8081/subjects/transaction-avro-topic-value/versions

# Register a new version of a schema under the subject "Kafka-value"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry.sandbox.net:8081/subjects/transaction-avro-topic-value/versions

# List all subjects
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry.sandbox.net:8081/subjects

# List all schema versions registered under the subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry.sandbox.net:8081/subjects/transaction-avro-topic-value/versions

# Fetch a schema by globally unique id 1
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry.sandbox.net:8081/schemas/ids/1

# Fetch version 1 of the schema registered under subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry.sandbox.net:8081/subjects/transaction-avro-topic-value/versions/1

# Fetch the most recently registered schema under subject "Kafka-value"
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry.sandbox.net:8081/subjects/transaction-avro-topic-value/versions/latest

# Check whether a schema has been registered under subject "Kafka-key"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry.sandbox.net:8081/subjects/transaction-avro-topic-key

# Test compatibility of a schema with the latest schema under subject "Kafka-value"
$ curl -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"schema": "{\"type\": \"string\"}"}' \
http://schemaregistry.sandbox.net:8081/compatibility/subjects/transaction-avro-topic-value/versions/latest

# Get top level config
$ curl -X GET -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
http://schemaregistry.sandbox.net:8081/config

# Update compatibility requirements globally
$ curl -X PUT -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"compatibility": "NONE"}' \
http://schemaregistry.sandbox.net:8081/config

# Update compatibility requirements under the subject "Kafka-value"
$ curl -X PUT -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
--data '{"compatibility": "BACKWARD"}' \
http://schemaregistry.sandbox.net:8081/config

```



http://schemaregistry:8081/subjects
["users-value","order-updated-value","order-created-value"]

http://schemaregistry:8081/schemas/types

http://schemaregistry:8081/subjects/order-updated-value/versions
http://schemaregistry:8081/schemas/ids/1

http://schemaregistry:8081/subjects?deleted=true



docker-compose exec connect sh -c "curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_pageviews_cos.config"
docker-compose exec connect sh -c "curl -X POST -H 'Content-Type: application/json' --data @connector_pageviews_cos.config http://schemaregistry:8083/connectors"

docker-compose exec connect sh -c "curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_users_cos.config"
docker-compose exec connect sh -c "curl -X POST -H 'Content-Type: application/json' --data @connector_users_cos.config http://schemaregistry:8083/connectors"

docker container stop $(docker container ls -a -q -f "label=io.confluent.docker")
docker container stop $(docker container ls -a -q -f "label=io.confluent.docker") && docker system prune -a -f --volumes



docker run --rm confluentinc/cp-server:7.5.0 sh -c "/bin/kafka-storage random-uuid"
zookeeper-shell zookeeper:2181 ls /