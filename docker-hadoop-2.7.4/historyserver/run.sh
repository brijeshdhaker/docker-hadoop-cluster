#!/bin/bash

$HADOOP_HOME/bin/hdfs dfs -mkdir -p /apps/hadoop-2.7.4/yarn/timeline
$HADOOP_HOME/bin/hdfs dfs -chmod -R 1777 /apps/hadoop-2.7.4/yarn/timeline

$HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR historyserver
