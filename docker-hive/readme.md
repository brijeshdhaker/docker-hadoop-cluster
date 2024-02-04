
## Testing
Load data into Hive:
```
  $ docker-compose exec hive-server bash
  # $HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET
  > CREATE TABLE pokes (foo INT, bar STRING);
  > LOAD DATA LOCAL INPATH '/opt/hive/examples/files/kv1.txt' OVERWRITE INTO TABLE pokes;
```

Then query it from PrestoDB. You can get [presto.jar](https://prestosql.io/docs/current/installation/cli.html) from PrestoDB website:
```
  $ wget https://repo1.maven.org/maven2/io/prestosql/presto-cli/308/presto-cli-308-executable.jar
  $ mv presto-cli-308-executable.jar presto.jar
  $ chmod +x presto.jar
  $ ./presto.jar --server localhost:8080 --catalog hive --schema default
  presto> select * from pokes;
```

#
# Hive with Spark Engine
#

ln -s /opt/spark-2.3.4/jars/spark-network-common_2.11-2.3.4.jar /opt/hive-3.1.2/lib/spark-network-common_2.11-2.3.4.jar
ln -s /opt/spark-2.3.4/jars/spark-core_2.11-2.3.4.jar /opt/hive-3.1.2/lib/spark-core_2.11-2.3.4.jar
ln -s /opt/spark-2.3.4/jars/scala-library-2.11.8.jar /opt/hive-3.1.2/lib/scala-library-2.11.8.jar

$SPARK_HOME/sbin/start-master.sh
$SPARK_HOME/bin/spark-class org.apache.spark.deploy.worker.Worker --webui-port 8081 spark://172.18.0.1:7077

hive --hiveconf hive.root.logger=INFO,console --remote-host=hiveserver.sandbox.net:10000

### -- JDBC 
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET"

### -- Beeline
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" --silent=false

$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" -e 'show tables' --hiveconf hive.server2.in.place.progress=true

$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" -f queries.sql

#
# Setup Metastore Database
#
echo "CREATE DATABASE hive_store;" | psql -U postgres
echo "CREATE USER hive_admin WITH PASSWORD 'hive_admin';" | psql -U postgres
echo "GRANT ALL PRIVILEGES ON DATABASE hive_store TO hive_admin;" | psql -U postgres

$ $HIVE_HOME/bin/schematool -initSchema -dbType derby
$ $HIVE_HOME/bin/schematool -initSchema -dbType postgres
$ $HIVE_HOME/bin/hive --version

#
#
#
export HADOOP_HOME=/opt/hadoop
$HADOOP_HOME/bin/hdfs dfs -mkdir -p /warehouse/tablespace/managed/hive
$HADOOP_HOME/bin/hdfs dfs -mkdir -p /warehouse/tablespace/external/hive
$HADOOP_HOME/bin/hdfs dfs -chmod g+w /tmp

#
#
#
$HIVE_HOME/bin/beeline -u jdbc:hive2:// -n scott -p tiger


$HIVE_HOME/bin/hiveserver2


$HIVE_HOME/bin/beeline -u jdbc:hive2://localhost:10000 scott tiger

$HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger

$HIVE_HOME/bin/beeline -u jdbc:hive2:// -n scott -p tiger

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
export HIVE_HOME=/opt/hive
$HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET"

set tez.queue.name = true;
bin/hive --hiveconf tez.queue.name=engineering

dfs -ls /warehouse/tablespace/managed/hive ;

#
#
#
### --default
SET hive.txn.manager=org.apache.hadoop.hive.ql.lockmgr.DummyTxnManager; 
SET hive.support.concurrency=false;

### 
SET hive.txn.manager=org.apache.hadoop.hive.ql.lockmgr.DbTxnManager;
SET hive.support.concurrency=true;

#This property is not needed if you are using Hive 2.x or later
set hive.enforce.bucketing = true;
set hive.execution.engine;
set hive.execution.engine=mr;
set hive.execution.engine=tez;
SET mapred.input.dir.recursive=true;

CREATE TABLE m_students (
    name STRING, 
    age  INT, 
    gpa  DOUBLE
);

INSERT INTO TABLE m_students VALUES ('Brijesh Dhaker', 35, 1.28);
INSERT INTO TABLE m_students VALUES ('Tejas Dhaker', 32, 2.32);
INSERT INTO TABLE m_students VALUES ('Neeta Dhaker', 40, 2.82);
INSERT INTO TABLE m_students VALUES ('Keshvi Dhaker', 12, 4.28);

#
describe extended m_students;
describe formatted m_students;

select * from m_students;
SELECT COUNT(*) FROM m_students;

# Validating Hive-on-Tez Installation
    Use the following procedure to validate your configuration of Hive-on-Tez:

Create a sample test.txt file:

echo -e "Brijesh D\t49\t3.15" > student.txt

echo -e "Brijesh D\t49\t3.15\nNeeta D\t40\t4.15" > student.txt

Upload the new data file to HDFS:

su $HDFS_USER 
hadoop fs -mkdir -p /user/brijeshdhaker/student
hadoop fs -copyFromLocal student.txt /user/brijeshdhaker/student

Open the Hive command-line shell:

$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET"

Create a table named student in Hive:

hive> 

CREATE EXTERNAL TABLE e_students(
    name STRING,
    age  INT,
    gpa  DOUBLE
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'STORED AS TEXTFILE 
LOCATION '/user/brijeshdhaker/student';

CREATE EXTERNAL TABLE e_students(
    name STRING,
    age  INT,
    gpa  DOUBLE
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'STORED AS TEXTFILE;

#
describe extended e_students;
describe formatted e_students;

dfs -cp /user/brijeshdhaker/student/student.txt /warehouse/tablespace/external/hive/e_students/student.txt ;

INSERT INTO TABLE e_students VALUES ('Brijesh Dhaker', 35, 1.28);
INSERT INTO TABLE e_students VALUES ('Tejas Dhaker', 32, 2.32);
INSERT INTO TABLE e_students VALUES ('Neeta Dhaker', 40, 2.82);
INSERT INTO TABLE e_students VALUES ('Keshvi Dhaker', 12, 4.28);

MSCK REPAIR TABLE e_students DROP PARTITIONS;

select * from e_students;
SELECT COUNT(*) FROM e_students;



CREATE EXTERNAL TABLE my_external_table (a string, b string)  
ROW FORMAT SERDE 'com.mytables.MySerDe'
WITH SERDEPROPERTIES ( "input.regex" = "*.csv")
LOCATION '/user/data';       

Execute the following query in Hive:

hive> 


If Hive-on-Tez is configured properly, this query should return successful results similar to the following:

hive> SELECT COUNT(*) FROM student;
Query ID = hdfs_20140604161313_544c4455-dfb3-4119-8b08-b70b46fee512
Total jobs = 1
Launching Job 1 out of 1
Number of reduce tasks determined at compile time: 1
In order to change the average load for a reducer (in bytes):
set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
set mapreduce.job.reduces=<number>
Starting Job = job_1401734196960_0007, Tracking URL = http://c6401.ambari.apache.org:8088/proxy/application_1401734196960_0007/
Kill Command = /usr/lib/hadoop/bin/hadoop job -kill job_1401734196960_0007
Hadoop job information for Stage-1: number of mappers: 1; number of reducers: 1
2014-06-04 16:13:24,116 Stage-1 map = 0%, reduce = 0%
2014-06-04 16:13:30,670 Stage-1 map = 100%, reduce = 0%, Cumulative CPU 0.82 sec
2014-06-04 16:13:39,065 Stage-1 map = 100%, reduce = 100%, Cumulative CPU 1.97 sec
MapReduce Total cumulative CPU time: 1 seconds 970 msec
Ended Job = job_1401734196960_0007
MapReduce Jobs Launched:
Job 0: Map: 1 Reduce: 1 Cumulative CPU: 1.97 sec HDFS Read: 240 HDFS Write: 2 SUCCESS
Total MapReduce CPU Time Spent: 1 seconds 970 msec
OK
1
Time taken: 28.47 seconds, Fetched: 1 row(s)
hive>




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

DROP DATABASE [IF EXISTS] SPARK_APPS [RESTRICT|CASCADE];

#
## Regular INTERNAL Tables
# empId,empName,job,manager,hiredate,salary,comm,deptno
# 7839, KING, PRESIDENT, null,17-11-1981,5000, null, 10
DROP TABLE IF EXISTS EMPLOYEE;

CREATE TABLE IF NOT EXISTS EMPLOYEE (
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

DROP TABLE IF EXISTS EMPLOYEE_EXTERNAL_TABLE;
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


