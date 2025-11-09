# Flink Operations Playground Image

The image defined by the `Dockerfile` in this folder is required by the Flink operations playground.

The image is based on the official Flink image and adds a demo Flink job (Click Event Count) and a corresponding data generator. The code of the application is located in the `./java/flink-ops-playground` folder.

#
```shell
HADOOP_CLASSPATH=`hadoop classpath` ${HOME}/flink-1.9.0/bin/flink run \
-m yarn-cluster \
-yqu some_yarn_queue \
-ynm "Flink on YARN" \
-yt ${HOME}/important_files \
-p 4 -yjm 1024m -ytm 1024m \
--class HelloWorld \
${HOME}/foo.jar

```

#### Start Flink Cluster
```shell

docker compose -f bd-docker-sandbox/docker-compose.yml up -d minio
docker compose -f bd-docker-sandbox/docker-compose.yml up -d zookeeper kafka-broker schema-registry
docker compose -f bd-docker-sandbox/docker-compose.yml up -d flink-jobmanager flink-taskmanager

## Topic - Create
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-topics --create --bootstrap-server kafka-broker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-text-topic --if-not-exists"
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-topics --create --bootstrap-server kafka-broker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-csv-topic --if-not-exists"
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-topics --create --bootstrap-server kafka-broker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-avro-topic --if-not-exists"
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-topics --create --bootstrap-server kafka-broker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic transaction-json-topic --if-not-exists"
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-topics --create --bootstrap-server kafka-broker.sandbox.net:9092 --partitions 3 --replication-factor 1 --topic fraud-alerts-topic --if-not-exists"

## List Topics
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-topics --list --bootstrap-server kafka-broker.sandbox.net:9092"
```

#### Stop Flink Cluster
```shell

docker compose -f bd-docker-sandbox/docker-compose.yml down -d flink-jobmanager flink-taskmanager
docker compose -f bd-docker-sandbox/docker-compose.yml down zookeeper kafka-broker schema-registry
docker compose -f bd-docker-sandbox/docker-compose.yml down minio
```

#
```bash

docker run --rm -i -t \
--network sandbox.net \
--add-host=docker.sandbox.net:172.18.0.1 \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v /apps:/apps \
-v /apps/configs/flink/transactions_workflow.properties:/opt/flink/conf/transactions_workflow.properties \
-v ./bd-docker-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v ./bd-flink-module/target/bd-flink-module.jar:/opt/bd-flink-module/bd-flink-module.jar \
-v ${HOME}/.m2/repository/org/apache/flink/flink-s3-fs-hadoop/1.20.0/flink-s3-fs-hadoop-1.20.0.jar:/opt/flink/plugins/s3-fs-hadoop/flink-s3-fs-hadoop-1.20.0-cp1.jar \
--name flink-playbox \
confluentinc/cp-flink:1.20.0-cp1-java17 /bin/bash

```

# Start Data Generation
```shell

java -classpath /opt/bd-flink-module/bd-flink-module.jar:/opt/flink/lib/* org.examples.flink.transaction.datagen.DataGenerator
```

# Start Financial Transactions Data Generation
```shell

java -classpath ./bd-flink-module/target/original-bd-flink-module.jar org.examples.flink.frauds.producers.TxnProducer
```

# Validate Data Generation
```shell

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-console-consumer \
--topic transaction-csv-topic \
--bootstrap-server kafka-broker.sandbox.net:9092 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 10000 2>/dev/null"  

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-console-consumer \
--topic transaction-json-topic \
--bootstrap-server kafka-broker.sandbox.net:9092 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 10000 2>/dev/null" 

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-console-consumer \
--topic fraud-alerts-topic \
--bootstrap-server kafka-broker.sandbox.net:9092 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 10000 2>/dev/null" 
```

# Start event generation
```shell

java -classpath /opt/bd-flink-module/bd-flink-module.jar:/opt/flink/lib/* org.examples.flink.clickcount.ClickEventGenerator \
--bootstrap.servers kafka-broker.sandbox.net:9092 \
--topic click-event-source &

```

# Start event count flink job
```shell

nohup flink run --detached \
--class org.examples.flink.clickcount.ClickEventCount /opt/bd-flink-module/bd-flink-module.jar \
--checkpointing \
--event-time \
--bootstrap.servers kafka-broker.sandbox.net:9092 \
--input-topic click-event-source \
--output-topic click-event-sink 2>/dev/null &

```

# Job has been submitted with JobID 3ee7c8da616b3cfdea2a37916c7ac41e
```shell

flink list

flink stop 3ee7c8da616b3cfdea2a37916c7ac41e
```

# Restart Job from save  point
```shell

/opt/flink/bin/flink run --detached \
--fromSavepoint s3a://defaultfs/execution/savepoints/savepoint-3ee7c8-4671ebd2716c \
--class flink.playgrounds.ops.clickcount.ClickEventCount /opt/bd-flink-module.jar \
--checkpointing \
--event-time \
--bootstrap.servers kafka-broker.sandbox.net:9092 \
--input-topic click-event-source \
--output-topic click-event-sink

```

# Validate Source & Sink
```shell

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-console-consumer \
--topic click-event-source \
--bootstrap-server kafka-broker.sandbox.net:9092 \
--from-beginning \
--property print.timestamp=true \
--property print.key=true \
--property print.value=true \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 20000 2>/dev/null" 


docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-console-consumer \
--topic click-event-source \
--bootstrap-server kafka-broker.sandbox.net:9092 \
--partition 0 \
--offset 0 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 20000 " 2>/dev/null

```

# Validate Sink
```shell


docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-console-consumer \
--topic click-event-sink \
--bootstrap-server kafka-broker.sandbox.net:9092 \
--from-beginning \
--property print.timestamp=true \
--property print.key=true \
--property print.value=true \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 20000 " 2>/dev/null

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafka-broker sh -c "kafka-console-consumer \
--topic click-event-sink \
--bootstrap-server kafka-broker.sandbox.net:9092 \
--offset 0 \
--partition 0 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 20000 " 2>/dev/null

kafka-topics --describe --topic click-event-sink --bootstrap-server kafka-broker.sandbox.net:9092

```

###
```shell

./bin/flink run examples/streaming/WordCount.jar --input s3a://defaultfs/README.md --output s3a://defaultfs/output/3f7e50176f1221e967d3ca0d22ebaab1

```

### Start event count flink job
```shell
/opt/flink/bin/flink run --detached \
--class flink.playgrounds.delta.sink.DeltaSinkExampleLocal /opt/bd-flink-module/bd-flink-module.jar \
--checkpointing \
--event-time
```

### Transaction Pipeline
```shell
# mini-cluster
/opt/flink/bin/flink run --detached \
--class org.examples.flink.transaction.TransactionPipeline /opt/bd-flink-module/bd-flink-module.jar \
--engine-type mini-cluster \
--table-name transactions \
--app-config transactions_workflow.properties \
--config-path ${HOME}/IdeaProjects/docker-hadoop-cluster/bd-flink-module/src/main/resources/mini-cluster

# remote-cluster
/opt/flink/bin/flink run --detached \
--fromSavepoint s3a://defaultfs/execution/savepoints/savepoint-eecc8b-c2b562817f15 \
--class org.examples.flink.transaction.TransactionPipeline /opt/bd-flink-module/bd-flink-module.jar \
--engine-type remote-cluster \
--table-name transactions \
--app-config s3a://defaultfs/workflows/1001/conf/transactions_workflow.properties \
--config-path file://opt/flink/conf

```
###
```shell
/opt/flink/bin/flink run --detached \
--class flink.playgrounds.delta.sink.DeltaSinkExampleCluster /opt/bd-flink-module/bd-flink-module.jar \
--checkpointing \
--event-time

```

### bounded
```shell
flink run --detached \
--class flink.playgrounds.delta.source.bounded.DeltaBoundedSourceClusterExample /opt/bd-flink-module/bd-flink-module.jar \
--table-path s3a://defaultfs/delta-flink-example/
```

### continuous
```shell
flink run --detached \
--class flink.playgrounds.delta.source.continuous.DeltaContinuousSourceClusterExample /opt/bd-flink-module/bd-flink-module.jar \
--table-path s3a://defaultfs/delta-flink-example/

```

```shell
mvn package exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=org.example.source.bounded.DeltaBoundedSourceExample -Dstaging.repo.url={maven_repo} -Dconnectors.version={version}
mvn package exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=org.example.sink.DeltaSinkExample -Dstaging.repo.url={maven_repo} -Dconnectors.version={version}


mvn package exec:java \ 
-Dmaven.multiModuleProjectDirectory=${HOME}/IdeaProjects/docker-hadoop-cluster/bd-flink-module/ \
-Dmaven.repo.local=/apps/.m2/repository \
-DskipTests=true \
-Dexec.cleanupDaemonThreads=false \
-Dexec.mainClass=flink.playgrounds.delta.sink.DeltaSinkExampleCluster \
-P local \

-Dstaging.repo.url={maven_repo} \
-Dconnectors.version={version}

/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java  -Djansi.passthrough=true -Dmaven.home=/opt/maven-3.6.3 -Dclassworlds.conf=/opt/maven-3.6.3/bin/m2.conf -Dmaven.ext.class.path=/snap/intellij-idea-community/553/plugins/maven/lib/maven-event-listener.jar -javaagent:/snap/intellij-idea-community/553/lib/idea_rt.jar=41423:/snap/intellij-idea-community/553/bin -Dfile.encoding=UTF-8 -classpath /opt/maven-3.6.3/boot/plexus-classworlds-2.6.0.jar:/opt/maven-3.6.3/boot/plexus-classworlds.license org.codehaus.classworlds.Launcher -Didea.version=2024.3 --update-snapshots -s ${HOME}/.m2/settings.xml -Dmaven.repo.local=/apps/.m2/repository -DskipTests=true clean package -P local,!cluster


-Dlog.file=/apps/var/logs/flink/flink--standalonesession-0-flink-jobmanager.log -Dlog4j.configuration=file:/opt/flink/conf/log4j-console.properties -Dlog4j.configurationFile=file:/opt/flink/conf/log4j-console.properties -Dlogback.configurationFile=file:/opt/flink/conf/logback-console.xml


#
#
#
/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -Dmaven.multiModuleProjectDirectory=${HOME}/IdeaProjects/docker-hadoop-cluster/bd-flink-module -Djansi.passthrough=true -Dmaven.home=/snap/intellij-idea-community/560/plugins/maven/lib/maven3 -Dclassworlds.conf=/snap/intellij-idea-community/560/plugins/maven/lib/maven3/bin/m2.conf -Dmaven.ext.class.path=/snap/intellij-idea-community/560/plugins/maven/lib/maven-event-listener.jar -javaagent:/snap/intellij-idea-community/560/lib/idea_rt.jar=39325:/snap/intellij-idea-community/560/bin -Dfile.encoding=UTF-8 -classpath /snap/intellij-idea-community/560/plugins/maven/lib/maven3/boot/plexus-classworlds-2.8.0.jar:/snap/intellij-idea-community/560/plugins/maven/lib/maven3/boot/plexus-classworlds.license org.codehaus.classworlds.Launcher -Didea.version=2024.3.1 --update-snapshots -s ${HOME}/.m2/settings.xml -Dmaven.repo.local=/apps/.m2/repository -DskipTests=true clean package -P cluster

mvn clean install 
```