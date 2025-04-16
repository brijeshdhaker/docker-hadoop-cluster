
#
# Create Table
# 
create 'test_table', 'cf'

#
# Add Data
# 
put 'test_table', 'row1', 'cf:a', 'value-A'
put 'test_table', 'row2', 'cf:b', 'value-B'
put 'test_table', 'row3', 'cf:c', 'value-C'

#
# Delete Record
#
deleteall 'emp', '1'

#
# List Tables
# 
list

### 4.
list 'test_table'

### 5.
describe 'test_table'

### 6.
scan 'test_table'

### 7.
get 'test_table', 'row1'

### 8. Disable Table
disable 'test_table'

### 9. Drop Table
drop 'test_table'

### Grant
# sudo -u hbase kinit -kt /etc/security/keytabs/hbase.service.keytab hbase/hive-spike.example.com@EXAMPLE.COM
# sudo -u hbase hbase shell

hbase(main):001:0> grant 'brijeshdhaker','RWCA'

## Read HBase table from PySpark

===============================================================================================================================

Ono of the Real-time Project Scenario is read HBase from PySpark | Part 1 | Hands-On

Step 1: Create HBase table

create 'transaction_detail_hbase_tbl','txn_data','cust_data'
```commandline
$ hbase shell
Java HotSpot(TM) 64-Bit Server VM warning: Using incremental CMS is deprecated and will likely be removed in a future release
HBase Shell
Use "help" to get list of supported commands.
Use "exit" to quit this interactive shell.
For Reference, please visit: http://hbase.apache.org/2.0/book.html#shell
Version 2.1.0-cdh6.2.0, rUnknown, Wed Mar 13 23:39:58 PDT 2019
Took 0.0041 seconds
$ hbase(main):001:0>
$hbase(main):002:0> create 'transaction_detail_hbase_tbl','txn_data','cust_data'
Created table transaction_detail_hbase_tbl
Took 5.7059 seconds
=> Hbase::Table - transaction_detail_hbase_tbl
hbase(main):003:0>
```

### Step 2: Insert/Put few records to HBase table
```commandline

put 'transaction_detail_hbase_tbl','1','txn_data:uuid','f5933a1b-7e40-46ec-a512-b69f3355a3bc'
put 'transaction_detail_hbase_tbl','1','txn_data:amount','50.85'
put 'transaction_detail_hbase_tbl','1','txn_data:cardtype','MasterCard'
put 'transaction_detail_hbase_tbl','1','txn_data:website','www.ebay.com'
put 'transaction_detail_hbase_tbl','1','txn_data:product','Laptop'
put 'transaction_detail_hbase_tbl','1','cust_data:city','Mumbai'
put 'transaction_detail_hbase_tbl','1','cust_data:country','India'
put 'transaction_detail_hbase_tbl','1','txn_data:addts','1670229342'
put 'transaction_detail_hbase_tbl','1','txn_data:txn_receive_date','2022-12-05'


put 'transaction_detail_hbase_tbl','2','txn_data:uuid','d8bc8036-dd5a-4866-a7c0-25b01aa466b1'
put 'transaction_detail_hbase_tbl','2','txn_data:amount','259.12'
put 'transaction_detail_hbase_tbl','2','txn_data:cardtype','MasterCard'
put 'transaction_detail_hbase_tbl','2','txn_data:website','www.amazon.com'
put 'transaction_detail_hbase_tbl','2','txn_data:product','Wrist Band'
put 'transaction_detail_hbase_tbl','2','cust_data:city','Pune'
put 'transaction_detail_hbase_tbl','2','cust_data:country','India'
put 'transaction_detail_hbase_tbl','2','txn_data:addts','1670229344'
put 'transaction_detail_hbase_tbl','2','txn_data:txn_receive_date','2022-12-05'

put 'transaction_detail_hbase_tbl','3','txn_data:uuid','8fa465b4-90ed-4729-b4c3-f1991257d00c'
put 'transaction_detail_hbase_tbl','3','txn_data:amount','328.16'
put 'transaction_detail_hbase_tbl','3','txn_data:cardtype','MasterCard'
put 'transaction_detail_hbase_tbl','3','txn_data:website','www.flipkart.com'
put 'transaction_detail_hbase_tbl','3','txn_data:product','TV Stand'
put 'transaction_detail_hbase_tbl','3','cust_data:city','New York City'
put 'transaction_detail_hbase_tbl','3','cust_data:country','United States'
put 'transaction_detail_hbase_tbl','3','txn_data:addts','1670229346'
put 'transaction_detail_hbase_tbl','3','txn_data:txn_receive_date','2022-12-05'

put 'transaction_detail_hbase_tbl','4','txn_data:uuid','55bd7efe-2396-44d9-a132-6b707f1839a7'
put 'transaction_detail_hbase_tbl','4','txn_data:amount','399.06'
put 'transaction_detail_hbase_tbl','4','txn_data:cardtype','Visa'
put 'transaction_detail_hbase_tbl','4','txn_data:website','www.snapdeal.com'
put 'transaction_detail_hbase_tbl','4','txn_data:product','TV Stand'
put 'transaction_detail_hbase_tbl','4','cust_data:city','New Delhi'
put 'transaction_detail_hbase_tbl','4','cust_data:country','India'
put 'transaction_detail_hbase_tbl','4','txn_data:addts','1670229348'
put 'transaction_detail_hbase_tbl','4','txn_data:txn_receive_date','2022-12-05'

put 'transaction_detail_hbase_tbl','5','txn_data:uuid','65ff481a-fdb3-4135-9cfc-bede42984252'
put 'transaction_detail_hbase_tbl','5','txn_data:amount','194.52'
put 'transaction_detail_hbase_tbl','5','txn_data:cardtype','Visa'
put 'transaction_detail_hbase_tbl','5','txn_data:website','www.ebay.com'
put 'transaction_detail_hbase_tbl','5','txn_data:product','External Hard Drive'
put 'transaction_detail_hbase_tbl','5','cust_data:city','Rome'
put 'transaction_detail_hbase_tbl','5','cust_data:country','Italy'
put 'transaction_detail_hbase_tbl','5','txn_data:addts','1670229350'
put 'transaction_detail_hbase_tbl','5','txn_data:txn_receive_date','2022-12-05'

```

### Step 3: Create Hive table pointing to HBase table using HBaseStorageHandler
```commandline

$ beeline -u jdbc:hive2://hive-server:10000 scott tiger
0: jdbc:hive2://hive-server:10000> 

drop table transaction_detail_hive_tbl;

CREATE EXTERNAL TABLE transaction_detail_hive_tbl(
    id string,
    uuid string, 
    cardtype string, 
    website string, 
    product string, 
    amount string, 
    city string, 
    country string,
    addts string,
    txn_receive_date string
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ("hbase.columns.mapping"=":key,txn_data:uuid,txn_data:cardtype,txn_data:website,txn_data:product,txn_data:amount,cust_data:city,cust_data:country,txn_data:addts,txn_data:txn_receive_date")
TBLPROPERTIES (
"hbase.table.name"="transaction_detail_hbase_tbl",
"hbase.mapred.output.outputtable"="transaction_detail_hbase_tbl",
"creator"="Brijesh K Dhaker"
);

```

###
```
0: jdbc:hive2://> describe formatted transaction_detail_hive_tbl;
OK
+-------------------------------------+------------+----------+
|              col_name               | data_type  | comment  |
+-------------------------------------+------------+----------+
| transaction_id                      | int        |          |
| cardtype                            | string     |          |
| website                             | string     |          |
| product                             | string     |          |
| addts                               | string     |          |
| amount                              | double     |          |
| city                                | string     |          |
| country                             | string     |          |
+-------------------------------------+------------+----------+
8 rows selected (0.057 seconds)
0: jdbc:hive2://> 

```


### Step 4: Query Hive table from Hive CLI or Hue browser to verify Hive table and HBase table integration is working
```commandline
0: jdbc:hive2://hive-server:10000> select * from transaction_detail_hive_tbl;
```

### ===============================================================================================================================

Ono of the Real-time Project Scenario is read HBase from PySpark | Part 2 | Hands-On

### Step 1: Create SparkSession object with Hive enable option in PySpark program
```python
from pyspark.sql import SparkSession
spark = SparkSession \
    .builder \
    .appName("Read HBase Table using PySpark Demo") \
    .config("spark.jars", "/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hive/lib/hive-hbase-handler-2.1.1-cdh6.3.2.jar") \
    .config("spark.executor.extraClassPath", "/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hive/lib/hive-hbase-handler-2.1.1-cdh6.3.2.jar") \
    .config("spark.executor.extraLibrary", "/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hive/lib/hive-hbase-handler-2.1.1-cdh6.3.2.jar") \
    .config("spark.driver.extraClassPath", "/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hive/lib/hive-hbase-handler-2.1.1-cdh6.3.2.jar") \
    .enableHiveSupport()\
    .getOrCreate()
```

### Step 2: Read/query Hive table using SparkSession object which internally uses HiveContext to make Hive connection(Metastore) and get the records
```python

spark.sql("show tables").show()
spark.sql("use default")
spark.sql("select * from transaction_detail_hive_tbl").show()

```

### Step 3: As usual do some analysis on the data in the DataFrame from Hive table


DataSet used:
-------------
```commandline
+--------------+---------------------+----------------------------------+------------------------+--------------------+------------------+---------------------+------------------------+
|transaction_id|cardtype             |website                           |product                 |addts               |amount            |city                 |country                 |
+--------------+---------------------+----------------------------------+------------------------+--------------------+------------------+---------------------+------------------------+
|             1|           MasterCard|                      www.ebay.com|                  Laptop| 2019-05-14 15:24:12|             50.85|               Mumbai|                   India|
|             2|           MasterCard|                    www.amazon.com|              Wrist Band| 2019-05-14 15:24:13|            259.12|                 Pune|                   India|
|             3|           MasterCard|                  www.flipkart.com|                TV Stand| 2019-05-14 15:24:14|            328.16|        New York City|           United States|
|             4|                 Visa|                  www.snapdeal.com|                TV Stand| 2019-05-14 15:24:15|            399.06|            New Delhi|                   India|
|             5|                 Visa|                      www.ebay.com|     External Hard Drive| 2019-05-14 15:24:16|            194.52|                 Rome|                   Italy|
+--------------+---------------------+----------------------------------+------------------------+--------------------+------------------+---------------------+------------------------+
```


### Reference link for other option for reading HBase table using Spark/PySpark:

https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-using-spark-query-hbase
https://mapr.com/developer-portal/mapr-tutorials/loading-hbase-tables-spark

#
# Run Spark Application
#
spark-submit \
--name "PySpark SHC Hbase Demo" \
--master local[*] \
--packages com.hortonworks:shc-core:1.1.1-2.1-s_2.11 \
--repositories http://repo.hortonworks.com/content/groups/public/ \
--files /opt/sandbox/hbase-2.4.9/conf/hbase-site.xml \
/home/brijeshdhaker/IdeaProjects/pyspark-hbase-integration/pyspark-shc-hbase.py

#
### Run using Spark HBase Connector ( hbase-spark )
#

<!-- https://mvnrepository.com/artifact/org.apache.hbase.connectors.spark/hbase-spark -->
<dependency>
    <groupId>org.apache.hbase.connectors.spark</groupId>
    <artifactId>hbase-spark</artifactId>
    <version>1.0.0</version>
</dependency>

spark-submit \
--name "PySpark Hbase Spark Demo" \
--master local[*] \
--packages org.apache.hbase.connectors.spark:hbase-spark:1.0.0 \
--repositories https://repo1.maven.org/maven2/ \
--files /opt/sandbox/hbase-2.4.9/conf/hbase-site.xml \
/home/brijeshdhaker/IdeaProjects/pyspark-hbase-integration/pyspark-spark-hbase.py

#
### Run using Cloudera “hbase-spark” connector
#
<!-- https://mvnrepository.com/artifact/org.apache.hbase/hbase-spark -->
<dependency>
    <groupId>org.apache.hbase</groupId>
    <artifactId>hbase-spark</artifactId>
    <version>2.1.0-cdh6.3.2</version>
</dependency>

spark-submit \
--name "PySpark Hbase Spark Demo" \
--master local[*] \
--packages org.apache.hbase:hbase-spark:2.1.0-cdh6.3.2 \
--repositories https://repository.cloudera.com/content/repositories/releases/ \
--files /opt/sandbox/hbase-2.4.9/conf/hbase-site.xml \
/home/brijeshdhaker/IdeaProjects/pyspark-hbase-integration/pyspark-spark-hbase.py

#
### Run using Hortonworks “hbase-spark” connector
#

<!-- https://mvnrepository.com/artifact/org.apache.hbase/hbase-spark -->
<dependency>
    <groupId>org.apache.hbase</groupId>
    <artifactId>hbase-spark</artifactId>
    <version>2.1.6.3.1.7.0-79</version>
</dependency>

spark-submit \
--name "PySpark Hbase Spark Demo" \
--master local[*] \
--packages org.apache.hbase:hbase-spark:2.1.6.3.1.7.0-79 \
--repositories 	https://repo.hortonworks.com/content/repositories/releases/ \
--files /opt/sandbox/hbase-2.4.9/conf/hbase-site.xml \
/home/brijeshdhaker/IdeaProjects/pyspark-hbase-integration/pyspark-spark-hbase.py


spark-submit \
--name "PySpark Hbase Spark Demo" \
--master local[*] \
/home/brijeshdhaker/IdeaProjects/pyspark-hbase-integration/pyspark-spark-hbase.py


#
# Run using “hbase-spark” connector
#
spark-submit \
--name "Sample Spark Application" \
--master local[*] \
--jars /opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hbase/hbase-spark-2.1.0-cdh6.3.2.jar \
--files /opt/sandbox/hbase-2.4.9/conf/hbase-site.xml \
/home/brijeshdhaker/IdeaProjects/pyspark-data-pipelines/com/example/spark/streams/stream-hbase-transformer.py

#
## Spark & Hbase Integration
#

#### 1. Create Hbase Table with String Key

```commandline # Create Books Table
$ hbase> 
create 'books', 'info', 'analytics'
put 'books', 'In Search of Lost Time', 'info:author', 'Marcel Proust'
put 'books', 'In Search of Lost Time', 'info:year', '1922'
put 'books', 'In Search of Lost Time', 'analytics:views', '3298'
put 'books', 'Godel, Escher, Bach', 'info:author', 'Douglas Hofstadter'
put 'books', 'Godel, Escher, Bach', 'info:year', '1979'
put 'books', 'Godel, Escher, Bach', 'analytics:views', '820'

## The table returns the following result when scanning:
$ hbase> scan 'books'
ROW                                              COLUMN+CELL                                                                                                                                 
 Godel, Escher, Bach                             column=analytics:views, timestamp=2021-12-26T09:44:17.455, value=820                                                                        
 Godel, Escher, Bach                             column=info:author, timestamp=2021-12-26T09:44:15.823, value=Douglas Hofstadter                                                             
 Godel, Escher, Bach                             column=info:year, timestamp=2021-12-26T09:44:15.844, value=1979                                                                             
 In Search of Lost Time                          column=analytics:views, timestamp=2021-12-26T09:44:15.798, value=3298                                                                       
 In Search of Lost Time                          column=info:author, timestamp=2021-12-26T09:44:15.761, value=Marcel Proust                                                                  
 In Search of Lost Time                          column=info:year, timestamp=2021-12-26T09:44:15.780, value=1922                                                                             
2 row(s)
Took 0.1034 seconds 
```

#### 2. Create Hbase Table with Integer Key
```
# Create Person Table
$ hbase>
create 'Person', 'Name', 'Address'
put 'Person', '1', 'Name:First', 'Raymond'
put 'Person', '1', 'Name:Last', 'Tang'
put 'Person', '1', 'Address:Country', 'Australia'
put 'Person', '1', 'Address:State', 'VIC'

put 'Person', '2', 'Name:First', 'Dnomyar'
put 'Person', '2', 'Name:Last', 'Gnat'
put 'Person', '2', 'Address:Country', 'USA'
put 'Person', '2', 'Address:State', 'CA'

## The table returns the following result when scanning:
$ hbase> scan 'Person'
ROW                             COLUMN+CELL
1                              column=Address:Country, timestamp=2021-02-05T20:48:42.088, value=Australia
1                              column=Address:State, timestamp=2021-02-05T20:48:46.750, value=VIC
1                              column=Name:First, timestamp=2021-02-05T20:48:32.544, value=Raymond
1                              column=Name:Last, timestamp=2021-02-05T20:48:37.085, value=Tang
2                              column=Address:Country, timestamp=2021-02-05T20:49:00.692, value=USA
2                              column=Address:State, timestamp=2021-02-05T20:49:04.972, value=CA
2                              column=Name:First, timestamp=2021-02-05T20:48:51.653, value=Dnomyar
2                              column=Name:Last, timestamp=2021-02-05T20:48:56.665, value=Gnat
2 row(s)
```

#
## 1. Using Hive External Tables
#

### 1.1 Create Hive External Table
```
$ hive>

CREATE EXTERNAL TABLE IF NOT EXISTS `default`.`books_ext` (
    `title` string,
    `author` string,
    `year` int,
    `views` double
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.hbase.HBaseSerDe'
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
   'hbase.columns.mapping'=':key,info:author,info:year,analytics:views'
)
TBLPROPERTIES (
    'hbase.mapred.output.outputtable'='books',
    'hbase.table.name'='books'
);

##
CREATE EXTERNAL TABLE IF NOT EXISTS `default`.`person_hbase` (
    `id`    int,  
    `fname` string,
    `lname` string,
    `state` string,
    `country` string
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.hbase.HBaseSerDe'
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
   'hbase.columns.mapping'=':key,Name:First,Name:Last,Address:State,Address:Country'
)
TBLPROPERTIES (
    'hbase.mapred.output.outputtable'='Person',
    'hbase.table.name'='Person'
);

```
### 1.2 Query Hive table from Spark using the SQLContext or Spark session:
```
spark-shell --jars file:///opt/hive-3.5.0/lib/hive-hbase-handler-3.5.0.jar
spark-shell --jars file:///opt/hive-2.3.9/lib/hive-hbase-handler-2.3.9.jar

spark.table("default.books_ext").show()

+--------------------+------------------+----+------+
|               title|            author|year| views|
+--------------------+------------------+----+------+
| Godel, Escher, Bach|Douglas Hofstadter|1979| 820.0|
|In Search of Lost...|     Marcel Proust|1922|3298.0|
+--------------------+------------------+----+------+

spark.table("default.person_hbase").show()

+---+-------+-----+-----+---------+
| id|  fname|lname|state|  country|
+---+-------+-----+-----+---------+
|  1|Raymond| Tang|  VIC|Australia|
|  2|Dnomyar| Gnat|   CA|      USA|
+---+-------+-----+-----+---------+


// for optimal performance
spark.sql('SET hbase.scan.cache=10000')
spark.sql('SET hbase.client.scanner.cache=10000')
```

Note : The last point means that accessing HBase from Spark through Hive is only a good option when doing operations on the entire table, such as full table scans. Otherwise, keep reading!

#
## 2. Using Spark-HBase Connector
#

### 1. Build HBase Spark connector
```
   git clone https://github.com/apache/hbase-connectors.git
   cd hbase-connectors/
   
   #
   mvn -Dspark.version=2.3.4 -Dscala.version=2.11.12 -Dscala.binary.version=2.11 -Dhbase.version=2.2.4 -Dhadoop.profile=2.0 -Dhadoop-three.version=2.10.1 -DskipTests -Dcheckstyle.skip -U clean package
   
   # for spark 2.4.0
   mvn -Dspark.version=2.4.8 -Dscala.version=2.11.12 -Dscala.binary.version=2.11 clean install
   
   # for spark 3.2.1
   mvn -Dspark.version=3.0.1 -Dscala.version=2.12.10 -Dscala.binary.version=2.12 -Dhbase.version=2.2.4 -Dhadoop.profile=3.0 -Dhadoop-three.version=3.2.0 -DskipTests -Dcheckstyle.skip -U clean package
   
   # for spark 3.2.1
   mvn -Dspark.version=3.0.1 -Dscala.version=2.12.10 -Dscala.binary.version=2.12 -Dhbase.version=2.4.9 -Dhadoop.profile=3.0 -Dhadoop-three.version=3.3.4 -DskipTests -Dcheckstyle.skip -U clean package
   
   The version arguments need to match with your Hadoop, Spark and HBase versions.
```

### 2. Run Sparkshell
```

spark-shell --jars /home/brijeshdhaker/IdeaProjects/hbase-connectors/spark/hbase-spark/target/hbase-spark-1.0.1-SNAPSHOT.jar
export HBASE_CONN_PATH=/opt/sandbox/spark-2.4.8/hbase
export HBASE_CLASSPATH=$HBASE_CONN_PATH/hbase-spark-1.0.1_2.11-2.4.8.jar:$HBASE_CONN_PATH/hbase-spark-it-1.0.1_2.11-2.4.8.jar:$HBASE_CONN_PATH/hbase-spark-protocol-1.0.1_2.11-2.4.8.jar:$HBASE_CONN_PATH/hbase-spark-protocol-shaded-1.0.1_2.11-2.4.8.jar
spark-shell --jars $HBASE_CONN_PATH/hbase-spark-1.0.1_2.11-2.4.8.jar,$HBASE_CONN_PATH/hbase-spark-it-1.0.1_2.11-2.4.8.jar,$HBASE_CONN_PATH/hbase-spark-protocol-1.0.1_2.11-2.4.8.jar,$HBASE_CONN_PATH/hbase-spark-protocol-shaded-1.0.1_2.11-2.4.8.jar

```

#### 1) First import the required classes:
```
import org.apache.hadoop.hbase.spark.HBaseContext
import org.apache.hadoop.hbase.HBaseConfiguration
val conf = HBaseConfiguration.create()
conf.set("hbase.zookeeper.quorum", "127.0.0.1:2181")
conf.set("hbase.zookeeper.property.clientPort","2181")
conf.set("hbase.spark.config.location", "file:///opt/sandbox/hbase-2.4.9/conf/hbase-site.xml")

## Create HBase context 
new HBaseContext(spark.sparkContext, conf)

## Create DataFram for Book Table
val bookDF = spark.read.format("org.apache.hadoop.hbase.spark")
.option("hbase.columns.mapping","title STRING :key, author STRING info:author, year STRING info:year, views STRING analytics:views")
.option("hbase.table", "books")
.option("hbase.use.hbase.context", false)
.option("hbase.config.resources", "file:///opt/sandbox/hbase-2.4.9/conf/hbase-site.xml") 
.option("hbase-push.down.column.filter", false) 
.load()

bookDF.printSchema()
bookDF.show()

## Create DataFrame for Person Table
val personDF = spark.read.format("org.apache.hadoop.hbase.spark")
  .option("hbase.columns.mapping","rowKey STRING :key, firstName STRING Name:First, lastName STRING Name:Last, country STRING Address:Country, state STRING Address:State")
  .option("hbase.table", "Person")
  .option("hbase.use.hbase.context", false)
  .option("hbase.config.resources", "file:///opt/sandbox/hbase-2.4.9/conf/hbase-site.xml") 
  .option("hbase-push.down.column.filter", false)
  .load()

The columns mapping matches with the definition in the steps above.
```
#### 5) Show DataFrame schema
```
   scala> personDF.schema
   res2: org.apache.spark.sql.types.StructType = StructType(StructField(lastName,StringType,true), StructField(country,StringType,true), StructField(state,StringType,true), StructField(firstName,StringType,true), StructField(rowKey,StringType,true))
```
#### 6) Show data
```
scala> personDF.show()
```

#### Use catalog

We can also define a catalog for the table Person created above and then use it to read data.

##### 1) Define catalog

def catalog = s"""{
|"table":{"namespace":"default", "name":"Person"},
|"rowkey":"key",
|"columns":{
|"rowkey":{"cf":"rowkey", "col":"key", "type":"string"},
|"firstName":{"cf":"Name", "col":"First", "type":"string"},
|"lastName":{"cf":"Name", "col":"Last", "type":"string"},
|"country":{"cf":"Address", "col":"Country", "type":"string"},
|"state":{"cf":"Address", "col":"State", "type":"string"}
|}
|}""".stripMargin

##### 2) Use catalog

Now the catalog can be directly passed into as tableCatalog option:

import org.apache.hadoop.hbase.spark.datasources._

var hbaseDF = spark.read.options(Map(HBaseTableCatalog.tableCatalog->catalog)).format("org.apache.hadoop.hbase.spark").load()
hbaseDF.show()

The code can also be simplified as:

var hbaseDF = spark.read.format("org.apache.hadoop.hbase.spark").option("catalog",catalog).load()
hbaseDF.show()

## Output:
+--------+------+---------+-----+---------+
|lastName|rowkey|  country|state|firstName|
+--------+------+---------+-----+---------+
|    Tang|     1|Australia|  VIC|  Raymond|
|    Gnat|     2|      USA|   CA|  Dnomyar|
+--------+------+---------+-----+---------+

## Summary
Unfortunately the connector packages for Spark 3.x are not published to Maven central repositories yet.
To save time for building hbase-connector project, you can download it from the ones I built using WSL: Release 1.0.1 HBase Connectors for Spark 3.0.1 · kontext-tech/hbase-connectors.


#
## 3. PySpark & Hbase with Hortonworks SHC connector
#

### 1. copy hbase-site.xml to $SPARK_HOME/conf/ directory

cp /usr/hdp/current/hbase-client/conf/hbase-site.xml spark2-client/conf/hbase-site.xml
#  export SPARK_CLASSPATH=/usr/hdp/current/hbase-client/lib/hbase-common.jar:/usr/hdp/current/hbase-client/lib/hbase-client.jar:/usr/hdp/current/hbase-client/lib/hbase-server.jar:/usr/hdp/current/hbase-client/lib/hbase-protocol.jar:/usr/hdp/current/hbase-client/lib/guava-12.0.1.jar

### 2.

pyspark --packages com.hortonworks:shc-core:1.1.1-2.1-s_2.11 --repositories http://repo.hortonworks.com/content/groups/public/ --files /usr/hdp/current/hbase-master/conf/hbase-site.xml

pyspark --packages com.hortonworks:shc-core:1.1.1-2.1-s_2.11 --repositories http://repo.hortonworks.com/content/groups/public/ --files /opt/sandbox/hbase-2.4.9/conf/hbase-site.xml

   <dependency>
     <groupId>com.hortonworks</groupId>
     <artifactId>shc-core</artifactId>
     <version>1.1.3-2.4-s_2.11</version>
   </dependency>

$SPARK_HOME/bin/pyspark --packages com.hortonworks:shc-core:1.1.3-2.4-s_2.11 --repositories http://repo.hortonworks.com/content/groups/public/ --files /opt/sandbox/hbase-2.4.9/conf/hbase-site.xml


### 3. Define Catalog for Hbase Table

### 4.
#### Scala
```   
import org.apache.spark.sql.execution.datasources.hbase._
import org.apache.spark.sql.{DataFrame, SparkSession}

val catalog = "{\"table\": {\"namespace\": \"default\", \"name\": \"books\"}, \"rowkey\": \"key\", \"columns\": {\"title\": {\"cf\": \"rowkey\", \"col\": \"key\", \"type\": \"string\"}, \"author\": {\"cf\": \"info\", \"col\": \"author\", \"type\": \"string\"} } }"

def withCatalog(cat: String): DataFrame = {
  spark.read.options(Map(HBaseTableCatalog.tableCatalog->cat)).format("org.apache.spark.sql.execution.datasources.hbase").load()
}
// Load the dataframe
val df = withCatalog(catalog)
df.show()

//SQL example
df.createOrReplaceTempView("table")
sqlContext.sql("select count(col1) from table").show

```

#### Python

```  
catalog = '{"table": {"namespace": "default", "name": "books"}, "rowkey": "key", "columns": {"title": {"cf": "rowkey", "col": "key", "type": "string"}, "author": {"cf": "info", "col": "author", "type": "string"} } }'
df = spark.read.options(catalog=catalog).format('org.apache.spark.sql.execution.datasources.hbase').load()
df.show()

   +--------------------+------------------+
   |               title|            author|
   +--------------------+------------------+
   | Godel, Escher, Bach|Douglas Hofstadter|
   |In Search of Lost...|     Marcel Proust|
   +--------------------+------------------+
```

### 5.

```
   df.write.options(catalog=catalog) \
   .format("org.apache.spark.sql.execution.datasources.hbase") \
   .save()
```
