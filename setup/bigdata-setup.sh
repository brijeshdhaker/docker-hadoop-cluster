#!/bin/bash

hadoop_gid=1001
hdfs_gid=1002
mapred_gid=1003
hive_gid=1004
hbase_gid=1005
zookeeper_gid=1006
zeppelin_gid=1007
spark_gid=1008
yarn_gid=1009
httpfs_gid=1010

bdusers[1001]=hadoop
bdusers[1002]=hdfs
bdusers[1003]=mapred
bdusers[1004]=hive
bdusers[1005]=hbase
bdusers[1006]=zookeeper
bdusers[1007]=zeppelin
bdusers[1008]=spark
bdusers[1009]=yarn
bdusers[1010]=httpfs

# delete existing users
for uid in {1001..1010}
do
  usr=${bdusers[$uid]}
  deluser $usr
done

# delete existing users
for gid in {1001..1010}
do
  grp=${bdusers[$gid]}
  delgroup $grp
done

# Add Groups
for gid in {1001..1010}
do
  group=${bdusers[$gid]}
  groupadd -f -r -g ${gid} $group
done

# Add Users
for uid in {1001..1010}
do
  group=${bdusers[$gid]}
  usr=${bdusers[$uid]}
  deluser $usr
done

useradd -m -r -s /bin/bash -g ${hadoop_gid} -u ${hadoop_gid} -G hadoop hadoop && \
useradd -m -s /bin/bash -g ${hdfs_gid} -u ${hdfs_gid} -G hdfs,hadoop hdfs && \
useradd -m -s /bin/bash -g ${httpfs_gid} -u ${httpfs_gid} -G httpfs,hadoop httpfs && \
useradd -m -s /bin/bash -g ${yarn_gid} -u ${yarn_gid} -G yarn,hadoop yarn && \
useradd -m -s /bin/bash -g ${mapred_gid} -u ${mapred_gid} -G mapred,hadoop mapred && \
useradd -m -s /bin/bash -g ${hive_gid} -u ${hive_gid} -G hive hive && \
useradd -m -s /bin/bash -g ${hbase_gid} -u ${hbase_gid} -G hbase hbase && \
useradd -m -s /bin/bash -g ${spark_gid} -u ${spark_gid} -G spark spark && \
useradd -m -s /bin/bash -g ${zookeeper_gid} -u ${zookeeper_gid} -G zookeeper zookeeper && \
useradd -m -s /bin/bash -g ${zeppelin_gid} -u ${zeppelin_gid} -G zeppelin zeppelin

# without home dir
# useradd --no-create-home --system -g 1001 -u 999 -G hadoop hdfs && \
useradd -M -r -s /bin/bash -g ${hadoop_gid} -u ${hadoop_gid} -G hadoop hadoop && \
useradd -M -r -s /bin/bash -g ${hdfs_gid} -u ${hdfs_gid} -G hdfs,hadoop hdfs && \
useradd -M -r -s /bin/bash -g ${httpfs_gid} -u ${httpfs_gid} -G httpfs,hadoop httpfs && \
useradd -M -r -s /bin/bash -g ${yarn_gid} -u ${yarn_gid} -G yarn,hadoop yarn && \
useradd -M -r -s /bin/bash -g ${mapred_gid} -u ${mapred_gid} -G mapred,hadoop mapred && \
useradd -M -r -s /bin/bash -g ${hive_gid} -u ${hive_gid} -G hive hive && \
useradd -M -r -s /bin/bash -g ${hbase_gid} -u ${hbase_gid} -G hbase hbase && \
useradd -M -r -s /bin/bash -g ${spark_gid} -u ${spark_gid} -G spark spark && \
useradd -M -r -s /bin/bash -g ${zookeeper_gid} -u ${zookeeper_gid} -G zookeeper zookeeper && \
useradd -M -r -s /bin/bash -g ${zeppelin_gid} -u ${zeppelin_gid} -G zeppelin zeppelin


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

docker volume create --name sandbox_zookeeper_secrets --opt type=none --opt device=/apps/sandbox/zookeeper/hadoop3/secrets --opt o=bind
docker volume create --name sandbox_zookeeper_data --opt type=none --opt device=/apps/sandbox/zookeeper/hadoop3/data --opt o=bind
docker volume create --name sandbox_zookeeper_log --opt type=none --opt device=/apps/sandbox/zookeeper/hadoop3/log --opt o=bind

docker volume create --name sandbox_kafka_secrets --opt type=none --opt device=/apps/sandbox/kafka/hadoop3/secrets --opt o=bind
docker volume create --name sandbox_kafka_data --opt type=none --opt device=/apps/sandbox/kafka/hadoop3/data --opt o=bind
docker volume create --name sandbox_kafka_log --opt type=none --opt device=/apps/sandbox/kafka/hadoop3/log --opt o=bind

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

docker volume create --name sandbox_hadoop_311 --opt type=none --opt device=/opt/hadoop-3.1.1 --opt o=bind
docker volume create --name sandbox_hadoop_311_dfs --opt type=none --opt device=/apps/sandbox/hadoop-3.1.1/dfs --opt o=bind
docker volume create --name sandbox_hadoop_311_yarn --opt type=none --opt device=/apps/sandbox/hadoop-3.1.1/yarn --opt o=bind
docker volume create --name sandbox_hadoop_311_mapred --opt type=none --opt device=/apps/sandbox/hadoop-3.1.1/mapred --opt o=bind

docker volume create --name sandbox_spark_312 --opt type=none --opt device=/opt/spark-3.1.2 --opt o=bind
docker volume create --name sandbox_hive_313 --opt type=none --opt device=/opt/hive-3.1.3 --opt o=bind
docker volume create --name sandbox_tez_091 --opt type=none --opt device=/opt/tez-0.9.1 --opt o=bind
docker volume create --name sandbox_hbase_246 --opt type=none --opt device=/opt/hbase-2.4.6 --opt o=bind
docker volume create --name sandbox_hbase_117 --opt type=none --opt device=/opt/hbase-1.1.7 --opt o=bind
docker volume create --name sandbox_flink_112 --opt type=none --opt device=/opt/flink-1.12.2 --opt o=bind

#
# Require Dirs
#
sudo mkdir -p /apps/{sandbox,hostpath,var/log,}
sudo chown brijeshdhaker:root -R /apps
sudo chmod 775 -R /apps

sudo mkdir -p /opt/hadoop-3.1.1
sudo tar --strip-components=1 -xvf hadoop-3.1.1.tar.gz -C /opt/hadoop-3.1.1

sudo mkdir -p /opt/hive-3.1.3
sudo tar --strip-components=1 -xvf apache-hive-3.1.3-bin.tar.gz -C /opt/hive-3.1.3

sudo mkdir -p /opt/tez-0.9.2
sudo tar --strip-components=1 -xvf apache-tez-0.9.2-bin.tar.gz -C /opt/tez-0.9.2

sudo mkdir -p /opt/hbase-2.4.6
sudo tar --strip-components=1 -xvf hbase-2.4.6-bin.tar.gz -C /opt/hbase-2.4.6

sudo mkdir -p /opt/hbase-1.1.7
sudo tar --strip-components=1 -xvf hbase-1.1.7-bin.tar.gz -C /opt/hbase-1.1.7

sudo mkdir -p /opt/spark-3.1.2
sudo tar --strip-components=1 -xvf spark-3.1.2-bin-hadoop3.2.tgz -C /opt/spark-3.1.2

sudo tar --strip-components=1 -xvf apache-zookeeper-3.7.1-bin.tar.gz -C /opt/zookeeper-3.7.1

sudo unzip apache-maven-3.6.3-bin.zip -d /opt

cd /opt
sudo chown -R brijeshdhaker:root hadoop-3.1.1 hbase-1.1.7 hbase-2.4.6 hive-3.1.3 tez-0.9.1 maven-3.6.3 spark-3.1.2 flink-1.12.2 zookeeper:3.7.1