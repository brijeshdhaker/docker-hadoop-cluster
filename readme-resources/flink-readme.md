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
docker compose -f bd-hadoop-sandbox/docker-compose.yml up -d zookeeper kafkabroker

```


#
#
#
```shell

docker run --rm -d --network sandbox.net --name txn-data-generator brijeshdhaker/bd-data-generator:1.0.0
docker logs -f txn-data-generator

docker container stop txn-data-generator
docker container rm txn-data-generator

docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic transaction-csv-topic \
--bootstrap-server kafkabroker.sandbox.net:9092" \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--timeout-ms 5000 2>/dev/null"

```

#
#
#
./bin/flink run examples/streaming/WordCount.jar --input s3a://warehouse-flink/README.md --output s3a://warehouse-flink/output/3f7e50176f1221e967d3ca0d22ebaab1



#
#
#
```shell

docker run --rm -i -t --network sandbox.net -v ./bd-flink-module/target/bd-flink-module-1.0.0.jar:/opt/bd-flink-module/bd-flink-module-1.0.0.jar --name click-event-generator brijeshdhaker/bd-flink-module:1.0.0 /bin/bash

java -classpath /opt/bd-flink-module/bd-flink-module-1.0.0.jar:/opt/flink/lib/* flink.playgrounds.ops.clickcount.ClickEventGenerator --bootstrap.servers kafkabroker.sandbox.net:9092 --topic click-event-source

java -classpath /opt/bd-flink-module-1.0.0.jar:/opt/flink/lib/* flink.playgrounds.ops.clickcount.ClickEventGenerator --bootstrap.servers kafkabroker.sandbox.net:9092 --topic click-event-source


docker run --rm -i -t --network sandbox.net --name click-event-generator brijeshdhaker/bd-flink-module:1.0.0 java -classpath /opt/bd-flink-module-1.0.0.jar:/opt/flink/lib/* flink.playgrounds.ops.clickcount.ClickEventGenerator --bootstrap.servers kafkabroker.sandbox.net:9092 --topic click-event-source
docker exec -it click-event-generator /bin/bash
docker logs -f click-event-generator

docker container stop click-event-generator
docker container rm click-event-generator

docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic click-event-source \
--bootstrap-server kafkabroker.sandbox.net:9092" \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config \
--timeout-ms 5000 2>/dev/null" 

```

```shell

docker run --rm -i -t --network sandbox.net \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v ./bd-hadoop-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v /apps:/apps \
--name click-event-counter \
brijeshdhaker/bd-flink-module:1.0.0 flink run -d /opt/bd-flink-module-1.0.0.jar --bootstrap.servers kafkabroker.sandbox.net:9092 --checkpointing --event-time --input-topic click-event-source --output-topic click-event-sink

docker run --rm -i -t --network sandbox.net \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v ./bd-hadoop-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v /apps:/apps \
--name click-event-counter \
brijeshdhaker/bd-flink-module:1.0.0 flink run -c flink.playgrounds.ops.clickcount.ClickEventCount /opt/bd-flink-module-1.0.0.jar --bootstrap.servers kafkabroker.sandbox.net:9092 --checkpointing --event-time --input-topic click-event-source --output-topic click-event-sink

docker run --rm -i -t --network sandbox.net \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v ./bd-hadoop-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v /apps:/apps \
--name click-event-counter \
brijeshdhaker/bd-flink-module:1.0.0 flink run -c flink.playgrounds.ops.clickcount.ClickEventCount /opt/bd-flink-module-1.0.0.jar --bootstrap.servers kafkabroker.sandbox.net:9092 --checkpointing --event-time --input-topic click-event-source --output-topic click-event-sink


docker run --rm -i -t --network sandbox.net \
-e JOB_MANAGER_RPC_ADDRESS=flink-jobmanager \
-e JOB_MANAGER_RPC_PORT=6123 \
-v ./bd-hadoop-sandbox/conf/flink/config.yaml:/opt/flink/conf/config.yaml \
-v /apps:/apps \
--name click-event-client brijeshdhaker/bd-flink-module:1.0.0 /bin/bash

flink list
flink stop 8ef92138adcdd40d7b65f82182d230f3

# Restart
flink run \
  --fromSavepoint s3a://warehouse-flink/execution/savepoints/savepoint-0e320c-0f5a00380968 \
  --detached  \
  --class flink.playgrounds.ops.clickcount.ClickEventCount /opt/bd-flink-module-1.0.0.jar \
  --bootstrap.servers kafkabroker.sandbox.net:9092 \
  --checkpointing \
  --event-time
  
docker compose -f bd-hadoop-sandbox/docker-compose.yml exec kafkabroker sh -c "kafka-console-consumer \
--topic click-event-sink \
--bootstrap-server kafkabroker.sandbox.net:9092" \
--consumer.config /apps/sandbox/kafka/cnf/client_plaintext.config

```