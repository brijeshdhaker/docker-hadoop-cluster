#!/bin/bash


if [ ! -f /apps/hostpath/hive/2.3.9/.already_setup ]; then

  ${HADOOP_HOME}/bin/hdfs dfs -mkdir       /tmp
  ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p    /user/hive/warehouse
  echo "HDFS dirs for hive successfully created ."

  ${HADOOP_HOME}/bin/hdfs dfs -mkdir g+w   /tmp
  ${HADOOP_HOME}/bin/hdfs dfs -chmod g+w   /user/hive/warehouse
  echo "HDFS dirs permissions successfully updated."

  touch /apps/hostpath/hive/2.3.9/.already_setup

fi


cd ${HIVE_HOME}/bin

# Start Hive Server
echo "Starting Hive Server ...."

${HIVE_HOME}/bin/hiveserver2 --hiveconf hive.root.logger=INFO,console hive.server2.enable.doAs=false
echo "Hive server successfully started ."

# metastore.thrift.uris=thrift://hive-metastore:9083
# hive.metastore.uris=thrift://hive-metastore:9083
# hive.metastore.uri.selection=SEQUENTIAL

