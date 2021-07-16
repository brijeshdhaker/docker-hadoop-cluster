#!/bin/bash
# We should format namenode only once. So, when we format it
# we save a marker file.
                                                                                                                               
if [ ! -f /apps/hostpath/hadoop/data/.already_formatted ]; then
  echo "Formatting  Name Node"                                                                                                                         
  mkdir -p /apps/hostpath/hadoop/data/dfs/data
  mkdir -p /apps/hostpath/hadoop/data/dfs/name
  mkdir -p /apps/hostpath/hadoop/data/dfs/namesecondary
                                                                                                                                                       
  hdfs namenode -format                                                                                                                                
  touch /apps/hostpath/hadoop/data/.already_formatted
fi

#Check whether they are equal 
if [ "$HDFS_MODE" == "SingleNode" ] 
then
	# HDFS on a SingleNode
	echo "Starting HDFS Single Node cluster."
	hdfs namenode &
	#
	hdfs datanode
else
	# HDFS on a MultiNode
	echo "Starting HDFS Multinode Node cluster."
    hdfs namenode	
fi
