#
# Hive with Spark Engine
#

ln -s /opt/spark-2.3.4/jars/spark-network-common_2.11-2.3.4.jar /opt/hive-3.1.2/lib/spark-network-common_2.11-2.3.4.jar
ln -s /opt/spark-2.3.4/jars/spark-core_2.11-2.3.4.jar /opt/hive-3.1.2/lib/spark-core_2.11-2.3.4.jar
ln -s /opt/spark-2.3.4/jars/scala-library-2.11.8.jar /opt/hive-3.1.2/lib/scala-library-2.11.8.jar

$SPARK_HOME/sbin/start-master.sh
$SPARK_HOME/bin/spark-class org.apache.spark.deploy.worker.Worker --webui-port 8081 spark://172.18.0.1:7077

hive --hiveconf hive.root.logger=INFO,console --remote-host=localhost

### -- JDBC 
jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET

### -- Beeline
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET"

#
# Setup Metastore Database
#
echo "CREATE DATABASE hive_store;" | psql -U postgres
echo "CREATE USER hive_admin WITH PASSWORD 'hive_admin';" | psql -U postgres
echo "GRANT ALL PRIVILEGES ON DATABASE hive_store TO hive_admin;" | psql -U postgres

#
#
#
export HADOOP_HOME=/opt/hadoop
$HADOOP_HOME/bin/hdfs dfs -mkdir -p /warehouse/tablespace/managed/hive
$HADOOP_HOME/bin/hdfs dfs -mkdir -p /warehouse/tablespace/external/hive
$HADOOP_HOME/bin/hdfs dfs -chmod g+w /tmp


$ $HIVE_HOME/bin/schematool -initSchema -dbType derby
$ $HIVE_HOME/bin/schematool -initSchema -dbType postgres
$ $HIVE_HOME/bin/hive --version

#
#
#
$HIVE_HOME/bin/beeline -u jdbc:hive2:// -n scott -p tiger


$HIVE_HOME/bin/hiveserver2


$HIVE_HOME/bin/beeline -u jdbc:hive2://localhost:10000 scott tiger

$HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger

$HIVE_HOMEbin/beeline -u jdbc:hive2:// -n scott -p tiger

beeline>!connect jdbc:hive2:// -n scott -p tiger
(or)
bin/beeline -u jdbc:hive2:// scott tiger

#
# Remote 
#
$HIVE_HOME/bin/beeline

beeline>!connect jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger
(or)
beeline>!connect jdbc:hive2://hiveserver.sandbox.net:10000 -n scott -p tiger

$HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger

#
# Documentation
#
https://cwiki.apache.org/confluence/display/Hive/Home#Home-HiveDocumentation

#
# Linux 
#
export SPARK_HOME=/opt/spark
$SPARK_HOME/bin/beeline -u jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger

#
# HDP Sandbox
#
$ $HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET"

CREATE TABLE students (name VARCHAR(64), age INT, gpa DECIMAL(3,2));
INSERT INTO TABLE students VALUES ('Brijesh Dhaker', 35, 1.28), ('Tejas Dhaker', 32, 2.32);

#
#
# Windows
#
SET SPARK_HOME=D:\apps\spark-3.1.2
%SPARK_HOME%\bin\beeline.cmd -u jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger

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

SET hive.txn.manager=org.apache.hadoop.hive.ql.lockmgr.DbTxnManager;
SET hive.support.concurrency=true;
SET hive.support.concurrency=false;
#This property is not needed if you are using Hive 2.x or later
set hive.enforce.bucketing = true;
#
#
#

show databases;

CREATE DATABASE IF NOT EXISTS SPARK_APPS;

DROP DATABASE [IF EXISTS] SPARK_APPS [RESTRICT|CASCADE];

#
## Regular INTERNAL Tables
# empId,empName,job,manager,hiredate,salary,comm,deptno
# 7839, KING, PRESIDENT, null,17-11-1981,5000, null, 10
DROP TABLE IF EXISTS SPARK_APPS.EMPLOYEE;

CREATE TABLE IF NOT EXISTS SPARK_APPS.EMPLOYEE (
    empId int,
    empName string,
    job string,
    manager int,
    hiredate string,
    salary int,
    comm int,
    deptno int
)
COMMENT 'Employee Table'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
STORED AS textfile
TBLPROPERTIES("creator"="Brijesh K Dhaker", "skip.header.line.count"="1");

INSERT INTO EMPLOYEE VALUES
(8001, 'Brijesh', 10, null, '17-11-1981',5000, null, 10),
(8002, 'Tejas', 12, null, '10-12-2021',7000, null, 20);

## load data from local file
LOAD DATA LOCAL INPATH '/home/brijeshdhaker/Downloads/Employee.txt' INTO TABLE SPARK_APPS.EMPLOYEE;

## load data from hdfs://namenode:9000/data/Employee.txt
LOAD DATA INPATH '/data/Employee.txt' OVERWRITE INTO TABLE SPARK_APPS.EMPLOYEE;

use SPARK_APPS;
show tables;
select * from EMPLOYEE where manager=7566;

DROP TABLE IF EXISTS SPARK_APPS.EMPLOYEE_EXTERNAL_TABLE;
CREATE EXTERNAL TABLE IF NOT EXISTS SPARK_APPS.EMPLOYEE_EXTERNAL_TABLE (
    empId int,
    empName string,
    job string,
    manager int,
    hiredate string,
    salary int,
    comm int,
    deptno int
)
COMMENT 'Employee External Table'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
STORED AS textfile
LOCATION '/user/brijeshdhaker/hiveexttable'
TBLPROPERTIES("creator"="Brijesh K Dhaker", "skip.header.line.count"="1");

LOAD DATA INPATH '/data/Employee.txt' OVERWRITE INTO TABLE SPARK_APPS.EMPLOYEE_EXTERNAL_TABLE;


select * from EMPLOYEE_EXTERNAL_TABLE where manager=7566;



#
## Create Regular Tables from existing table
#
CREATE TABLE q1_miniwikistats
AS
SELECT projcode, sum(pageviews)
FROM miniwikistats
WHERE dt >= '20110101' AND dt <= '20110105'
GROUP BY projcode;

#
## Temporary Tables
#
CREATE TEMPORARY TABLE emp.employee_tmp (
id int,
name string,
age int,
gender string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

CREATE TABLE emp.similar LIKE emp.employee;


SHOW PARTITIONS table_name PARTITION(partitioned_column='partition_value')
SHOW PARTITIONS hive_partitioned_table;
SHOW PARTITIONS hive_partitioned_table PARTITION(city='Paris');

CREATE EXTERNAL TABLE slice_miniwikistats ...
LOCATION 's3n://paid-qubole/default-datasets/miniwikistats/'
ALTER TABLE slice_miniwikistats RECOVER PARTITIONS LIKE '20110102*'

CREATE TMP TABLE tmp_stats AS
SELECT * FROM miniwikistats
WHERE projcode LIKE 'ab%' AND dt > '20110101';

DESCRIBE FORMATTED hive_partitioned_table;

#
## Parquet
# 

create external table nation_s3_parquet
(N_NATIONKEY INT, N_NAME STRING, N_REGIONKEY INT, N_COMMENT STRING)
STORED AS Parquet
LOCATION  's3://qtest-qubole-com/datasets/presto/functional/nation_s3_orc'

#
## ORC Tables : The Optimized Row Columnar (ORC) file format offers an efficient way for storing Hive data.
#
create external table nation_s3_orc
(N_NATIONKEY INT, N_NAME STRING, N_REGIONKEY INT, N_COMMENT STRING)
STORED AS ORC
LOCATION  's3://qtest-qubole-com/datasets/presto/functional/nation_s3_orc'
TBLPROPERTIES ("orc.compress"="SNAPPY");

#
## AVRO Tables 
#

#
# 1. Get Avro Schema 
#
$ java -jar avro-tools-1.7.4.jar getschema episodes.avro
{
"type" : "record",
"name" : "episodes",
"namespace" : "testing.hive.avro.serde",
"fields" : [ {
"name" : "title",
"type" : "string",
"doc"  : "episode title"
}, {
"name" : "air_date",
"type" : "string",
"doc"  : "initial date"
}, {
"name" : "doctor",
"type" : "int",
"doc"  : "main actor playing the Doctor in episode"
} ]
}

#
# 2. DDL Statement
#

CREATE EXTERNAL TABLE episodes
ROW FORMAT
SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
WITH SERDEPROPERTIES ('avro.schema.literal'='
{
"type" : "record",
"name" : "episodes",
"namespace" : "testing.hive.avro.serde",
"fields" : [ {
"name" : "title",
"type" : "string",
"doc" : "episode title"
}, {
"name" : "air_date",
"type" : "string",
"doc" : "initial date"
}, {
"name" : "doctor",
"type" : "int",
"doc" : "main actor playing the Doctor in episode"
} ]
}
')
STORED AS
INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION 's3://public-qubole/datasets/avro/episodes'
;

#
## Partition Table
#

CREATE TABLE zipcodes(
    RecordNumber int,
    Country string,
    City string,
    Zipcode int
) PARTITIONED BY(state string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

hdfs dfs -put /home/brijeshdhaker/Downloads/zipcodes.csv /data/
LOAD DATA INPATH '/data/zipcodes.csv' INTO TABLE zipcodes;

SHOW PARTITIONS zipcodes;
SHOW PARTITIONS zipcodes PARTITION(state='NC');

ALTER TABLE zipcodes ADD PARTITION (state='CA') LOCATION '/user/data/zipcodes_ca';
ALTER TABLE zipcodes PARTITION (state='AL') RENAME TO PARTITION (state='NY');
ALTER TABLE zipcodes DROP IF EXISTS PARTITION (state='AL');
MSCK REPAIR TABLE zipcodes SYNC PARTITIONS;
DESCRIBE FORMATTED zipcodes PARTITION(state='PR');
SHOW TABLE EXTENDED LIKE zipcodes PARTITION(state='PR');



#
## Hive Bucketing Example
#
DROP TABLE IF EXISTS zipcodes;

CREATE TABLE zipcodes(
    RecordNumber int,
    Country string,
    City string,
    Zipcode int
)
PARTITIONED BY(state string)
CLUSTERED BY (Zipcode) INTO 32 BUCKETS
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

#This property is not needed if you are using Hive 2.x or later
set hive.enforce.bucketing = true;

LOAD DATA INPATH '/data/zipcodes.csv' INTO TABLE zipcodes;
SELECT * FROM zipcodes WHERE state='PR' and country=704;


CREATE TABLE IF NOT EXISTS buckets_test.nytaxi_sample_bucketed (
    trip_id INT,
    vendor_id STRING,
    pickup_datetime TIMESTAMP
)
CLUSTERED BY (trip_id)
INTO 20 BUCKETS
STORED AS PARQUET
LOCATION ‘s3:///buckets_test/hive-clustered/’;


