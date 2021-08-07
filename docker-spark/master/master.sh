#!/bin/bash

export PUBLIC_DNS=$(hostname  -i)
echo "Running Spark Master on ${PUBLIC_DNS}."

source ${SPARK_HOME}/sbin/spark-config.sh
source ${SPARK_HOME}/bin/load-spark-env.sh

mkdir -p "${SPARK_WORKER_LOG}"

ln -sf /dev/stdout "${SPARK_WORKER_LOG}"/spark-worker.out

${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.master.Master --ip ${PUBLIC_DNS} --port 7077 --webui-port 8080