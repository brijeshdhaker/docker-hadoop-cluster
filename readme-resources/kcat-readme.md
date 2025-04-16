kafkacat is one of my go-to tools when working with Kafka. It’s a producer and consumer, but also a swiss-army knife of debugging and troubleshooting capabilities. So when I built a new Fedora server recently, I needed to get it installed. Unfortunately there’s no pre-packed install available on yum, so here’s how to do it manually.

Note
kafkacat is now known as kcat (ref). When invoking the command you will need to use kcat in place of kafkacat.
Pre-requisite installs 🔗
We’ll need some packages from the Confluent repo so set this up for yum first by creating /etc/yum.repos.d/confluent.repo:
```shell
[Confluent.dist]
name=Confluent repository (dist)
baseurl=https://packages.confluent.io/rpm/5.4/7
gpgcheck=1
gpgkey=https://packages.confluent.io/rpm/5.4/archive.key
enabled=1

[Confluent]
name=Confluent repository
baseurl=https://packages.confluent.io/rpm/5.4
gpgcheck=1
gpgkey=https://packages.confluent.io/rpm/5.4/archive.key
enabled=1
Now install the dependencies:
```
# Update yum
```shell
sudo yum update -y
```

# Install build tools
```shell
sudo yum group install "Development Tools" -y
```
# Install librdkafka and other deps
```shell
sudo yum install -y librdkafka-devel yajl-devel avro-c-devel
```

Build kafkacat 🔗
Pull down the kafkacat repo:
```shell
git clone https://github.com/edenhill/kafkacat.git
cd kafkacat
```
Prepare the install - make sure that this step does not result in an error!
```shell
./configure
```

If you get errors here, it’s off to Google you go to try and figure them out, because there’s no point continuing if you can’t. You might find some failed steps that don’t result in an actual error - this is a "soft fail" and means that certain functionality won’t be available in the kafkacat that you install (in this case, Avro/Schema Registry). Here’s an example of one:

checking for serdes (by pkg-config)... failed
checking for serdes (by compile)... failed (disable)
Install! 🔗
```shell
make
sudo make install
```

Check that it works:
```shell
➜ kafkacat -V
kafkacat - Apache Kafka producer and consumer tool
https://github.com/edenhill/kafkacat
Copyright (c) 2014-2019, Magnus Edenhill
Version 1.5.0-5-ge98256 (JSON, librdkafka 1.3.0 builtin.features=gzip,snappy,ssl,sasl,regex,lz4,sasl_gssapi,sasl_plain,sasl_scram,plugins,zstd,sasl_oauthbearer)
```
Test it:
```shell
➜ kafkacat -b kafkabroker.sandbox.net:9092 -L
Metadata for all topics (from broker 3: kafkabroker.sandbox.net:9092/3):
3 brokers:
broker 2 at kafkabroker.sandbox.net:19092
broker 3 at kafkabroker.sandbox.net:9092 (controller)
broker 1 at kafkabroker.sandbox.net:9092
```
Note
kafkacat is now known as kcat (ref). When invoking the command you will need to use kcat in place of kafkacat.
This all seems like too much hassle? 🔗
Yeah, that’s why Docker was invented ;-)

If you want to run kafkacat but can’t get it installed, do not fear! You can run it anyway:

docker run --rm edenhill/kafkacat:1.5.0 \
kafkacat -V
You just need to make sure you wrap your head around Docker networking if you do this, because localhost to a Docker container is not the same (by default) as localhost on your host machine:

➜ docker run --rm edenhill/kafkacat:1.5.0 \
kafkacat -b kafkabroker.sandbox.net:9092 -L
% ERROR: Failed to acquire metadata: Local: Broker transport failure
If you add --network=host then it will use the network as if executing locally:

➜ docker run --rm --network=host edenhill/kafkacat:1.5.0 \
kafkacat -b kafkabroker.sandbox.net:9092 -L

Metadata for all topics (from broker 3: kafkabroker.sandbox.net:9092/3):
3 brokers:
broker 2 at kafkabroker.sandbox.net:19092
broker 3 at kafkabroker.sandbox.net:9092 (controller)
broker 1 at kafkabroker.sandbox.net:9092


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