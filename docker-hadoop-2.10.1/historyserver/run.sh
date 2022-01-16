#!/bin/bash

$HADOOP_HOME/bin/hdfs dfs -mkdir -p /yarn/timeline
$HADOOP_HOME/bin/hdfs dfs -chmod -R 1777 /yarn/timeline

$HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR historyserver
