#!/bin/bash

/usr/bin/cp -Rf ${ETC_CONF_DIR}/*.xml ${HADOOP_HOME}/etc/hadoop/

datadir=`echo $HDFS_SITE_dfs_datanode_data_dir | perl -pe 's#file://##'`

if [ ! -d $datadir ]; then
  echo "Datanode data directory not found: $datadir"
  exit 2
fi

$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR datanode
