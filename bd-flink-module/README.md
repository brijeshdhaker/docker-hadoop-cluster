# Flink Operations Playground Image

The image defined by the `Dockerfile` in this folder is required by the Flink operations playground.

The image is based on the official Flink image and adds a demo Flink job (Click Event Count) and a corresponding data generator. The code of the application is located in the `./java/flink-ops-playground` folder.


#
#
#
```shell

docker compose -f bd-docker-sandbox/docker-compose.yml up -d flink-jobmanager flink-taskmanager

```


#
#
#
```shell

docker run --rm -d --network sandbox.net --name txn-data-generator brijeshdhaker/bd-data-generator:1.0.0
docker logs -f txn-data-generator

docker container stop txn-data-generator
docker container rm txn-data-generator

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic transaction-csv-topic \
--bootstrap-server kafkabroker.sandbox.net:9092" \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--timeout-ms 5000 2>/dev/null"

```

#
#
#
./bin/flink run examples/streaming/WordCount.jar --input s3a://warehouse-flink/README.md --output s3a://warehouse-flink/output



#
#
#
```shell

docker run --rm -i -t \
--network sandbox.net \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v /apps:/apps \
-v ./bd-flink-module/target:/opt/bd-flink-module \
-v ./bd-docker-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v ./bd-docker-sandbox/resources/libs/s3-fs-hadoop/flink-s3-fs-hadoop-1.20.0.jar:/opt/flink/plugins/s3-fs-hadoop/flink-s3-fs-hadoop-1.20.0.jar \
--name flink-playbox \
apache/flink:1.20.0-scala_2.12-java17 /bin/bash

# start event generation
java -classpath /opt/bd-flink-module/bd-flink-module-1.0.0.jar:/opt/flink/lib/* flink.playgrounds.ops.clickcount.ClickEventGenerator --bootstrap.servers kafkabroker.sandbox.net:9092 --topic click-event-source &

# start event count flink job
flink run --detached \
--class flink.playgrounds.ops.clickcount.ClickEventCount /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time \
--bootstrap.servers kafkabroker.sandbox.net:9092 \
--input-topic click-event-source \
--output-topic click-event-sink

# Job has been submitted with JobID 3ee7c8da616b3cfdea2a37916c7ac41e

flink list

flink stop 3ee7c8da616b3cfdea2a37916c7ac41e

# Restart Job from save  point
/opt/flink/bin/flink run --detached \
--fromSavepoint s3a://warehouse-flink/execution/savepoints/savepoint-3ee7c8-4671ebd2716c \
--class flink.playgrounds.ops.clickcount.ClickEventCount /opt/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time \
--bootstrap.servers kafkabroker.sandbox.net:9092 \
--input-topic click-event-source \
--output-topic click-event-sink

```

# Validate Source & Sink
```shell

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic click-event-source \
--bootstrap-server kafkabroker.sandbox.net:9092" \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--timeout-ms 5000 2>/dev/null"

```

# Validate Sink
```shell

docker compose -f bd-docker-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic click-event-sink \
--bootstrap-server kafkabroker.sandbox.net:9092" \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--timeout-ms 5000 2>/dev/null"

```
jar tf ./bd-docker-sandbox/resources/libs/s3-fs-hadoop/flink-s3-fs-hadoop-1.16.2.jar | grep "org.apache.hadoop.fs.s3a.S3AFileSystem"

# start event count flink job
flink run --detached \
--class flink.playgrounds.delta.sink.DeltaSinkExample /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time


flink run --detached \
--class flink.playgrounds.delta.sink.DeltaSinkExampleCluster /opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--checkpointing \
--event-time

flink run --detached \
--class flink.playgrounds.delta.sink.DeltaSinkExampleCluster /opt/bd-flink-module/bd-flink-module-1.0.0-jar-with-dependencies.jar \
--table-path s3a://warehouse-flink/delta-flink-example/


