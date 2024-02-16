#!/bin/bash

if [ ! -f /apps/sandbox/hive/.already_setup ]; then

  ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /tmp
  ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /warehouse/tablespace/managed/hive
  ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /warehouse/tablespace/external/hive
  echo "HDFS dirs for hive successfully created ."

  ${HADOOP_HOME}/bin/hdfs dfs -mkdir g+w   /tmp
  ${HADOOP_HOME}/bin/hdfs dfs -chmod g+w   /warehouse/tablespace/external/hive
  ${HADOOP_HOME}/bin/hdfs dfs -chmod g+w   /warehouse/tablespace/managed/hive
  echo "HDFS dirs permissions successfully updated."
  touch /apps/sandbox/hive/.already_setup

fi

# For enabling hive to use the Tez engine
export HADOOP_CLASSPATH="$(${HADOOP_HOME}/bin/hadoop classpath):${TEZ_CONF_DIR}:$(find ${TEZ_HOME} -name "*.jar" | paste -sd ":")"


# Start Hive Server
echo "Starting Hive Server ...."
${HIVE_HOME}/bin/hiveserver2 --hiveconf hive.root.logger=INFO,console
echo "Hive server successfully started ."

# metastore.thrift.uris=thrift://metastore.sandbox.net:9083
# hive.metastore.uris=thrift://metastore.sandbox.net:9083
# hive.metastore.uri.selection=SEQUENTIAL

