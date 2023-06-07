#!/bin/bash

if [ ! -f /apps/sandbox/hive/.already_setup ]; then
  ${HADOOP_HOME}/bin/hdfs dfs -mkdir       /tmp
  ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p    /user/hive/warehouse
  echo "HDFS dirs for hive successfully created ."

  ${HADOOP_HOME}/bin/hdfs dfs -mkdir g+w   /tmp
  ${HADOOP_HOME}/bin/hdfs dfs -chmod g+w   /user/hive/warehouse
  echo "HDFS dirs permissions successfully updated."
  touch /apps/sandbox/hive/.already_setup
fi

# For enabling hive to use the Tez engine
#if [ -z "${HIVE_AUX_JARS_PATH}" ]; then
#  export HIVE_AUX_JARS_PATH="${TEZ_JARS}"
#else
#  export HIVE_AUX_JARS_PATH="${HIVE_AUX_JARS_PATH}:${TEZ_JARS}"
#fi

export HADOOP_CLASSPATH="$(${HADOOP_HOME}/bin/hadoop classpath):${TEZ_CONF_DIR}:$(find ${TEZ_HOME} -name "*.jar" | paste -sd ":")"

# Start Hive Server
cd ${HIVE_HOME}/bin
echo "Starting Hive Server ...."
${HIVE_HOME}/bin/hiveserver2 --hiveconf hive.root.logger=INFO,console --hiveconf hive.server2.enable.doAs=false
echo "Hive server successfully started ."



# metastore.thrift.uris=thrift://hive-metastore:9083
# hive.metastore.uris=thrift://hive-metastore:9083
# hive.metastore.uri.selection=SEQUENTIAL

