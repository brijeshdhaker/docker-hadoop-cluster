#!/bin/bash

$HADOOP_HOME/bin/hdfs dfs -mkdir -p /apps/hostpath/hadoop/yarn/timeline
$HADOOP_HOME/bin/hdfs dfs -chmod -R 1777 /apps/hostpath/hadoop/yarn/timeline

$HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR historyserver
