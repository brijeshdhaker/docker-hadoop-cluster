#!/bin/bash

# wait for hdfs to be up & running
sleep 30

${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /mr-history/tmp
${HADOOP_HOME}/bin/hdfs dfs -chmod -R 1777 /mr-history/tmp

${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /mr-history/done
${HADOOP_HOME}/bin/hdfs dfs -chmod -R 1777 /mr-history/done

${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /app-logs
${HADOOP_HOME}/bin/hdfs dfs -chmod -R 1777 /app-logs

${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /tmp/hadoop-yarn/staging
${HADOOP_HOME}/bin/hdfs dfs -chmod -R 1777 /tmp/hadoop-yarn/staging

${HADOOP_HOME}/sbin/mr-jobhistory-daemon.sh --config ${HADOOP_HOME}/etc/hadoop start historyserver

${HADOOP_HOME}/bin/yarn resourcemanager