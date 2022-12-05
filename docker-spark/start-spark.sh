#!/bin/bash
#start-spark.sh

. "${SPARK_HOME}/bin/load-spark-env.sh"
# When the spark work_load is master run class org.apache.spark.deploy.master.Master
if [ "$SPARK_WORKLOAD" == "master" ];
then

export SPARK_MASTER_HOST=$(hostname  -i)
echo "Running Spark Master on ${SPARK_MASTER_HOST}."

cd ${SPARK_HOME}/bin && ./spark-class org.apache.spark.deploy.master.Master --ip $SPARK_MASTER_HOST --port $SPARK_MASTER_PORT --webui-port $SPARK_MASTER_WEBUI_PORT >> $SPARK_MASTER_LOG

elif [ "$SPARK_WORKLOAD" == "worker" ];
then

export WORKER_HOST=$(hostname  -i)
echo "Running Spark Worker on ${WORKER_HOST}."

# When the spark work_load is worker run class org.apache.spark.deploy.master.Worker
cd ${SPARK_HOME}/bin && ./spark-class org.apache.spark.deploy.worker.Worker --webui-port $SPARK_WORKER_WEBUI_PORT $SPARK_MASTER >> $SPARK_WORKER_LOG

elif [ "$SPARK_WORKLOAD" == "HistoryServer" ];
then
    echo "SPARK HistoryServer"
    #
    export PATH=${PATH}:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin
    #
    ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p    /tmp/spark/logs
    echo "Spark History Server log dirs successfully created ."

    ${HADOOP_HOME}/bin/hdfs dfs -chmod g+w   /tmp/spark/logs
    echo "Spark History Server log dirs permissions successfully updated."

    cd ${SPARK_HOME}/bin && ./spark-class org.apache.spark.deploy.history.HistoryServer --properties-file ${SPARK_HOME}/conf/spark.logserver.conf

elif [ "$SPARK_WORKLOAD" == "submit" ];
then
    echo "SPARK SUBMIT"
else
    echo "Undefined Workload Type $SPARK_WORKLOAD, must specify: master, worker, submit"
fi

