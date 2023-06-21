
| filesystem | Path                  | User:Group  | Permissions |
|------------|-----------------------|-------------|-------------|
| local      | dfs.namenode.name.dir | hdfs:hadoop | drwx------  |
| local      | dfs.datanode.data.dir | hdfs:hadoop | drwx------  |
| local      | $HADOOP_LOG_DIR       | hdfs:hadoop | drwxrwxr-x  |
|            |                       |             |             |
|            |                       |             |             |
|            |                       |             |             |
|            |                       |             |             |
|            |                       |             |             |
|            |                       |             |             |

|filesystem	Path	User:Group	Permissions
local	dfs.namenode.name.dir	hdfs:hadoop	drwx------
local	dfs.datanode.data.dir	hdfs:hadoop	drwx------
local	$HADOOP_LOG_DIR	hdfs:hadoop	drwxrwxr-x
local	$YARN_LOG_DIR	yarn:hadoop	drwxrwxr-x
local	yarn.nodemanager.local-dirs	yarn:hadoop	drwxr-xr-x
local	yarn.nodemanager.log-dirs	yarn:hadoop	drwxr-xr-x
local	container-executor	root:hadoop	--Sr-s--*
local	conf/container-executor.cfg	root:hadoop	r-------*
hdfs	/	hdfs:hadoop	drwxr-xr-x
hdfs	/tmp	hdfs:hadoop	drwxrwxrwxt
hdfs	/user	hdfs:hadoop	drwxr-xr-x
hdfs	yarn.nodemanager.remote-app-log-dir	yarn:hadoop	drwxrwxrwxt
hdfs	mapreduce.jobhistory.intermediate-done-dir	mapred:hadoop	drwxrwxrwxt
hdfs	mapreduce.jobhistory.done-dir	mapred:hadoop	drwxr-x---

#
# MySql Admin Password
#
root/hortonworks1

#
# Ranger Password
#
admin/hortonworks1

#
# sandbox-hdp.hortonworks.com
# docker pull hortonworks/nifi:3.1.2.0
docker run --name nifi \
  -p 19090:8080 \
  -p 19443:8443 \
  -d \
  hortonworks/nifi:3.1.2.0

http://sandbox-hdp.hortonworks.com:19090/nifi

#
#
#
bin/nifi.sh set-single-user-credentials nifi-admin nifiadmin1234$


#
# Schema Registory
#

unix> mysql -u root -p
unix> Enter password: hortonworks1
mysql> create database schema_registry;
mysql> CREATE USER 'registry_user'@'localhost' IDENTIFIED BY 'registry_password';
mysql> GRANT ALL PRIVILEGES ON schema_registry.* TO 'registry_user'@'localhost' WITH GRANT OPTION;
mysql> commit;


./bootstrap/bootstrap-storage.sh
./bin/registry-server-start.sh ./conf/registry-mysql.yaml &

/usr/hdp/current/schema-registry/bin/registry-server-start.sh /usr/hdp/current/schema-registry/conf/registry-mysql.yaml
 
http://sandbox-hdp.hortonworks.com:7788
http://sandbox-hdp.hortonworks.com:7788/api/v1

tar xzvf hortonworks-registry-0.2.1.tar.gz
cd hortonworks-registry-0.2.1
./bin/registry-server-start.sh conf/registry-dev.yaml


#
# Stream Analytics
#

## 1. Create Kafka Topics

sandbox-hdp.hortonworks.com:6667

/apps/data-loader/routes

./createKafkaTopics.sh sandbox-hdp.hortonworks.com:2181

## 2. Register Schemas for the Kafka Topics

java -cp \
stream-simulator-jar-with-dependencies.jar \
hortonworks.hdf.sam.refapp.trucking.simulator.schemaregistry.TruckSchemaRegistryLoader \
http://sandbox-hdp.hortonworks.com:7788/api/v1

## 3. Setting up an Enrichment Store, Creating an HBase Table, and Creating an HDFS Directory

cd /usr/hdp/current/phoenix-client/bin
./sqlline.py sandbox-hdp.hortonworks.com:2181:/hbase-unsecure


### 3.3 Steps for Starting HBase and Creating an HBase Table

This can be easily done by adding the HDP HBase Service using Ambari.
Create a new HBase table by logging into an node where Hbase client is installed then execute the following commands:
cd /usr/hdp/current/hbase-client/bin
/hbase shell
create 'violation_events', {NAME=> 'events', VERSIONS => 3} ;

### 3.4 Steps for Creating an HDFS Directory

Create the following directory in HDFS and give it access to all users.

Log into a node where HDFS client is installed.
Execute the following commands:
su hdfs
hadoop fs -mkdir /apps/trucking-app
hadoop fs -chmod 777 /apps/trucking-app


#
#
# Run on a YARN cluster
# --conf "spark.yarn.jars=file:///opt/spark-2.3.1/jars/*.jar" \

su hdfs
hadoop fs -mkdir -p /user/brijeshdhaker/spark-jars
hdfs dfs -put *zip /user/brijeshdhaker/spark-jars/spark-jars.zip

#
./bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode cluster \
--executor-memory 1G \
--num-executors 2 \
--conf "spark.yarn.archive=hdfs:///user/brijeshdhaker/spark-jars/spark-jars.zip" \
/usr/hdp/3.0.1.0-187/spark2/examples/jars/spark-examples_*.jar 2
