E.g. to consume all messages from mytopic partition 2 and then exit:

## Arguments Details 
* -L
* -J  for JSON Format
* -C
* * -K delimiter
* -P
* * -K delimiter
* 
* -t  for topic name
* -p  for partition
* -o  for offset    |  beginning, 10, -10
* -l  data file path
* 

```shell
docker run -it --rm \
--network sandbox.net \
--volume /apps:/apps \
--volume ./bd-docker-sandbox/conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -V

kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_plaintext.config  -L -J | jq .

```

##
docker compose -f  bd-docker-sandbox/dc-kafka-cluster.yaml exec kafkaclient sh -c "kafkacat -V"

# Producing messages inline from a script
docker run --interactive \
--network sandbox.net \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19092 -P \
-t kafka-simple-topic \
-K : \
-l /apps/sandbox/kafka/json_messages.txt


docker run -it --rm \
--hostname=clients.sandbox.net \
--network sandbox.net \
--volume ./conf/kafka/data:/etc/kafka/data \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -F /etc/kafka/secrets/cnf/librdkafka.config -C -t test_topic -o -10 \
-f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n'

nc -vz kafkabroker.sandbox.net 9092

# Producer
```shell
echo "aea284e3-24c6-4969-a85f-fff8e34fb41c	{'uuid': 'aea284e3-24c6-4969-a85f-fff8e34fb41c', 'addts': 1708533236}"| kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -P -t kafka-simple-topic -K '\t'

```
# Consumer
```shell
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -C -t kafka-simple-topic -f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e

## with consumer group
kafkacat -b kafkabroker.sandbox.net:9092 -G kafka-simple-cg kafka-simple-topic -o 600 -e
docker compose -f  bd-docker-sandbox/dc-kafka-cluster.yaml exec kafkaclient sh -c "kafkacat -b kafkabroker.sandbox.net:19092 -C -G kafka-simple-cg -t kafka-simple-topic -f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' "
docker compose -f  bd-docker-sandbox/dc-kafka-cluster.yaml exec kafkabroker sh -c "kafka-console-consumer --bootstrap-server kafkabroker.sandbox.net:19092 --topic kafka-simple-topic --from-beginning"

```

## PLAINTEXT
```shell
docker compose -f  bd-docker-sandbox/dc-kafka-cluster.yaml exec kafkaclient sh -c "kafkacat -b kafkabroker.sandbox.net:19092 -L"
docker compose -f  bd-docker-sandbox/dc-kafka-cluster.yaml exec kafkaclient sh -c "kafkacat -b kafkabroker.sandbox.net:19092 -P -t kafka-simple-topic -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose -f  bd-docker-sandbox/dc-kafka-cluster.yaml exec kafkaclient sh -c "kafkacat -b kafkabroker.sandbox.net:19092 -C -t kafka-simple-topic -o -10 -e"
```
## SASL_PLAINTEXT
```shell
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -P -t kafka-simple-topic -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -C -t kafka-simple-topic -o -10 -e"
```
## SASL_SSL
```shell
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_ssl.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_ssl.config -P -t kafka-simple-topic -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_ssl.config -C -t kafka-simple-topic -o -10 -f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n' -e 2>/dev/null"
```
## SSL
```shell
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_ssl.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_ssl.config -P -t kafka-simple-topic -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_ssl.config -C -t kafka-simple-topic -o -10 -e"
```

## From Host machine
```shell
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_plaintext.config -L
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_plaintext.config -C -t kafka-simple-topic -o -10 -e
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_sasl_ssl.config -C -t kafka-simple-topic -o -10 -f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n' -e 2>/dev/null
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_ssl.config -C -t kafka-simple-topic -o -10 -e
```