# Flink Operations Playground Image

The image defined by the `Dockerfile` in this folder is required by the Flink operations playground.

The image is based on the official Flink image and adds a demo Flink job (Click Event Count) and a corresponding data generator. The code of the application is located in the `./java/flink-ops-playground` folder.

# Start Flink Cluster
```shell
docker compose -f bd-docker-sandbox/docker-compose.yml up -d flink-jobmanager flink-taskmanager
```
#
```shell

docker run --rm -d --network sandbox.net --name txn-data-generator brijeshdhaker/bd-data-generator:1.0.0
docker logs -f txn-data-generator

docker container stop txn-data-generator
docker container rm txn-data-generator


```
#
```shell
./bin/flink run examples/streaming/WordCount.jar --input s3a://defaultfs/README.md --output s3a://defaultfs/output

```
#
```shell
docker run --rm -i -t \
--network sandbox.net \
--add-host=raspberrypi.sandbox.net:172.18.0.1 \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v /apps/libs/flink/flink-s3-fs-hadoop-1.20.0-cp1.jar:/opt/flink/plugins/s3-fs-hadoop/flink-s3-fs-hadoop-1.20.0-cp1.jar \
-v ./bd-docker-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v ./bd-flink-module/target/bd-flink-module-1.0.0.jar:/opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--name flink-playbox \
confluentinc/cp-flink:1.20.0-cp1-java17-arm64 /bin/bash
```
# Start Transaction Generation
```shell
java -classpath /opt/bd-flink-module/bd-flink-module-1.0.0.jar/opt/flink/lib/* com.org.example.flink.transaction.datagen.DataGenerator
```

# Validate Transaction Generation
```shell
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic transaction-csv-topic \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 5000 " 2>/dev/null
```

# start event generation
```shell
java -classpath /opt/bd-flink-module/bd-flink-module-1.0.0.jar:/opt/flink/lib/* com.org.example.flink.clickcount.ClickEventGenerator \
--bootstrap.servers kafkabroker.sandbox.net:9092 \
--topic click-event-source &
```

# start event count flink job
```shell
/opt/flink/bin/flink run \
--detached \
--class com.org.example.flink.clickcount.ClickEventCount /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time \
--bootstrap.servers kafkabroker.sandbox.net:9092 \
--input-topic click-event-source \
--output-topic click-event-sink

```

# Job has been submitted with JobID 3ee7c8da616b3cfdea2a37916c7ac41e
```shell
/opt/flink/bin/flink list
```
# 
```shell
flink stop ca7f71cecf2de500300692328a5b02de
```

# Restart Job from save  point
```shell
/opt/flink/bin/flink run --detached \
--fromSavepoint s3a://defaultfs/execution/savepoints/savepoint-d76da7-200c05309eaf \
--class com.org.example.flink.clickcount.ClickEventCount /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time \
--bootstrap.servers kafkabroker.sandbox.net:9092 \
--input-topic click-event-source \
--output-topic click-event-sink
```

# Validate Source & Sink
```shell
docker compose -f ./bd-docker-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic click-event-source \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 5000 " 2>/dev/null
```

# Validate Sink
```shell
docker compose -f bd-docker-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic click-event-sink \
--bootstrap-server kafkabroker.sandbox.net:9092 \
--consumer.config /apps/configs/kafka/librdkafka_plaintext.config \
--timeout-ms 5000" 2>/dev/null

# jar tf ./bd-docker-sandbox/resources/libs/s3-fs-hadoop/flink-s3-fs-hadoop-1.16.2.jar | grep "org.apache.hadoop.fs.s3a.S3AFileSystem"
```



# start event count flink job
```shell
/opt/flink/bin/flink run --detached \
--class flink.playgrounds.delta.sink.DeltaSinkExampleLocal /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time
```

# Transaction Pipeline
```shell
/opt/flink/bin/flink run --detached \
--class com.org.example.flink.transaction.TransactionPipeline /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--engine-type remote-cluster \
--table-name transactions \
--config-path /opt/flink/conf
```
#
```shell
/opt/flink/bin/flink run --detached \
--class flink.playgrounds.delta.sink.DeltaSinkExampleCluster /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time

```

# bounded
```shell
flink run --detached \
--class flink.playgrounds.delta.source.bounded.DeltaBoundedSourceClusterExample /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--table-path s3a://defaultfs/delta-flink-example/
```

#
# continuous
#
```shell
flink run --detached \
--class flink.playgrounds.delta.source.continuous.DeltaContinuousSourceClusterExample /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--table-path s3a://defaultfs/delta-flink-example/

```


mvn package exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=org.example.source.bounded.DeltaBoundedSourceExample -Dstaging.repo.url={maven_repo} -Dconnectors.version={version}
mvn package exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass=org.example.sink.DeltaSinkExample -Dstaging.repo.url={maven_repo} -Dconnectors.version={version}


mvn package exec:java \ 
-Dmaven.multiModuleProjectDirectory=/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-flink-module/ \
-Dmaven.repo.local=/apps/.m2/repository \
-DskipTests=true \
-Dexec.cleanupDaemonThreads=false \
-Dexec.mainClass=flink.playgrounds.delta.sink.DeltaSinkExampleCluster \
-P local \

-Dstaging.repo.url={maven_repo} \
-Dconnectors.version={version}

/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java  -Djansi.passthrough=true -Dmaven.home=/opt/maven-3.6.3 -Dclassworlds.conf=/opt/maven-3.6.3/bin/m2.conf -Dmaven.ext.class.path=/snap/intellij-idea-community/553/plugins/maven/lib/maven-event-listener.jar -javaagent:/snap/intellij-idea-community/553/lib/idea_rt.jar=41423:/snap/intellij-idea-community/553/bin -Dfile.encoding=UTF-8 -classpath /opt/maven-3.6.3/boot/plexus-classworlds-2.6.0.jar:/opt/maven-3.6.3/boot/plexus-classworlds.license org.codehaus.classworlds.Launcher -Didea.version=2024.3 --update-snapshots -s /home/brijeshdhaker/.m2/settings.xml -Dmaven.repo.local=/apps/.m2/repository -DskipTests=true clean package -P local,!cluster


-Dlog.file=/apps/var/logs/flink/flink--standalonesession-0-flink-jobmanager.log -Dlog4j.configuration=file:/opt/flink/conf/log4j-console.properties -Dlog4j.configurationFile=file:/opt/flink/conf/log4j-console.properties -Dlogback.configurationFile=file:/opt/flink/conf/logback-console.xml


#
#
#
/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin/java -Dmaven.multiModuleProjectDirectory=/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-flink-module -Djansi.passthrough=true -Dmaven.home=/snap/intellij-idea-community/560/plugins/maven/lib/maven3 -Dclassworlds.conf=/snap/intellij-idea-community/560/plugins/maven/lib/maven3/bin/m2.conf -Dmaven.ext.class.path=/snap/intellij-idea-community/560/plugins/maven/lib/maven-event-listener.jar -javaagent:/snap/intellij-idea-community/560/lib/idea_rt.jar=39325:/snap/intellij-idea-community/560/bin -Dfile.encoding=UTF-8 -classpath /snap/intellij-idea-community/560/plugins/maven/lib/maven3/boot/plexus-classworlds-2.8.0.jar:/snap/intellij-idea-community/560/plugins/maven/lib/maven3/boot/plexus-classworlds.license org.codehaus.classworlds.Launcher -Didea.version=2024.3.1 --update-snapshots -s /home/brijeshdhaker/.m2/settings.xml -Dmaven.repo.local=/apps/.m2/repository -DskipTests=true clean package -P cluster

mvn clean install 