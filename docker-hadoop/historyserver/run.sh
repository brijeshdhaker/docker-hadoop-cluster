#!/bin/bash

/usr/bin/cp -Rf ${ETC_CONF_DIR}/*.xml ${HADOOP_HOME}/etc/hadoop/

$HADOOP_HOME/bin/mapred --config $HADOOP_CONF_DIR historyserver
