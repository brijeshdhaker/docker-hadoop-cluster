HADOOP_CLASSPATH=`hadoop classpath` /home/foouser/flink-1.9.0/bin/flink run \
-m yarn-cluster \
-yqu some_yarn_queue \
-ynm "Flink on YARN" \
-yt /home/foouser/important_files \
-p 4 -yjm 1024m -ytm 1024m \
--class HelloWorld \
/home/foouser/foo.jar

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
./bin/flink run examples/streaming/WordCount.jar --input s3a://defaultfs/README.md --output s3a://defaultfs/output/3f7e50176f1221e967d3ca0d22ebaab1



#
#
#
```shell

docker run --rm -i -t \
--network sandbox.net \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v /apps:/apps \
-v ./bd-docker-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v ./bd-flink-module/target/bd-flink-module-1.0.0.jar:/opt/bd-flink-module/bd-flink-module-1.0.0.jar \
--name flink-playbox \
flink:1.20.0-scala_2.12-java17 /bin/bash

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
--fromSavepoint s3a://defaultfs/execution/savepoints/savepoint-3ee7c8-4671ebd2716c \
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