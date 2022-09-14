#!/bin/bash

#
# Docker network
#
docker network create -d bridge bigdata.net
docker network create -d bridge sandbox-bigdata.net

#
# Linux Docker Volumes
#
docker volume create --name sandbox3_apps_hostpath --opt type=none --opt device=/apps/hostpath --opt o=bind
docker volume create --name sandbox3_m2 --opt type=none --opt device=/apps/hostpath/.m2 --opt o=bind
docker volume create --name sandbox3_ivy2 --opt type=none --opt device=/apps/hostpath/.ivy2 --opt o=bind

docker volume create --name sandbox3_zookeeper_secrets --opt type=none --opt device=/apps/hostpath/sandbox3/zookeeper/secrets --opt o=bind
docker volume create --name sandbox3_zookeeper_data --opt type=none --opt device=/apps/hostpath/sandbox3/zookeeper/data --opt o=bind
docker volume create --name sandbox3_zookeeper_log --opt type=none --opt device=/apps/hostpath/sandbox3/zookeeper/log --opt o=bind

docker volume create --name sandbox3_kafka_secrets --opt type=none --opt device=/apps/hostpath/sandbox3/kafka-broker/secrets --opt o=bind
docker volume create --name sandbox3_kafka_data --opt type=none --opt device=/apps/hostpath/sandbox3/kafka-broker/data --opt o=bind
docker volume create --name sandbox3_kafka_log --opt type=none --opt device=/apps/hostpath/sandbox3/kafka-broker/log --opt o=bind

docker volume create --name sandbox3_schemaregistry_secrets --opt type=none --opt device=/apps/hostpath/sandbox3/schema-registry/secrets --opt o=bind
docker volume create --name sandbox3_schemaregistry_data --opt type=none --opt device=/apps/hostpath/sandbox3/schema-registry/data --opt o=bind
docker volume create --name sandbox3_schemaregistry_log --opt type=none --opt device=/apps/hostpath/sandbox3/schema-registry/log --opt o=bind

docker volume create --name sandbox3_cassandra_data --opt type=none --opt device=/apps/hostpath/sandbox3/cassandra/data --opt o=bind
docker volume create --name sandbox3_cassandra_conf --opt type=none --opt device=/apps/hostpath/sandbox3/cassandra/conf --opt o=bind

docker volume create --name sandbox3_mysql_data --opt type=none --opt device=/apps/hostpath/sandbox3/mysql/data --opt o=bind
docker volume create --name sandbox3_mysql_conf --opt type=none --opt device=/apps/hostpath/sandbox3/mysql/conf --opt o=bind

docker volume create --name sandbox3_hadoop_dfs_name --opt type=none --opt device=/apps/hostpath/sandbox3/hadoop/dfs/name --opt o=bind
docker volume create --name sandbox3_hadoop_dfs_data --opt type=none --opt device=/apps/hostpath/sandbox3/hadoop/dfs/data --opt o=bind
docker volume create --name sandbox3_hadoop_yarn_history --opt type=none --opt device=/apps/hostpath/sandbox3/hadoop/yarn/history --opt o=bind

docker volume create --name sandbox3_postgres --opt type=none --opt device=/apps/hostpath/sandbox3/postgres --opt o=bind
docker volume create --name sandbox3_postgres_data --opt type=none --opt device=/apps/hostpath/sandbox3/postgres/data --opt o=bind
docker volume create --name sandbox3_postgres_conf --opt type=none --opt device=/apps/hostpath/sandbox3/postgres/conf --opt o=bind
docker volume create --name sandbox3_postgres_init --opt type=none --opt device=/apps/hostpath/sandbox3/postgres/init.d --opt o=bind

docker volume create --name sandbox3_zeppelin --opt type=none --opt device=/apps/hostpath/sandbox3/zeppelin --opt o=bind
docker volume create --name sandbox3_zeppelin_conf --opt type=none --opt device=/apps/hostpath/sandbox3/zeppelin/conf --opt o=bind

docker volume create --name sandbox3_nifi_conf --opt type=none --opt device=/apps/hostpath/sandbox3/nifi/conf --opt o=bind
docker volume create --name sandbox3_nifi_content_repository --opt type=none --opt device=/apps/hostpath/sandbox3/nifi/content_repository --opt o=bind
docker volume create --name sandbox3_nifi_database_repository --opt type=none --opt device=/apps/hostpath/sandbox3/nifi/database_repository --opt o=bind
docker volume create --name sandbox3_nifi_flowfile_repository --opt type=none --opt device=/apps/hostpath/sandbox3/nifi/flowfile_repository --opt o=bind
docker volume create --name sandbox3_nifi_provenance_repository --opt type=none --opt device=/apps/hostpath/sandbox3/nifi/provenance_repository --opt o=bind
docker volume create --name sandbox3_nifi_log --opt type=none --opt device=/apps/hostpath/sandbox3/nifi/logs --opt o=bind
docker volume create --name sandbox3_nifi_state --opt type=none --opt device=/apps/hostpath/sandbox3/nifi/state --opt o=bind

docker volume create --name sandbox3_hadoop --opt type=none --opt device=/opt/sandbox3/hadoop-3.2.1 --opt o=bind
docker volume create --name sandbox3_hbase --opt type=none --opt device=/opt/sandbox3/hbase-2.4.9 --opt o=bind
docker volume create --name sandbox3_hbase_client --opt type=none --opt device=/opt/sandbox3/hbase-1.1.7 --opt o=bind
docker volume create --name sandbox3_hive --opt type=none --opt device=/opt/sandbox3/hive-3.1.2 --opt o=bind
docker volume create --name sandbox3_spark --opt type=none --opt device=/opt/sandbox3/spark-3.1.2 --opt o=bind

#
# Windows Docker Volumes
#
docker volume create --name sandbox3_apps_hostpath --opt type=none --opt device=/d/apps/hostpath --opt o=bind
docker volume create --name sandbox3_m2 --opt type=none --opt device=/d/apps/hostpath/.m2 --opt o=bind
docker volume create --name sandbox3_ivy2 --opt type=none --opt device=/d/apps/hostpath/.ivy2 --opt o=bind

docker volume create --name sandbox3_zookeeper_secrets --opt type=none --opt device=/d/apps/hostpath/sandbox3/zookeeper/secrets --opt o=bind
docker volume create --name sandbox3_zookeeper_data --opt type=none --opt device=/d/apps/hostpath/sandbox3/zookeeper/data --opt o=bind
docker volume create --name sandbox3_zookeeper_log --opt type=none --opt device=/d/apps/hostpath/sandbox3/zookeeper/log --opt o=bind

docker volume create --name sandbox3_kafka_secrets --opt type=none --opt device=/d/apps/hostpath/sandbox3/kafka-broker/secrets --opt o=bind
docker volume create --name sandbox3_kafka_data --opt type=none --opt device=/d/apps/hostpath/sandbox3/kafka-broker/data --opt o=bind
docker volume create --name sandbox3_kafka_log --opt type=none --opt device=/d/apps/hostpath/sandbox3/kafka-broker/log --opt o=bind

docker volume create --name sandbox3_schemaregistry_secrets --opt type=none --opt device=/d/apps/hostpath/sandbox3/schema-registry/secrets --opt o=bind
docker volume create --name sandbox3_schemaregistry_data --opt type=none --opt device=/d/apps/hostpath/sandbox3/schema-registry/data --opt o=bind
docker volume create --name sandbox3_schemaregistry_log --opt type=none --opt device=/d/apps/hostpath/sandbox3/schema-registry/log --opt o=bind

docker volume create --name sandbox3_cassandra_data --opt type=none --opt device=/d/apps/hostpath/sandbox3/cassandra/data --opt o=bind
docker volume create --name sandbox3_cassandra_conf --opt type=none --opt device=/d/apps/hostpath/sandbox3/cassandra/conf --opt o=bind

docker volume create --name sandbox3_mysql_data --opt type=none --opt device=/d/apps/hostpath/sandbox3/mysql/data --opt o=bind
docker volume create --name sandbox3_mysql_conf --opt type=none --opt device=/d/apps/hostpath/sandbox3/mysql/conf --opt o=bind

docker volume create --name sandbox3_hadoop_dfs_name --opt type=none --opt device=/d/apps/hostpath/sandbox3/hadoop/dfs/name --opt o=bind
docker volume create --name sandbox3_hadoop_dfs_data --opt type=none --opt device=/d/apps/hostpath/sandbox3/hadoop/dfs/data --opt o=bind
docker volume create --name sandbox3_hadoop_yarn_history --opt type=none --opt device=/d/apps/hostpath/sandbox3/hadoop/yarn/history --opt o=bind

docker volume create --name sandbox3_postgres --opt type=none --opt device=/d/apps/hostpath/sandbox3/postgres --opt o=bind
docker volume create --name sandbox3_postgres_data --opt type=none --opt device=/d/apps/hostpath/sandbox3/postgres/data --opt o=bind
docker volume create --name sandbox3_postgres_conf --opt type=none --opt device=/d/apps/hostpath/sandbox3/postgres/conf --opt o=bind
docker volume create --name sandbox3_postgres_init --opt type=none --opt device=/d/apps/hostpath/sandbox3/postgres/init.d --opt o=bind

docker volume create --name sandbox3_zeppelin --opt type=none --opt device=/d/apps/hostpath/sandbox3/zeppelin --opt o=bind
docker volume create --name sandbox3_zeppelin_conf --opt type=none --opt device=/d/apps/hostpath/sandbox3/zeppelin/conf --opt o=bind

docker volume create --name sandbox3_nifi_conf --opt type=none --opt device=/d/apps/hostpath/sandbox3/nifi/conf --opt o=bind
docker volume create --name sandbox3_nifi_content_repository --opt type=none --opt device=/d/apps/hostpath/sandbox3/nifi/content_repository --opt o=bind
docker volume create --name sandbox3_nifi_database_repository --opt type=none --opt device=/d/apps/hostpath/sandbox3/nifi/database_repository --opt o=bind
docker volume create --name sandbox3_nifi_flowfile_repository --opt type=none --opt device=/d/apps/hostpath/sandbox3/nifi/flowfile_repository --opt o=bind
docker volume create --name sandbox3_nifi_provenance_repository --opt type=none --opt device=/d/apps/hostpath/sandbox3/nifi/provenance_repository --opt o=bind
docker volume create --name sandbox3_nifi_log --opt type=none --opt device=/d/apps/hostpath/sandbox3/nifi/logs --opt o=bind
docker volume create --name sandbox3_nifi_state --opt type=none --opt device=/d/apps/hostpath/sandbox3/nifi/state --opt o=bind

docker volume create --name sandbox3_hadoop --opt type=none --opt device=/d/opt/sandbox3/hadoop-3.2.1 --opt o=bind
docker volume create --name sandbox3_hbase --opt type=none --opt device=/d/opt/sandbox3/hbase-2.4.9 --opt o=bind
docker volume create --name sandbox3_hbase_client --opt type=none --opt device=/d/opt/sandbox3/hbase-1.1.7 --opt o=bind
docker volume create --name sandbox3_hive --opt type=none --opt device=/d/opt/sandbox3/hive-3.1.2 --opt o=bind
docker volume create --name sandbox3_spark --opt type=none --opt device=/d/opt/sandbox3/spark-3.1.2 --opt o=bind



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
