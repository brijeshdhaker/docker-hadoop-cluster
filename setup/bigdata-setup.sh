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
sudo chown brijeshdhaker:brijeshdhaker -R /apps
sudo chmod 777 -R /apps

sudo mkdir -p /opt/sandbox
sudo chown brijeshdhaker:brijeshdhaker -R /opt/sandbox
sudo chmod 777 -R /opt/sandbox

#
mkdir -p /apps/hostpath/sandbox3/zookeeper/snode
mkdir -p /apps/hostpath/sandbox3/zookeeper/data
mkdir -p /apps/hostpath/sandbox3/zookeeper/secrets
mkdir -p /apps/hostpath/sandbox3/zookeeper/logs
#
mkdir -p /apps/hostpath/sandbox3/hadoop/dfs/data
mkdir -p /apps/hostpath/sandbox3/hadoop/dfs/name
mkdir -p /apps/hostpath/sandbox3/hadoop/dfs/namesecondary

mkdir -p /apps/hostpath/sandbox3/postgres
mkdir -p /apps/hostpath/sandbox3/hive
#
mkdir -p /apps/hostpath/drivers
mkdir -p /apps/hostpath/datasets
#
mkdir -p /apps/hostpath/localstack


sudo chown brijeshdhaker:brijeshdhaker -R /apps
sudo chmod 777 -R /apps
