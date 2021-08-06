#!/bin/sh
. /start-common.sh

#

case "${NODE_TYPE}" in
  MASTER)
    ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.master.Master --ip $(hostname -i) --port 7077 --webui-port 8080
  ;;
  WORKER)
    ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.worker.Worker spark://spark-master:7077 --webui-port 8081
  ;;
  LOGSERVER)
    ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.history.HistoryServer --properties-file ${SPARK_HOME}/conf/spark.logserver.conf
  ;;
  HIVESERVER)
    ${SPARK_HOME}/bin/spark-class org.apache.spark.sql.hive.thriftserver.HiveThriftServer2 --properties-file ${SPARK_HOME}/conf/spark.logserver.conf
  ;;  
  *)
    exit 1
  ;;
esac
