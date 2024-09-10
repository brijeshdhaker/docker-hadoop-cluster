
docker run -u $(id -u) -p 8080:8080 --rm -v $PWD/logs:/logs -v $PWD/notebook:/notebook \
-v /usr/lib/spark-3.5.1:/opt/spark -v /usr/lib/flink-1.18.1:/opt/flink \
-e FLINK_HOME=/opt/flink -e SPARK_HOME=/opt/spark \
-e ZEPPELIN_LOG_DIR='/logs' -e ZEPPELIN_NOTEBOOK_DIR='/notebook' --name zeppelin apache/zeppelin:0.11.1