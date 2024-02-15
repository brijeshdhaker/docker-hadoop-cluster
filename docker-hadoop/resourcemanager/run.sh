#!/bin/bash

/usr/bin/cp -Rf ${ETC_CONF_DIR}/*.xml ${HADOOP_HOME}/etc/hadoop/

$HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR resourcemanager
