#!/bin/bash

/usr/bin/cp -Rf ${HADOOP_ETC_CONF}/*.xml ${HADOOP_HOME}/etc/hadoop/

$HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR resourcemanager
