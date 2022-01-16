#!/bin/bash

#
# Docker network
#
docker network create -d bridge bigdata.net
docker network create -d bridge sandbox-bigdata.net
#
# Require Dirs
#
sudo mkdir -p /apps

#
mkdir -p /apps/hostpath/zookeeper/snode
mkdir -p /apps/hostpath/zookeeper/cluster/data_1
mkdir -p /apps/hostpath/zookeeper/cluster/data_2
mkdir -p /apps/hostpath/zookeeper/cluster/data_3
mkdir -p /apps/hostpath/zookeeper/logs
#
mkdir -p /apps/hostpath/hadoop/2.7.4/dfs/data
mkdir -p /apps/hostpath/hadoop/2.7.4/dfs/name
mkdir -p /apps/hostpath/hadoop/2.7.4/dfs/namesecondary

mkdir -p /apps/hostpath/postgres/hive-2.3.7
mkdir -p /apps/hostpath/hive/2.3.7
#
mkdir -p /apps/hostpath/zeppelin/notebook
mkdir -p /apps/hostpath/zeppelin/logs
#
mkdir -p /apps/hostpath/drivers
mkdir -p /apps/hostpath/datasets
#
mkdir -p /apps/hostpath/localstack
#
mkdir -p /apps/hostpath/kafka
mkdir -p /apps/hostpath/kafka/snode
mkdir -p /apps/hostpath/kafka/cluster/data_1
mkdir -p /apps/hostpath/kafka/cluster/data_2
mkdir -p /apps/hostpath/kafka/cluster/data_3
mkdir -p /apps/hostpath/kafka/schema

sudo chown brijeshdhaker:brijeshdhaker -R /apps
sudo chmod 777 -R /apps
