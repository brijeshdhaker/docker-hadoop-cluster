#!/bin/bash

#Hadoop Configurations
export HADOOP_HOME=/opt/hadoop
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export HADOOP_MAPRED_HOME=${HADOOP_HOME}
export HADOOP_COMMON_HOME=${HADOOP_HOME}
export HADOOP_HDFS_HOME=${HADOOP_HOME}
export YARN_HOME=${HADOOP_HOME}
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

#Hive Configurations
export HIVE_HOME=/opt/hive
export PATH=$PATH:$HIVE_HOME/sbin:$HIVE_HOME/bin
export CLASSPATH=$CLASSPATH:$HADOOP_HOME/lib/*:$HIVE_HOME/lib/*od g+w /user/hive/warehouse


if [ ! -f /tmp/hive-scratch ]; then
  echo "Creating warehouse  and scratch dir for hive"
  mkdir -p /user/hive/warehouse
  mkdir -p /tmp/hive-scratch/operation_logs
fi

## Create Hive Metastore Derby Database
if [ ! -d /user/hive/metastore/metastore_db ]; then

  echo "Create Metastore Derby Database for Hive ."
  ${HIVE_HOME}/bin/schematool -initSchema -dbType postgres

fi

${HIVE_HOME}/bin/hiveserver2

#