#!/bin/bash

#
#
#

mkdir -p /apps/hostpath/
mkdir -p /apps/hostpath/hadoop/yarn
mkdir -p /apps/hostpath/spark
mkdir -p /apps/hostpath/postgres
mkdir -p /apps/hostpath/hive
mkdir -p /apps/hostpath/hive
mkdir -p /apps/hostpath/hive
mkdir -p /apps/hostpath/notebook
#
#
mkdir -p /apps/hostpath/zeppelin/notebook
mkdir -p /apps/hostpath/zeppelin/logs
#
#
mkdir -p /apps/hostpath/drivers
mkdir -p /apps/hostpath/datasets
#
#
#
mkdir -p /apps/hostpath/localstack
#
#
#
mkdir -p /apps/hostpath/kafka
mkdir -p /apps/hostpath/kafka/data
mkdir -p /apps/hostpath/kafka/schema

#
#
#
if [ -f /apps/hostpath/hadoop ]; then
  mkdir -p /apps/hostpath/hadoop/yarn
  mkdir -p /apps/hostpath/hadoop/dfs
fi

#
#
#
if [ -f /apps/hostpath/hadoop ]; then
  mkdir -p /apps/hostpath/hadoop/yarn
  mkdir -p /apps/hostpath/hadoop/dfs
fi