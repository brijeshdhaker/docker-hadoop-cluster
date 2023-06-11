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

if [ ! -f /hadoop/dfs/.already_formatted ]; then

  echo "Formatting name node name directory: $namedir"
  mkdir -p /hadoop/dfs/data
  mkdir -p /hadoop/dfs/name
  mkdir -p /hadoop/dfs/secondary

  $HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode -format $CLUSTER_NAME -force
  touch /hadoop/dfs/.already_formatted

fi

$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode
