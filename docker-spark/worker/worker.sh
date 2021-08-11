#!/bin/bash

export PUBLIC_DNS=$(hostname  -i)
echo "Running Spark Worker on ${PUBLIC_DNS}."

source ${SPARK_HOME}/sbin/spark-config.sh
source ${SPARK_HOME}/bin/load-spark-env.sh

mkdir -p "${SPARK_WORKER_LOG}"

ln -sf /dev/stdout "${SPARK_WORKER_LOG}"/spark-worker.out

${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.worker.Worker --webui-port "$WORKER_WEBUI_PORT" "$SPARK_MASTER" >> "$SPARK_WORKER_LOG"/spark-worker.out