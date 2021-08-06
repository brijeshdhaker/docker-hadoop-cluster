#!/bin/bash

${HADOOP_HOME}/bin/hdfs dfs -rm -r /tmp
${HADOOP_HOME}/bin/hdfs dfs -rm -r /user/hive/warehouse
echo "HDFS dirs for hive successfully deleted ."

${HADOOP_HOME}/bin/hdfs dfs -mkdir       /tmp
${HADOOP_HOME}/bin/hdfs dfs -mkdir -p    /user/hive/warehouse
${HADOOP_HOME}/bin/hdfs dfs -mkdir g+w   /tmp
${HADOOP_HOME}/bin/hdfs dfs -chmod g+w   /user/hive/warehouse
echo "HDFS dirs for hive successfully created ."

cd ${HIVE_HOME}/bin

# Start Hive Server
echo "Starting Hive Server ...."

${HIVE_HOME}/bin/hiveserver2 --hiveconf hive.root.logger=INFO,console hive.server2.enable.doAs=false metastore.thrift.uris=thrift://hive-metastore:9083 	hive.metastore.uris=thrift://hive-metastore:9083 hive.metastore.uri.selection=SEQUENTIAL
echo "Hive server successfully started ."
