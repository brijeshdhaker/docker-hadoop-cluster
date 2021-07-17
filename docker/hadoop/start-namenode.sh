#!/bin/bash

# We should format namenode only once. So, when we format it
# we save a marker file.

if [ ! -f /apps/hostpath/hadoop/.already_formatted ]; then
  echo "Formatting  Name Node"
  mkdir -p /apps/hostpath/hadoop/dfs/data
  mkdir -p /apps/hostpath/hadoop/dfs/name
  mkdir -p /apps/hostpath/hadoop/dfs/namesecondary

  hdfs namenode -format
  touch /apps/hostpath/hadoop/.already_formatted
fi

hdfs namenode