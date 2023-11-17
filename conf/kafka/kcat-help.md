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
* 
* 

docker run -it --rm \
--network sandbox.net \
--volume ./conf/kafka/secrets:/etc/kafka/secrets \
--volume ./conf/kerberos:/etc/kerberos \
--env KRB5_CONFIG=/etc/kerberos/krb5.conf \
brijeshdhaker/kafka-clients:7.5.0 \
kafkacat -b kafkabroker.sandbox.net:19092 -L -J \
-X 'security.protocol=SASL_PLAINTEXT' \
-X 'sasl.mechanisms=PLAIN' \
-X 'sasl.username=admin' \
-X 'sasl.password=admin-secret' \
| jq .


## PLAINTEXT
docker compose exec kafkaclient kafkacat -F /etc/kafka/secrets/cnf/librdkafka_plaintext.config -C -t test_topic -o -10 -e

## SASL_PLAINTEXT
docker compose exec kafkaclient kafkacat -F /etc/kafka/secrets/cnf/librdkafka_sasl_paintext.config -C -t test_topic -o -10 -e

## SASL_SSL
docker compose exec kafkaclient sh -c "kafkacat -F /etc/kafka/secrets/cnf/librdkafka_sasl_ssl.config -L"
docker compose exec kafkaclient sh -c "kafkacat -F /etc/kafka/secrets/cnf/librdkafka_sasl_ssl.config -P -t test_topic -l /etc/kafka/secrets/data/messages.txt 2>/dev/null"
docker compose exec kafkaclient sh -c "kafkacat -F /etc/kafka/secrets/cnf/librdkafka_sasl_ssl.config -C -t test_topic -o -10 -f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\nTimestamp: %T\tPartition: %p\tOffset: %o\n--\n' -e 2>/dev/null"

## SSL
docker compose exec kafkaclient kafkacat -F /etc/kafka/secrets/cnf/librdkafka_ssl.config -C -t test_topic -o -10 -e


## From Host machine
kafkacat -F conf/kafka/secrets/cnf/librdkafka_thinkpad_plaintext.config -L
kafkacat -F conf/kafka/secrets/cnf/librdkafka_thinkpad_plaintext.config -C -t test_topic -o -10 -e
kafkacat -F conf/kafka/secrets/cnf/librdkafka_thinkpad_sasl_ssl.config -C -t test_topic -o -10 -e
kafkacat -F conf/kafka/secrets/cnf/librdkafka_thinkpad_ssl.config -C -t test_topic -o -10 -e
