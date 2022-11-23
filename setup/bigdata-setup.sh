#!/bin/bash

#
# Docker network
#
docker network create -d bridge bigdata.net

docker network create -d bridge sandbox-bigdata.net

#
# Linux Docker Volumes
#
docker volume create --name sandbox_host_path --opt type=none --opt device=/apps/hostpath --opt o=bind
docker volume create --name sandbox_base_path --opt type=none --opt device=/apps/sandbox --opt o=bind

docker volume create --name sandbox_m2 --opt type=none --opt device=/apps/hostpath/.m2 --opt o=bind
docker volume create --name sandbox_ivy2 --opt type=none --opt device=/apps/hostpath/.ivy2 --opt o=bind

docker volume create --name sandbox_zookeeper_secrets --opt type=none --opt device=/apps/sandbox/zookeeper/secrets --opt o=bind
docker volume create --name sandbox_zookeeper_data --opt type=none --opt device=/apps/sandbox/zookeeper/data --opt o=bind
docker volume create --name sandbox_zookeeper_log --opt type=none --opt device=/apps/sandbox/zookeeper/log --opt o=bind

docker volume create --name sandbox_kafka_secrets --opt type=none --opt device=/apps/sandbox/kafka-broker/secrets --opt o=bind
docker volume create --name sandbox_kafka_data --opt type=none --opt device=/apps/sandbox/kafka-broker/data --opt o=bind
docker volume create --name sandbox_kafka_log --opt type=none --opt device=/apps/sandbox/kafka-broker/log --opt o=bind

docker volume create --name sandbox_schema_registry_secrets --opt type=none --opt device=/apps/sandbox/schema-registry/secrets --opt o=bind
docker volume create --name sandbox_schema_registry_data --opt type=none --opt device=/apps/sandbox/schema-registry/data --opt o=bind
docker volume create --name sandbox_schema_registry_log --opt type=none --opt device=/apps/sandbox/schema-registry/log --opt o=bind

docker volume create --name sandbox_cassandra_data --opt type=none --opt device=/apps/sandbox/cassandra/data --opt o=bind
docker volume create --name sandbox_cassandra_conf --opt type=none --opt device=/apps/sandbox/cassandra/conf --opt o=bind

docker volume create --name sandbox_mysql_data --opt type=none --opt device=/apps/sandbox/mysql/data --opt o=bind
docker volume create --name sandbox_mysql_conf --opt type=none --opt device=/apps/sandbox/mysql/conf --opt o=bind

docker volume create --name sandbox_hadoop_data --opt type=none --opt device=/apps/sandbox/hadoop --opt o=bind
docker volume create --name sandbox_hadoop_dfs_name --opt type=none --opt device=/apps/sandbox/hadoop/dfs/name --opt o=bind
docker volume create --name sandbox_hadoop_dfs_data --opt type=none --opt device=/apps/sandbox/hadoop/dfs/data --opt o=bind

docker volume create --name sandbox_yarn_history --opt type=none --opt device=/apps/sandbox/hadoop/yarn/history --opt o=bind

docker volume create --name sandbox_hive_data --opt type=none --opt device=/apps/sandbox/hive --opt o=bind

docker volume create --name sandbox_postgres_data --opt type=none --opt device=/apps/sandbox/postgres/data --opt o=bind
docker volume create --name sandbox_postgres_conf --opt type=none --opt device=/apps/sandbox/postgres/conf --opt o=bind
docker volume create --name sandbox_postgres_init --opt type=none --opt device=/apps/sandbox/postgres/init.d --opt o=bind

docker volume create --name sandbox_zeppelin --opt type=none --opt device=/apps/sandbox/zeppelin --opt o=bind
docker volume create --name sandbox_zeppelin_conf --opt type=none --opt device=/apps/sandbox/zeppelin/conf --opt o=bind
docker volume create --name sandbox_zeppelin_notebook --opt type=none --opt device=/home/brijeshdhaker/IdeaProjects/zeppelin-notebooks --opt o=bind

docker volume create --name sandbox_nifi_conf --opt type=none --opt device=/apps/sandbox/nifi/conf --opt o=bind
docker volume create --name sandbox_nifi_content_repository --opt type=none --opt device=/apps/sandbox/nifi/content_repository --opt o=bind
docker volume create --name sandbox_nifi_database_repository --opt type=none --opt device=/apps/sandbox/nifi/database_repository --opt o=bind
docker volume create --name sandbox_nifi_flowfile_repository --opt type=none --opt device=/apps/sandbox/nifi/flowfile_repository --opt o=bind
docker volume create --name sandbox_nifi_provenance_repository --opt type=none --opt device=/apps/sandbox/nifi/provenance_repository --opt o=bind
docker volume create --name sandbox_nifi_log --opt type=none --opt device=/apps/sandbox/nifi/logs --opt o=bind
docker volume create --name sandbox_nifi_state --opt type=none --opt device=/apps/sandbox/nifi/state --opt o=bind

docker volume create --name sandbox_hadoop --opt type=none --opt device=/opt/hadoop-3.2.1 --opt o=bind
docker volume create --name sandbox_hbase --opt type=none --opt device=/opt/hbase-2.4.9 --opt o=bind
docker volume create --name sandbox_hbase_client --opt type=none --opt device=/opt/hbase-1.1.7 --opt o=bind
docker volume create --name sandbox_hive --opt type=none --opt device=/opt/hive-3.1.2 --opt o=bind
docker volume create --name sandbox_spark --opt type=none --opt device=/opt/spark-3.1.2 --opt o=bind

#
# Windows Docker Volumes
#

docker volume create --name sandbox_host_path --opt type=none --opt device=/d/apps/hostpath --opt o=bind
docker volume create --name sandbox_base_path --opt type=none --opt device=/d/apps/sandbox --opt o=bind

docker volume create --name sandbox_m2 --opt type=none --opt device=/d/apps/hostpath/.m2 --opt o=bind
docker volume create --name sandbox_ivy2 --opt type=none --opt device=/d/apps/hostpath/.ivy2 --opt o=bind

docker volume create --name sandbox_zookeeper_secrets --opt type=none --opt device=/d/apps/sandbox/zookeeper/secrets --opt o=bind
docker volume create --name sandbox_zookeeper_data --opt type=none --opt device=/d/apps/sandbox/zookeeper/data --opt o=bind
docker volume create --name sandbox_zookeeper_log --opt type=none --opt device=/d/apps/sandbox/zookeeper/log --opt o=bind

docker volume create --name sandbox_kafka_secrets --opt type=none --opt device=/d/apps/sandbox/kafka-broker/secrets --opt o=bind
docker volume create --name sandbox_kafka_data --opt type=none --opt device=/d/apps/sandbox/kafka-broker/data --opt o=bind
docker volume create --name sandbox_kafka_log --opt type=none --opt device=/d/apps/sandbox/kafka-broker/log --opt o=bind

docker volume create --name sandbox_schema_registry_secrets --opt type=none --opt device=/d/apps/sandbox/schema-registry/secrets --opt o=bind
docker volume create --name sandbox_schema_registry_data --opt type=none --opt device=/d/apps/sandbox/schema-registry/data --opt o=bind
docker volume create --name sandbox_schema_registry_log --opt type=none --opt device=/d/apps/sandbox/schema-registry/log --opt o=bind

docker volume create --name sandbox_cassandra_data --opt type=none --opt device=/d/apps/sandbox/cassandra/data --opt o=bind
docker volume create --name sandbox_cassandra_conf --opt type=none --opt device=/d/apps/sandbox/cassandra/conf --opt o=bind

docker volume create --name sandbox_mysql_data --opt type=none --opt device=/d/apps/sandbox/mysql/data --opt o=bind
docker volume create --name sandbox_mysql_conf --opt type=none --opt device=/d/apps/sandbox/mysql/conf --opt o=bind

docker volume create --name sandbox_hadoop_data --opt type=none --opt device=/d/apps/sandbox/hadoop --opt o=bind
docker volume create --name sandbox_hadoop_dfs_name --opt type=none --opt device=/d/apps/sandbox/hadoop/dfs/name --opt o=bind
docker volume create --name sandbox_hadoop_dfs_data --opt type=none --opt device=/d/apps/sandbox/hadoop/dfs/data --opt o=bind

docker volume create --name sandbox_yarn_history --opt type=none --opt device=/d/apps/sandbox/hadoop/yarn/history --opt o=bind

docker volume create --name sandbox_hive_data --opt type=none --opt device=/d/apps/sandbox/hive --opt o=bind

docker volume create --name sandbox_postgres --opt type=none --opt device=/d/apps/sandbox/postgres --opt o=bind
docker volume create --name sandbox_postgres_data --opt type=none --opt device=/d/apps/sandbox/postgres/data --opt o=bind
docker volume create --name sandbox_postgres_conf --opt type=none --opt device=/d/apps/sandbox/postgres/conf --opt o=bind
docker volume create --name sandbox_postgres_init --opt type=none --opt device=/d/apps/sandbox/postgres/init.d --opt o=bind

docker volume create --name sandbox_zeppelin --opt type=none --opt device=/d/apps/sandbox/zeppelin --opt o=bind
docker volume create --name sandbox_zeppelin_conf --opt type=none --opt device=/d/apps/sandbox/zeppelin/conf --opt o=bind
docker volume create --name sandbox_zeppelin_notebook --opt type=none --opt device=/c/Users/brije/IdeaProjects/zeppelin-notebooks --opt o=bind

docker volume create --name sandbox_nifi_conf --opt type=none --opt device=/d/apps/sandbox/nifi/conf --opt o=bind
docker volume create --name sandbox_nifi_content_repository --opt type=none --opt device=/d/apps/sandbox/nifi/content_repository --opt o=bind
docker volume create --name sandbox_nifi_database_repository --opt type=none --opt device=/d/apps/sandbox/nifi/database_repository --opt o=bind
docker volume create --name sandbox_nifi_flowfile_repository --opt type=none --opt device=/d/apps/sandbox/nifi/flowfile_repository --opt o=bind
docker volume create --name sandbox_nifi_provenance_repository --opt type=none --opt device=/d/apps/sandbox/nifi/provenance_repository --opt o=bind
docker volume create --name sandbox_nifi_log --opt type=none --opt device=/d/apps/sandbox/nifi/logs --opt o=bind
docker volume create --name sandbox_nifi_state --opt type=none --opt device=/d/apps/sandbox/nifi/state --opt o=bind

docker volume create --name sandbox_hadoop --opt type=none --opt device=/d/opt/hadoop-3.2.1 --opt o=bind
docker volume create --name sandbox_hbase --opt type=none --opt device=/d/opt/hbase-2.4.9 --opt o=bind
docker volume create --name sandbox_hbase_client --opt type=none --opt device=/d/opt/hbase-1.1.7 --opt o=bind
docker volume create --name sandbox_hive --opt type=none --opt device=/d/opt/hive-3.1.2 --opt o=bind
docker volume create --name sandbox_spark --opt type=none --opt device=/d/opt/spark-3.1.2 --opt o=bind



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
mkdir -p /apps/sandbox/zookeeper/snode
mkdir -p /apps/sandbox/zookeeper/data
mkdir -p /apps/sandbox/zookeeper/secrets
mkdir -p /apps/sandbox/zookeeper/logs
#
mkdir -p /apps/sandbox/hadoop/dfs/data
mkdir -p /apps/sandbox/hadoop/dfs/name
mkdir -p /apps/sandbox/hadoop/dfs/namesecondary

mkdir -p /apps/sandbox/postgres
mkdir -p /apps/sandbox/hive
#
mkdir -p /apps/hostpath/drivers
mkdir -p /apps/hostpath/datasets
#
mkdir -p /apps/hostpath/localstack


sudo chown brijeshdhaker:brijeshdhaker -R /apps
sudo chmod 777 -R /apps
