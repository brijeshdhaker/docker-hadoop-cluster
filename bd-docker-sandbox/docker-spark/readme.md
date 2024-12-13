#
# 1. Create Base images from Spark Distribution 
#

export SPARK_HOME=/apps/spark
cd $SPARK_HOME
./bin/docker-image-tool.sh -t 3.5.0 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build

#
# 2. Create Base images from Spark Distribution
#


#
#
#
$SPARK_HOME/bin/spark-shell \
--jars \
$HIVE_HOME/lib/hive-metastore-3.5.0.jar,\
$HIVE_HOME/lib/hive-exec-3.5.0.jar,\
$HIVE_HOME/lib/hive-common-3.5.0.jar,\
$HIVE_HOME/lib/hive-serde-3.5.0.jar,\
$HIVE_HOME/lib/guava-27.0-jre.jar \
--conf spark.sql.hive.metastore.version=3.5.0 \
--conf spark.sql.hive.metastore.jars=$HIVE_HOME"/lib/*" \
--conf spark.sql.warehouse.dir=hdfs://localhost:9000/user/hive/warehouse

#
#
#
scala> spark.conf.get("spark.sql.catalogImplementation")

#
#
#
scala> spark.sharedState.externalCatalog.listTables("default")

#
#
#
scala> spark.table("demo_sales").show

#
# Linux
#
export SPARK_HOME=/apps/spark-3.5.0
$SPARK_HOME/bin/beeline -u jdbc:hive2://192.168.0.100:10000 scott tiger

DROP DATABASE IF EXISTS SPARK_APPS CASCADE;


#
# Hive Create Table
#

0: jdbc:hive2://192.168.0.100:10000>

DROP TABLE IF EXISTS demo_sales ;

CREATE TABLE demo_sales
(id BIGINT, qty BIGINT, name STRING)
COMMENT 'Demo: Connecting Spark SQL to Hive Metastore'
PARTITIONED BY (rx_mth_cd STRING COMMENT 'Prescription Date YYYYMM aggregated')
STORED AS PARQUET;

#
# INSER DATA IN HIVE Table
#
# (id BIGINT, qty BIGINT, name STRING)
# PARTITIONED BY (rx_mth_cd STRING COMMENT 'Prescription Date YYYYMM aggregated')
INSERT INTO demo_sales PARTITION (rx_mth_cd="202001") VALUES (1, 1000, 'one');
INSERT INTO demo_sales PARTITION (rx_mth_cd="202002") VALUES (2, 2000, 'two');

#
# Query the records in the table.
#

0: jdbc:hive2://localhost:10000> SELECT * FROM demo_sales;

0: jdbc:hive2://localhost:10000> SHOW PARTITIONS demo_sales;

#
#
#
scala> spark.table("demo_sales").show

#
# Display the metadata of the table from the Spark catalog (DESCRIBE EXTENDED SQL command).
#
scala> sql("DESCRIBE EXTENDED demo_sales").show(Integer.MAX_VALUE, truncate = false)


#
# Create Hive Partitioned Table in Parquet Format
#
DROP TABLE IF EXISTS hive_partitioned_table ;

CREATE TABLE hive_partitioned_table
(id BIGINT, name STRING)
COMMENT 'Demo: Hive Partitioned Parquet Table and Partition Pruning'
PARTITIONED BY (city STRING COMMENT 'City')
STORED AS PARQUET;

INSERT INTO hive_partitioned_table
PARTITION (city="Warsaw")
VALUES (0, 'Jacek');

INSERT INTO hive_partitioned_table
PARTITION (city="Paris")
VALUES (1, 'Agata');

#
# Accessing Hive Table in Spark Shell
# DROP TABLE IF EXISTS cities ;

assert(spark.conf.get("spark.sql.warehouse.dir").startsWith("hdfs"))
val tableName = "hive_partitioned_table"
assert(spark.table(tableName).collect.length == 2 /* rows */)
// Use the default value of spark.sql.hive.convertMetastoreParquet
assert(spark.conf.get("spark.sql.hive.convertMetastoreParquet").toBoolean)
val parts = spark.sharedState.externalCatalog.listPartitions("default", tableName)

scala> parts.foreach(println)

#
# Create another Hive table using Spark.
#
Seq("Warsaw").toDF("name").write.saveAsTable("cities")


#
# Check out the table in Hive using beeline.
#
0: jdbc:hive2://localhost:10000> desc formatted cities;

#
# Explore Partition Pruning
#

val q = sql(s"""SELECT * FROM $tableName WHERE city IN ('Warsaw')""")
scala> q.explain(extended = true)

== Parsed Logical Plan ==
'Project [*]
+- 'Filter 'city IN (Warsaw)
+- 'UnresolvedRelation `hive_partitioned_table`

== Analyzed Logical Plan ==
id: bigint, name: string, city: string
Project [id#101L, name#102, city#103]
+- Filter city#103 IN (Warsaw)
+- SubqueryAlias `default`.`hive_partitioned_table`
+- Relation[id#101L,name#102,city#103] parquet

== Optimized Logical Plan ==
Project [id#101L, name#102, city#103]
+- Filter (isnotnull(city#103) && (city#103 = Warsaw))
+- Relation[id#101L,name#102,city#103] parquet

== Physical Plan ==
*(1) FileScan parquet default.hive_partitioned_table[id#101L,name#102,city#103] Batched: true, Format: Parquet, Location: PrunedInMemoryFileIndex[hdfs://localhost:9000/user/hive/warehouse/hive_partitioned_table/city=War..., PartitionCount: 1, PartitionFilters: [isnotnull(city#103), (city#103 = Warsaw)], PushedFilters: [], ReadSchema: struct<id:bigint,name:string>

#
#
Note the PartitionFilters field of the leaf FileScan node in the physical plan. It uses an PrunedInMemoryFileIndex (for the partition index). Let’s explore it.


import org.apache.spark.sql.execution.FileSourceScanExec
val scan = q.queryExecution.executedPlan.collect { case op: FileSourceScanExec => op }.head

val index = scan.relation.location
scala> println(s"Time of partition metadata listing: ${index.metadataOpsTimeNs.get}ns")
Time of partition metadata listing: 41703540ns

// You may also want to review metadataTime metric in web UI
// Includes the above time and the time to list files

// You should see the following value (YMMV)
scan.execute.collect
scala> println(scan.metrics("metadataTime").value)
41

#
# Use a subquery to filter by and note the PartitionFilters field of FileScan operator 
# (which is not supported for partition pruning since the values to filter partitions 
# by are not known until the execution time).
#

val q = sql(s"""SELECT * FROM $tableName WHERE city IN (SELECT * FROM cities)""")
scala> q.explain(extended = true)
== Parsed Logical Plan ==
'Project [*]
+- 'Filter 'city IN (list#104 [])
:  +- 'Project [*]
:     +- 'UnresolvedRelation `cities`
+- 'UnresolvedRelation `hive_partitioned_table`

== Analyzed Logical Plan ==
id: bigint, name: string, city: string
Project [id#113L, name#114, city#115]
+- Filter city#115 IN (list#104 [])
:  +- Project [name#108]
:     +- SubqueryAlias `default`.`cities`
:        +- Relation[name#108] parquet
+- SubqueryAlias `default`.`hive_partitioned_table`
+- Relation[id#113L,name#114,city#115] parquet

== Optimized Logical Plan ==
Join LeftSemi, (city#115 = name#108)
:- Relation[id#113L,name#114,city#115] parquet
+- Relation[name#108] parquet

== Physical Plan ==
*(2) BroadcastHashJoin [city#115], [name#108], LeftSemi, BuildRight
:- *(2) FileScan parquet default.hive_partitioned_table[id#113L,name#114,city#115] Batched: true, Format: Parquet, Location: CatalogFileIndex[hdfs://localhost:9000/user/hive/warehouse/hive_partitioned_table], PartitionCount: 2, PartitionFilters: [], PushedFilters: [], ReadSchema: struct<id:bigint,name:string>
+- BroadcastExchange HashedRelationBroadcastMode(List(input[0, string, true]))
+- *(1) FileScan parquet default.cities[name#108] Batched: true, Format: Parquet, Location: InMemoryFileIndex[hdfs://localhost:9000/user/hive/warehouse/cities], PartitionFilters: [], PushedFilters: [], ReadSchema: struct<name:string>