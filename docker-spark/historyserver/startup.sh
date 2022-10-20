#!/bin/bash

export PUBLIC_DNS=$(hostname  -i)
echo "Running Spark HistoryServer on ${PUBLIC_DNS}."

source ${SPARK_HOME}/sbin/spark-config.sh
source ${SPARK_HOME}/bin/load-spark-env.sh

#
export PATH=${PATH}:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

#
if [ ! -f /apps/sandbox/spark/.already_setup ]; then

  ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p    /tmp/spark/logs
  echo "Spark History Server log dirs successfully created ."

  ${HADOOP_HOME}/bin/hdfs dfs -chmod g+w   /tmp/spark/logs
  echo "Spark History Server log dirs permissions successfully updated."


  touch /apps/sandbox/spark/.already_setup

fi

mkdir -p /spark/logs

ln -sf /dev/stdout /spark/logs/spark-history.out

#
${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.history.HistoryServer --properties-file ${SPARK_HOME}/conf/spark.logserver.conf

#


