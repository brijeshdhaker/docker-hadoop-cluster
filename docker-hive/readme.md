#Hadoop Configurations
export HADOOP_HOME=/opt/hadoop-3.2.1
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export HADOOP_MAPRED_HOME=${HADOOP_HOME}
export HADOOP_COMMON_HOME=${HADOOP_HOME}
export HADOOP_HDFS_HOME=${HADOOP_HOME}
export YARN_HOME=${HADOOP_HOME}
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

#Hive Configurations
export HIVE_HOME=/opt/hive-3.1.2
export PATH=$PATH:$HIVE_HOME/sbin:$HIVE_HOME/bin
export CLASSPATH=$CLASSPATH:$HADOOP_HOME/lib/*:$HIVE_HOME/lib/*

#
# Setup Metastore Database
#
echo "CREATE DATABASE hive_store;" | psql -U postgres
echo "CREATE USER hive_admin WITH PASSWORD 'hive_admin';" | psql -U postgres
echo "GRANT ALL PRIVILEGES ON DATABASE hive_store TO hive_admin;" | psql -U postgres

#
#
#
export HADOOP_HOME=/opt/hadoop-3.2.1
$HADOOP_HOME/bin/hdfs dfs -mkdir -p /user/hive/warehouse
$HADOOP_HOME/bin/hdfs dfs -mkdir -p /user/tmp
$HADOOP_HOME/bin/hdfs dfs -chmod g+w /user/tmp
$HADOOP_HOME/bin/hdfs dfs -chmod g+w /user/hive/warehouse


schematool -initSchema -dbType derby

cd $HIVE_HOME
$ bin/schematool -initSchema -dbType postgres

$ bin/hive --version

#
#
#
bin/beeline -u jdbc:hive2:// -n scott -p tiger

$HIVE_HOME/bin/hiveserver2

bin/beeline -u jdbc:hive2://hostmaster:10000 scott tiger

bin/beeline -u jdbc:hive2:// -n scott -p tiger

beeline>!connect jdbc:hive2:// -n scott -p tiger
(or)
bin/beeline -u jdbc:hive2:// scott tiger

#
# Remote 
#
beeline>!connect jdbc:hive2://hostmaster:10000 scott tiger
(or)
beeline>!connect jdbc:hive2://hostmaster:10000 -n scott -p tiger

bin/beeline -u jdbc:hive2://hostmaster:10000 scott tiger

#
# Documentation
#
https://cwiki.apache.org/confluence/display/Hive/Home#Home-HiveDocumentation

#
# Linux 
#
export SPARK_HOME=/opt/spark-3.1.2
$SPARK_HOME/bin/beeline -u jdbc:hive2://192.168.0.100:10000 scott tiger

#
#
# Windows
#
SET SPARK_HOME=D:\apps\spark-3.1.2
%SPARK_HOME%\bin\beeline.cmd -u jdbc:hive2://192.168.0.100:10000 scott tiger

#
#
#
$ wget https://repo1.maven.org/maven2/io/prestosql/presto-cli/308/presto-cli-308-executable.jar
$ mv presto-cli-308-executable.jar presto.jar
$ chmod +x presto.jar
$ ./presto.jar --server localhost:8080 --catalog hive --schema default
presto> select * from pokes;
./presto --server localhost:8080 --catalog hive --schema default

#
#
#

show databases;

CREATE DATABASE IF NOT EXISTS SPARK_APPS;

DROP DATABASE [IF EXISTS] database_name [RESTRICT|CASCADE];

CREATE TABLE IF NOT EXISTS SPARK_APPS.employee (
id int,
name string,
age int,
gender string )
COMMENT 'Employee Table'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

LOAD DATA LOCAL INPATH '/home/hive/data.txt' INTO TABLE emp.employee;