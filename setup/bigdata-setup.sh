#!/bin/bash

# Add Groups
groupadd -f -r -g 1001 hadoop && \
groupadd -f -r -g 1002 hdfs && \
groupadd -f -r -g 1003 mapred && \
groupadd -f -r -g 1005 hbase && \
groupadd -f -r -g 185 spark && \
groupadd -f -r -g 1006 zookeeper && \
groupadd -f -r -g 1007 zeppelin

# Add Users
useradd --no-create-home --system -g 1001 -u 999 -G hadoop hdfs && \
useradd -M -r -g 1001 -u 998 -G hadoop yarn && \
useradd -M -r -g 1001 -u 997 -G hadoop mapred && \
useradd -M -r -g 1003 -u 996 -G mapred hive && \
useradd -M -r -g 1005 -u 995 -G hbase hbase && \
useradd -M -r -g 1006 -u 993 -G zookeeper zookeeper && \
useradd -M -r -g 1007 -u 992 -G zeppelin zeppelin && \
useradd -M -r -g 185 -u 994 -G spark spark

#
# Docker network
#
docker network create -d bridge sandbox.net

#
# Linux Docker Volumes
#

docker volume create --name sandbox_apps_path --opt type=none --opt device=/apps --opt o=bind

docker volume create --name sandbox_maven_363 --opt type=none --opt device=/opt/maven-3.6.3 --opt o=bind
docker volume create --name sandbox_m2 --opt type=none --opt device=/apps/hostpath/.m2 --opt o=bind
docker volume create --name sandbox_ivy2 --opt type=none --opt device=/apps/hostpath/.ivy2 --opt o=bind

docker volume create --name sandbox_zookeeper_371 --opt type=none --opt device=/apps/sandbox/zookeeper-3.7.1 --opt o=bind

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

docker volume create --name sandbox_airflow_sources --opt type=none --opt device=/apps/sandbox/airflow --opt o=bind
docker volume create --name sandbox_airflow_dags --opt type=none --opt device=/apps/sandbox/airflow/dags --opt o=bind
docker volume create --name sandbox_airflow_logs --opt type=none --opt device=/apps/sandbox/airflow/logs --opt o=bind
docker volume create --name sandbox_airflow_plugins --opt type=none --opt device=/apps/sandbox/airflow/plugins --opt o=bind

docker volume create --name sandbox_hadoop_324 --opt type=none --opt device=/opt/hadoop-3.2.4 --opt o=bind
docker volume create --name sandbox_hadoop_324_dfs --opt type=none --opt device=/apps/sandbox/hadoop-3.2.4/dfs --opt o=bind
docker volume create --name sandbox_hadoop_324_yarn --opt type=none --opt device=/apps/sandbox/hadoop-3.2.4/yarn --opt o=bind
docker volume create --name sandbox_hadoop_324_mapred --opt type=none --opt device=/apps/sandbox/hadoop-3.2.4/mapred --opt o=bind

docker volume create --name sandbox_spark_340 --opt type=none --opt device=/opt/spark-3.4.0 --opt o=bind

docker volume create --name sandbox_hive_312 --opt type=none --opt device=/opt/hive-3.1.2 --opt o=bind
docker volume create --name sandbox_tez_092 --opt type=none --opt device=/opt/tez-0.9.2 --opt o=bind

docker volume create --name sandbox_hbase_300 --opt type=none --opt device=/opt/hbase-3.0.0 --opt o=bind
docker volume create --name sandbox_hbase_117 --opt type=none --opt device=/opt/hbase-1.1.7 --opt o=bind

#
# Require Dirs
#
sudo mkdir -p /apps/{sandbox, hostpath, var/log}
sudo chown brijeshdhaker:root -R /apps
sudo chmod 775 -R /apps

sudo tar --strip-components=1 -xvf hadoop-3.2.4.tar.gz -C /opt/hadoop-3.2.4
sudo tar --strip-components=1 -xvf spark-3.1.2-bin-hadoop3.2.tgz -C /opt/spark-3.1.2
sudo tar --strip-components=1 -xvf spark-3.4.0-bin-hadoop3.tgz -C /opt/spark-3.4.0
sudo tar --strip-components=1 -xvf apache-hive-3.1.2-bin.tar.gz -C /opt/hive-3.1.2
sudo tar --strip-components=1 -xvf hbase-3.0.0-alpha-4-bin.tar.gz -C /opt/hbase-3.0.0
sudo tar --strip-components=1 -xvf hbase-1.1.7-bin.tar.gz -C /opt/hbase-1.1.7
sudo tar --strip-components=1 -xvf apache-zookeeper-3.7.1-bin.tar.gz -C /opt/zookeeper-3.7.1
sudo tar --strip-components=1 -xvf apache-tez-0.9.2-bin.tar.gz -C /opt/tez-0.9.2
sudo unzip apache-maven-3.6.3-bin.zip -d /opt

cd /opt
sudo chown -R brijeshdhaker:root hadoop-3.2.4 hbase-1.1.7 hbase-3.0.0 hive-3.1.2 maven-3.6.3 spark-3.4.0 zookeeper:3.7.1