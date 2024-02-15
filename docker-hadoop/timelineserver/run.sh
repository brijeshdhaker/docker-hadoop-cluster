#!/bin/bash

/usr/bin/cp -Rf ${ETC_CONF_DIR}/*.xml ${HADOOP_HOME}/etc/hadoop/

timelinedir='/yarn/timeline'

if [ ! -d $timelinedir ]; then
  echo "timeline data directory not found: $timelinedir"
  exit 2
fi

$HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR timelineserver
