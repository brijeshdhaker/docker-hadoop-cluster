#
# Hive & Hbase Integration
#

## 1. add following property in hive-site.xml

<property>
<name>hive.aux.jars.pathh</name>
<value>
	file:///opt/hbase-2.4.6/lib/commons-lang3-3.9.jar,
	file:///opt/hbase-2.4.6/lib/hbase-zookeeper-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-mapreduce-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/jackson-annotations-2.10.1.jar,
	file:///opt/hbase-2.4.6/lib/hbase-shaded-miscellaneous-3.5.1.jar,
	file:///opt/hbase-2.4.6/lib/jackson-databind-2.10.1.jar,
	file:///opt/hbase-2.4.6/lib/hbase-hadoop-compat-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-metrics-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-client-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-protocol-shaded-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/jackson-core-2.10.1.jar,
	file:///opt/hbase-2.4.6/lib/protobuf-java-2.5.0.jar,
	file:///opt/hbase-2.4.6/lib/hbase-shaded-netty-3.5.1.jar,
	file:///opt/hbase-2.4.6/lib/metrics-core-3.2.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-server-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-hadoop2-compat-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-metrics-api-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-common-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-protocol-2.4.6.jar,
	file:///opt/hbase-2.4.6/lib/hbase-shaded-protobuf-2.1.0.jar,
	file:///opt/hbase-2.4.6/lib/client-facing-thirdparty/htrace-core4-4.2.0-incubating.jar,
	file:///opt/hbase-2.4.6/lib/zookeeper-3.5.7.jar
</value>
</property>

## 2. Copy $HBASE_HOME/conf/hbase-site.xml to $HIVE_HOME/conf/hbase-site.xml

    cp HBASE_HOME/conf/hbase-site.xml $HIVE_HOME/conf/hbase-site.xml

## 3. Create Hive Table

    CREATE TABLE hbase_table_emp(id int, name string, role string) 
    STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
    WITH SERDEPROPERTIES ("hbase.columns.mapping" = ":key,cf1:name,cf1:role")
    TBLPROPERTIES ("hbase.table.name" = "emp");

Lets verify this table emp in HBase shell and view its metadata.

    $ hbase shell
    hbase> list
    hbase> describe 'emp'



## 4. Create Temp Hive Table for Data Insert

### We can not directly load data into hbase table “emp” with load data inpath hive command. We have to copy data into it from another Hive table. Lets create another test hive table with the same schema as hbase_table_emp and we will insert records into it with hive load data input command.


    hive> create table testemp(id int, name string, role string) row format delimited fields terminated by '\t';
    hive> load data local inpath '/home/brijeshdhaker/sample.txt' into table testemp;
    hive> select * from testemp;

## 5. Insert Data into Hbase Hive Table

    hive> insert overwrite table hbase_table_emp select * from testemp;
    hive> select * from hbase_table_emp;

# Mapping Existing HBase Tables to Hive

## Create Hbase Table

    create 'user', 'cf1', 'cf2'
    put 'user', 'row1', 'cf1:a', 'value1'
    put 'user', 'row1', 'cf1:b', 'value2'
    put 'user', 'row1', 'cf2:c', 'value3'
    put 'user', 'row2', 'cf2:c', 'value4'
    put 'user', 'row2', 'cf1:b', 'value5'
    put 'user', 'row3', 'cf1:a', 'value6'
    put 'user', 'row3', 'cf2:c', 'value7'

## Create HIVE Table Using Hbase Table

    CREATE EXTERNAL TABLE hbase_table_user(key string, val1 string, val2 string, val3 string)
    STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
    WITH SERDEPROPERTIES ("hbase.columns.mapping" = "cf1:a,cf1:b,cf2:c")
    TBLPROPERTIES("hbase.table.name" = "user");



