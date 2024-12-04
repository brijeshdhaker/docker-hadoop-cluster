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