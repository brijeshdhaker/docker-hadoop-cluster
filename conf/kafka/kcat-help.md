E.g. to consume all messages from mytopic partition 2 and then exit:

## Arguments Details 
* -L  List Topics
* -C  Consumer Mode
* * -K delimiter
* 
* -P  Producer Mode
* * -K delimiter
* -J  for JSON Format
* 
* -t  for topic name
* -p  for partition
* -o  for offset    |  beginning, 10, -10
* 
* 

docker run -it --rm \
--network sandbox.net \
--volume /apps:/apps \
--volume ./conf/kerberos/krb5.conf:/etc/krb5.conf \
--env KRB5_CONFIG=/etc/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19092 -L -J \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=PLAIN' \
-X 'sasl.username=admin' \
-X 'sasl.password=admin-secret' \
| jq .


## PLAINTEXT
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_plaintext.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_plaintext.config -P -t kafka-simple-topic -K '\t' -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_plaintext.config -C -t kafka-simple-topic -o -10 -K '\t' -f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e 2>/dev/null"

## SASL_PLAINTEXT
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -P -t kafka-simple-topic -K '\t' -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_paintext.config -C -t kafka-simple-topic -o -10 -K '\t' -f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e 2>/dev/null"

## SASL_SSL
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_ssl.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_ssl.config -P -t kafka-simple-topic -K '\t' -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_sasl_ssl.config -C -t kafka-simple-topic -o -10 -K '\t' -f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e 2>/dev/null"

## SSL
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_ssl.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_ssl.config -P -t kafka-simple-topic -K '\t' -l /apps/sandbox/kafka/json_messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_ssl.config -C -t kafka-simple-topic -o -10 -K '\t' -f '\nKey (%K bytes): %k\nValue (%S bytes): %s\nTimestamp: %T \nPartition: %p \nOffset: %o \n\n--\n' -e 2>/dev/null"


## From Host machine
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_plaintext.config -L
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_sasl_ssl.config -L
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_ssl.config -L

kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_plaintext.config -C -t kafka-simple-topic -o -10 -e
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_sasl_ssl.config -C -t kafka-simple-topic -o -10 -e
kafkacat -F /apps/sandbox/kafka/cnf/librdkafka_thinkpad_ssl.config -C -t kafka-simple-topic -o -10 -e
