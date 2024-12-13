#!/bin/bash

. "${SPARK_HOME}/bin/load-spark-env.sh"

# When the spark work_load is master run class org.apache.spark.deploy.master.Master
if [ "${SPARK_WORKLOAD}" == "master" ];
then
    export SPARK_MASTER_HOST=$(hostname  -i)
    echo "Starting Spark master on ${SPARK_MASTER_HOST}."
    ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.master.Master --ip ${SPARK_MASTER_HOST} --port ${SPARK_MASTER_PORT} --webui-port ${SPARK_MASTER_WEBUI_PORT} >> ${SPARK_MASTER_LOG}-$(date +%s).out
#
elif [ "${SPARK_WORKLOAD}" == "worker" ];
then
    export WORKER_HOST=$(hostname  -i)
    echo "Starting Spark worker on $(hostname  -i)."
    # When the spark work_load is worker run class org.apache.spark.deploy.master.Worker
    ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.worker.Worker --webui-port ${SPARK_WORKER_WEBUI_PORT} ${SPARK_MASTER} >> ${SPARK_WORKER_LOG}-$(date +%s).out
#
elif [ "${SPARK_WORKLOAD}" == "HistoryServer" ];
then
    echo "Spark HistoryServer"
    ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.history.HistoryServer --properties-file ${SPARK_HISTORY_CONF_FILE} >> ${SPARK_HS_LOG}-$(date +%s).out
#
elif [ "${SPARK_WORKLOAD}" == "ThriftServer" ];
then
    echo "Spark ThriftServer"
    ${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.SparkSubmit --class org.apache.spark.sql.hive.thriftserver.HiveThriftServer2 spark-internal >> ${SPARK_TS_LOG}-$(date +%s).out
#
elif [ "${SPARK_WORKLOAD}" == "submit" ];
then
    echo "Spark Submit"
else
    echo "Undefined Workload Type ${SPARK_WORKLOAD}, must specify: master, worker, HistoryServer, ThriftServer, submit"
fi

