#!/bin/bash

. ${SPARK_HOME}/bin/load-spark-env.sh
echo "Starting Spark History Server ... "

. ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.history.HistoryServer --properties-file ${SPARK_HOME}/conf/spark-defaults.conf