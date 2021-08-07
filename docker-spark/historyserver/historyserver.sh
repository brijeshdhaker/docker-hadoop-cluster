#!/bin/bash

export PUBLIC_DNS=$(hostname  -i)
echo "Running Spark HistoryServer on ${PUBLIC_DNS}."

source ${SPARK_HOME}/sbin/spark-config.sh
source ${SPARK_HOME}/bin/load-spark-env.sh

mkdir -p "$SPARK_MASTER_LOG"

ln -sf /dev/stdout "$SPARK_MASTER_LOG"/spark-history.out

#
${SPARK_HOME}/bin/spark-class org.apache.spark.deploy.history.HistoryServer --properties-file ${SPARK_HOME}/conf/spark.logserver.conf

#


