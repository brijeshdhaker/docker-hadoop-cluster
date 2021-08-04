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
export CLASSPATH=$CLASSPATH:$HADOOP_HOME/lib/*:$HIVE_HOME/lib/*

#
if [ ! -f /apps/hostpath/hive/hive-scratch ]; then

  echo "Creating warehouse  and scratch dir for hive"
  mkdir -p /apps/hostpath/hive/warehouse
  mkdir -p /apps/hostpath/hive/metastore
  mkdir -p /apps/hostpath/hive/hive-scratch/operation_logs

fi

## Create hive metastore postgres database
if [ ! -f /apps/hostpath/hive/.already_metastore ]; then

  echo "Create hive metastore postgres database ."
  ${HIVE_HOME}/bin/schematool -initSchema -dbType postgres
  #
  touch /apps/hostpath/hive/.already_metastore
fi

# Start Hive Server
${HIVE_HOME}/bin/hiveserver2
echo "Hive Server Successfully started ."
#