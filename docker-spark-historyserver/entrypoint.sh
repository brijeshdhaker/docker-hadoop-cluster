#!/bin/bash

if [ ! -d ${HISTORY_SERVER_LOGDIR} ]; then

  mkdir -p   ${HISTORY_SERVER_LOGDIR}
  chmod g+w  ${HISTORY_SERVER_LOGDIR}
  echo "Spark History Server Log Dir ${HISTORY_SERVER_LOGDIR} "
  echo "Spark History Server log dirs created and permissions updated successfully."

fi

. ${SPARK_HOME}/bin/load-spark-env.sh
echo "Starting Spark History Server ... "


. ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.history.HistoryServer --properties-file ${SPARK_HOME}/conf/spark-defaults.conf