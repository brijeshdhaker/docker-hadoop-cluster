#!/bin/bash

namedir=`echo $HDFS_CONF_dfs_namenode_name_dir | perl -pe 's#file://##'`

if [ ! -d $namedir ]; then
  echo "Namenode name directory not found: $namedir"
  exit 2
fi

if [ -z "$CLUSTER_NAME" ]; then
  echo "Cluster name not specified"
  exit 2
fi

echo "remove lost+found from $namedir"
#rm -r $namedir/lost+found

if [ ! -f /apps/hostpath/hadoop/.already_formatted ]; then

  echo "Formatting namenode name directory: $namedir"
  mkdir -p /apps/hostpath/hadoop/dfs/data
  mkdir -p /apps/hostpath/hadoop/dfs/name
  mkdir -p /apps/hostpath/hadoop/dfs/namesecondary

  $HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode -format $CLUSTER_NAME
  touch /apps/hostpath/hadoop/.already_formatted

fi

#
#if [ "`ls -A $namedir`" == "" ]; then
#
#  echo "Formatting namenode name directory: $namedir"
#  $HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode -format $CLUSTER_NAME
#
#fi

$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode