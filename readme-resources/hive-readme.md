### Solution for 'For input string: "0x100" '
export TERM=xterm-color

###

$SPARK_HOME/bin/spark-shell \
--jars $HIVE_HOME/lib/hive-metastore-2.3.6.jar,$HIVE_HOME/lib/hive-exec-2.3.6.jar,$HIVE_HOME/lib/hive-common-2.3.6.jar,$HIVE_HOME/lib/hive-serde-2.3.6.jar,$HIVE_HOME/lib/guava-14.0.1.jar \
--conf spark.sql.hive.metastore.version=2.3 \
--conf spark.sql.hive.metastore.jars=$HIVE_HOME"/lib/*" \
--conf spark.sql.warehouse.dir=hdfs://localhost:9000/user/hive/warehouse

### HIVE ON HDP
```
$HIVE_HOME/bin/hiveserver2

$HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver:10000/default;user=scott;password=tiger
$HIVE_HOME/bin/beeline -u jdbc:hive2:// -n scott -p tiger
$HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver:10000 scott tiger
$HIVE_HOME/bin/beeline -u jdbc:hive2:// scott tiger
$HIVE_HOME/bin/beeline -u jdbc:hive2:// -n scott -p tiger

$HIVE_HOME/bin/beeline
beeline>!connect jdbc:hive2://hiveserver:10000 -n scott -p tiger
beeline>!connect jdbc:hive2://localhost:10000 -n scott -p tiger
beeline>!connect jdbc:hive2:// -n scott -p tiger
```

#### Documentation
https://cwiki.apache.org/confluence/display/Hive/Home#Home-HiveDocumentation

#### Use presto for Hive Access
```
$ wget https://repo1.maven.org/maven2/io/prestosql/presto-cli/308/presto-cli-308-executable.jar
$ mv presto-cli-308-executable.jar presto.jar
$ chmod +x presto.jar
$ ./presto.jar --server localhost:8080 --catalog hive --schema default
presto> select * from pokes;
./presto --server localhost:8080 --catalog hive --schema default
```

#### Hive Properties
```
SET hive.txn.manager=org.apache.hadoop.hive.ql.lockmgr.DbTxnManager;
SET hive.support.concurrency=true;
SET hive.support.concurrency=false;
Note :  This property is not needed if you are using Hive 2.x or later
set hive.enforce.bucketing = true;
```

#
```hive

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
```
#### Direct data from local file
```
INSERT INTO EMPLOYEE VALUES 
(8001, 'Brijesh', 10, null, '17-11-1981',5000, null, 10),
(8002, 'Tejas', 12, null, '10-12-2021',7000, null, 20),
(8002, 'Keshvi', 14, null, '10-12-2021',6000, null, 20);
```

#### load data from local file
```
LOAD DATA LOCAL INPATH '/home/brijeshdhaker/Downloads/Employee.txt' INTO TABLE SPARK_APPS.EMPLOYEE;
# Note : This command will remove content at source directory and create a internal table
```
#### load data from hdfs://namenode:9000/data/Employee.txt
```
LOAD DATA INPATH '/data/Employee.txt' OVERWRITE INTO TABLE SPARK_APPS.EMPLOYEE;
```

use SPARK_APPS;
show tables;
select * from EMPLOYEE where manager=7566;

#### HIVE External table
```hive
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
TBLPROPERTIES(
    "creator"="Brijesh K Dhaker", 
    "skip.header.line.count"="1"
);

LOAD DATA INPATH '/data/Employee.txt' OVERWRITE INTO TABLE SPARK_APPS.EMPLOYEE_EXTERNAL_TABLE;

select * from EMPLOYEE_EXTERNAL_TABLE where manager=7566;
```
#### Create Regular Tables from existing table
```
CREATE TABLE q1_miniwikistats
AS
SELECT projcode, sum(pageviews)
FROM miniwikistats
WHERE dt >= '20110101' AND dt <= '20110105'
GROUP BY projcode;

```
#### Temporary Tables
```
CREATE TEMPORARY TABLE emp.employee_tmp (
    id int,
    name string,
    age int,
    gender string)
    ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

CREATE TABLE emp.similar LIKE emp.employee;
```
#### Create Temp table from query results
```
CREATE TMP TABLE tmp_stats AS
SELECT * FROM miniwikistats
WHERE projcode LIKE 'ab%' AND dt > '20110101';
```
#### Parquet
```
create external table nation_s3_parquet (
    N_NATIONKEY INT, 
    N_NAME STRING, 
    N_REGIONKEY INT, 
    N_COMMENT STRING
)
STORED AS Parquet
LOCATION  's3://qtest-qubole-com/datasets/presto/functional/nation_s3_orc'

```
### #ORC Tables : The Optimized Row Columnar (ORC) file format offers an efficient way for storing Hive data.
```
create external table nation_s3_orc (
    N_NATIONKEY INT, 
    N_NAME STRING, 
    N_REGIONKEY INT, 
    N_COMMENT STRING
)
STORED AS ORC
LOCATION  's3://qtest-qubole-com/datasets/presto/functional/nation_s3_orc'
TBLPROPERTIES (
"hive.exec.dynamic.partition"="true",
"hive.exec.dynamic.partition.mode"="nonstrict",
"orc.compress"="SNAPPY"
);
```
#### AVRO Tables
#### 1. Get Avro Schema
```
$ java -jar avro-tools-1.7.4.jar getschema episodes.avro
{
    "type" : "record",
    "name" : "episodes",
    "namespace" : "testing.hive.avro.serde",
    "fields" : [{
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
    }]
}
```
#### 2. DDL Statement
```
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
LOCATION 's3://public-qubole/datasets/avro/episodes';

```
#
#### Hive Partition Table
#
```
CREATE TABLE zipcodes(
    RecordNumber int,
    Country string,
    City string,
    Zipcode int
)
PARTITIONED BY(state string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

hadoop fs -mv /data /datasets
hdfs dfs -put /apps/hostpath/datasets/zipcodes20.csv /datasets/
LOAD DATA INPATH '/datasets/zipcodes20.csv' INTO TABLE zipcodes;
LOAD DATA local INPATH '/apps/hostpath/datasets/zipcodes20.csv' INTO TABLE zipcodes;
Note: Remember the partitioned column should be the last column on the file to loaded data into right partitioned column of the table.

OR

1) CREATE TABLE zipcodes_tmp(RecordNumber int,Country string,City string,Zipcode int,State string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
2) LOAD DATA local INPATH '/apps/hostpath/datasets/zipcodes20.csv' INTO TABLE zipcodes_tmp;
3) INSERT OVERWRITE TABLE zipcodes PARTITION(state) SELECT RecordNumber,Country,City,Zipcode,State from  zipcodes_tmp;

SHOW PARTITIONS zipcodes;
SHOW PARTITIONS zipcodes PARTITION(state='NC');

ALTER TABLE zipcodes ADD PARTITION (state='CA') LOCATION '/user/data/zipcodes_ca';
ALTER TABLE zipcodes PARTITION (state='AL') RENAME TO PARTITION (state='NY');
ALTER TABLE zipcodes DROP IF EXISTS PARTITION (state='AL');
ALTER TABLE zipcodes RECOVER PARTITIONS LIKE 'state=*'
MSCK REPAIR TABLE zipcodes SYNC PARTITIONS;
DESCRIBE FORMATTED zipcodes;
DESCRIBE FORMATTED zipcodes PARTITION(state='PR');
SHOW TABLE EXTENDED LIKE zipcodes PARTITION(state='PR');
```
#### Hive Bucketing Example
```
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
```

#
#
```
scala> spark.conf.get("spark.sql.catalogImplementation")
scala> spark.catalog.listTables.show
scala> spark.sharedState.externalCatalog.listTables("default")
scala> sql("DESCRIBE EXTENDED tweeter_tweets").show(Integer.MAX_VALUE, truncate = false)
```

#
# Beeline Shell Commands
#

# The output should be:
| COMMAND                 | DESCRIPTION                                                                       | EXAMPLE                                             |
|-------------------------|:----------------------------------------------------------------------------------|:----------------------------------------------------|
| !help                   | Print a summary of command usage                                                  |                                                     |
| !quit                   | Exits the Beeline client.                                                         |                                                     |
| !history                | Display the command history                                                       |                                                     |
| !table <sql_query_file> | Run SQL query from file                                                           |                                                     |
| set                     | Prints a list of configuration variables that are overridden by the user or Hive. |                                                     |
| set -v                  | Prints all Hadoop and Hive configuration variables.                               |                                                     |
| reset                   | Resets the configuration to the default values.                                   |                                                     |
| !record <file_name>     | Save result set to a file in local file system                                    | !record /user/dummy_local_user/myquery1_results.out |
| !sh                     | Run shell CMD                                                                     | !sh date                                            |
| !sh hadoop fs           | Run HDFS CMD                                                                      | !sh hadoop fs -ls /                                 |
| !dbinfo                 | Check Hive version                                                                |                                                     |
| dfs                     | Executes a dfs command.                                                           | dfs -ls /user ;                                     |

#
# Beeline Command line options
#
COMMAND	DESCRIPTION	BEELINE COMMAND LINE OPTION	INSIDE BEELINE SHELL
Ctrl + r	Search on history of commands

Autocomplete	Press Tab key
Display all 436 possibilities? (y or n)
If you enter y, you’ll get a long list of all the keywords		
Navigation Keystrokes	Use the up↑ and down↓ arrow keys to scroll through previous commands

Ctrl+A goes to the beginning of the line
Ctrl+E goes to the end of the line

Delete key will delete the character to the left of the cursor		
set system:user.name; (or)
set system:user.name=yourusername;	System Namespace (provides read-write access to Java system properties)
set env:HOME; env Namespace (provides read-only access to environment variables)

-- Comment line1
-- Comment line2
Comments in Hive Scripts.		
beeline -e < Hive query >	Run query	beeline -e " show databases"
beeline -f < Hive query >	Run query from file	beeline -f /user/dummy_local_user/myquery1.sql

--hiveconf property=value
(or)
SET property=value;	Use value for the given configuration property. Properties that are listed in hive.conf.restricted.list cannot be reset with hiveconf	beeline --hiveconf hive.auto.convert.join=false;
--Display the configuration value
SET hive.auto.convert.join;

SET hive.auto.convert.join=false;
--Display the configuration value
SET hive.auto.convert.join;

--hivevar name=value
(or)
SET name=value;	Hive variable name and value.
This is a Hive-specific setting in which variables can be set at the session level and referenced in Hive commands or queries.	beeline --hivevar myvar=hello
--Display the variable value
SELECT "${myvar}";


SET hivevar:myvar=hello;
--Display the variable value
SELECT "${myvar}";

outputformat	Format mode for result display. Default is table. Options [
csv/tsv/csv2/tsv2/dsv/
table/
vertical/
xmlattr/
xmlelements
]	beeline --outputformat=tsv2	Ex: !set outputformat tsv2
Ex: !set outputformat xmlattr
Result of the query select id, value, comment from test_table






color	Control whether color is used for display. Default is false	beeline --color=true	!set color true
verbose	Show verbose error messages and debug information (true) or do not show (false). Default is false.	beeline --verbose=true	!set verbose true
headerInterval	The interval for redisplaying column headers, in number of rows, when outputformat is table. Default is 100.	beeline --headerInterval=true	!set headerInterval 10000
showHeader	Show column names in query results (true) or not (false). Default is true.	beeline --showHeader=true	!set showHeader false
force	Continue running script even after errors (true) or do not continue (false). Default is false.	beeline --force=true	!set force true
silent	Reduce the amount of informational messages displayed (true) or not (false).
It also stops displaying the log messages for the query from HiveServer2.
Default is false.	beeline --silent=true	!set silent true
delimiterForDSV	The delimiter for delimiter-separated values output format. Default is ‘|’ character.	beeline --outputformat=dsv --delimiterForDSV="-"	!set outputformat dsv
!set delimiterForDSV “-”
Hive queries


PURPOSE	QUERY
Display current database being used
```shell
SELECT CURRENT_DATABASE();
```

Load data from a local file to the hive table
```shell
LOAD DATA LOCAL INPATH '/unix-path/myfile' INTO TABLE mytable;
```

Load data from hdfs file to the hive table
```shell
LOAD DATA INPATH '/hdfs-path/myfile' INTO TABLE mytable;
```
### -- Beeline

$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver:10000/default;user=scott;password=tiger"

$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" --silent=false
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" -e 'show tables' --hiveconf hive.server2.in.place.progress=true
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" -f queries.sql

#### 1. At the command line, copy the Hue sample_07 and sample_08 CSV files to HDFS
```commandline
export HADOOP_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hadoop
cd ~/PycharmProjects/pyspark-hive-integration

hdfs dfs -put data/sample_07.csv 
hdfs dfs -put data/sample_08.csv 
# or
$HADOOP_HOME/bin/hadoop fs -put data/sample_07.csv 
$HADOOP_HOME/bin/hadoop fs -put data/sample_08.csv 
```

#### 2. Start spark-shell
```commandline
#
export SPARK_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/spark
#
$SPARK_HOME/bin/pyspark
```

#### 3. Create Hive tables sample_07 and sample_08
```python
>>> spark.sql("CREATE TABLE sample_07 (code string,description string,total_emp int,salary int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TextFile")
>>> spark.sql("CREATE TABLE sample_08 (code string,description string,total_emp int,salary int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TextFile")
```

#### 4. In Beeline, show the Hive tables:
```commandline
$HIVE_HOME/bin/beeline -u jdbc:hive2://hiveserver.sandbox.net:10000 scott tiger

[0: jdbc:hive2://hiveserver:> show tables;
+------------+--+
|  tab_name  |
+------------+--+
| sample_07  |
| sample_08  |
+------------+--+
```

#### 5. Load the data in the CSV files into the tables:
```python
>>> spark.sql("LOAD DATA INPATH '/user/brijeshdhaker/sample_07.csv' OVERWRITE INTO TABLE sample_07")
>>> spark.sql("LOAD DATA INPATH '/user/brijeshdhaker/sample_08.csv' OVERWRITE INTO TABLE sample_08")
```

#### 6. Create DataFrames containing the contents of the sample_07 and sample_08 tables:
```python
>>> df_07 = spark.sql("SELECT * from sample_07")
>>> df_08 = spark.sql("SELECT * from sample_08")
```
#### 7. Show all rows in df_07 with salary greater than 150,000:

```
>>> df_07.filter(df_07.salary > 150000).show()
# The output should be:
+-------+--------------------+---------+------+
|   code|         description|total_emp|salary|
+-------+--------------------+---------+------+
|11-1011|    Chief executives|   299160|151370|
|29-1022|Oral and maxillof...|     5040|178440|
|29-1023|       Orthodontists|     5350|185340|
|29-1024|     Prosthodontists|      380|169360|
|29-1061|   Anesthesiologists|    31030|192780|
|29-1062|Family and genera...|   113250|153640|
|29-1063| Internists, general|    46260|167270|
|29-1064|Obstetricians and...|    21340|183600|
|29-1067|            Surgeons|    50260|191410|
|29-1069|Physicians and su...|   237400|155150|
+-------+--------------------+---------+------+
```

#### 8. Create the DataFrame df_09 by joining df_07 and df_08, retaining only the code and description columns.

```
>>> df_09 = df_07.join(df_08, df_07.code == df_08.code).select(df_07.code,df_07.description)
>>> df_09.show()
# The new DataFrame looks like:
+-------+--------------------+
|   code|         description|
+-------+--------------------+
|00-0000|     All Occupations|
|11-0000|Management occupa...|
|11-1011|    Chief executives|
|11-1021|General and opera...|
|11-1031|         Legislators|
|11-2011|Advertising and p...|
|11-2021|  Marketing managers|
|11-2022|      Sales managers|
|11-2031|Public relations ...|
|11-3011|Administrative se...|
|11-3021|Computer and info...|
|11-3031|  Financial managers|
|11-3041|Compensation and ...|
|11-3042|Training and deve...|
|11-3049|Human resources m...|
|11-3051|Industrial produc...|
|11-3061| Purchasing managers|
|11-3071|Transportation, s...|
|11-9011|Farm, ranch, and ...|
|11-9012|Farmers and ranchers|
+-------+--------------------+
```
#### 9. Save DataFrame df_09 as the Hive table sample_09:
```python
>>> df_09.write.saveAsTable("sample_09")
```

#### 10. In Beeline, show the Hive tables:
```commandline

[0: jdbc:hive2://hiveserver:> show tables;
+------------+--+
|  tab_name  |
+------------+--+
| sample_07  |
| sample_08  |
| sample_09  |
+------------+--+
```
#### 11 . The equivalent program in Python, that you could submit using spark-submit, would be:
```python
from pyspark import SparkContext, SparkConf, HiveContext

if __name__ == "__main__":

  # create Spark context with Spark configuration
  conf = SparkConf().setAppName("Data Frame Join")
  sc = SparkContext(conf=conf)
  sqlContext = HiveContext(sc)
  df_07 = sqlContext.sql("SELECT * from sample_07")
  df_07.filter(df_07.salary > 150000).show()
  df_08 = sqlContext.sql("SELECT * from sample_08")
  tbls = sqlContext.sql("show tables")
  tbls.show()
  df_09 = df_07.join(df_08, df_07.code == df_08.code).select(df_07.code,df_07.description)
  df_09.show()
  df_09.write.saveAsTable("sample_09")
  tbls = sqlContext.sql("show tables")
  tbls.show()
```

#
### Make Sure Table deleted before Spark Job Run
#

```commandline
0: jdbc:hive2://hiveserver:10000> drop table sample_09;

```

#
### Run PySpark Job
#

```commandline

export PYSPARK_DRIVER_PYTHON=/opt/conda/envs/pyspark37/bin/python
export PYSPARK_PYTHON=/opt/conda/envs/pyspark37/bin/python
$SPARK_HOME/bin/spark-submit \
    --name "PySpark HiveContext" \
    --master local[*] \
    --py-files /home/brijeshdhaker/PycharmProjects/pyspark-hive-integration.zip pyspark-hive-integration/pyspark-hive-context.py

```

###

```commandline
export PYSPARK_DRIVER_PYTHON=/opt/conda/envs/pyspark37/bin/python
export PYSPARK_PYTHON=/opt/conda/envs/pyspark37/bin/python
#
$SPARK_HOME/bin/spark-submit \
    --name "PySpark Hive Session" \
    --master local[*] \
    --py-files /home/brijeshdhaker/PycharmProjects/pyspark-hive-integration.zip pyspark-hive-integration/pyspark-hive-session.py
```