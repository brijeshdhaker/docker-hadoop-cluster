
#

![iceberg-logo](https://www.apache.org/logos/res/iceberg/iceberg.png)

### Spark Session catalog : This configures Spark to use Iceberg's SparkSessionCatalog as a wrapper around that session catalog. 
### When a table is not an Iceberg table, the built-in catalog will be used to load it instead.
```
spark.sql.catalog.spark_catalog = org.apache.iceberg.spark.SparkSessionCatalog
spark.sql.catalog.spark_catalog.type = hive
```
### This creates an Iceberg catalog named hive_prod that loads tables from a Hive metastore:
```
spark.sql.catalog.hive_prod = org.apache.iceberg.spark.SparkCatalog
spark.sql.catalog.hive_prod.type = hive
spark.sql.catalog.hive_prod.uri = thrift://metastore-host:port
##### omit uri to use the same URI as Spark: hive.metastore.uris in hive-site.xml
```

### This creates an Iceberg REST catalog named rest_prod that loads tables from REST URL http://localhost:8080:
```
spark.sql.catalog.rest_prod = org.apache.iceberg.spark.SparkCatalog
spark.sql.catalog.rest_prod.type = rest
spark.sql.catalog.rest_prod.uri = http://localhost:8080
```
### This creates an Iceberg catalog in HDFS that can be configured using type=hadoop
```
spark.sql.catalog.hadoop_prod = org.apache.iceberg.spark.SparkCatalog
spark.sql.catalog.hadoop_prod.type = hadoop
spark.sql.catalog.hadoop_prod.warehouse = hdfs://namenode.sandbox.net:9000/warehouse/tablespace/external/spark
```

### Start Spark Shell

```shell
$SPARK_HOME/bin/spark-shell \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--properties-file $SPARK_HOME/conf/spark-iceburg.conf

--packages org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.4.3 \

$SPARK_HOME/bin/spark-sql \
--packages org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.4.3 \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--properties-file $SPARK_HOME/conf/spark-iceburg.conf

$SPARK_HOME/bin/spark-sql --packages org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.4.3 \
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions \
--conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkSessionCatalog \
--conf spark.sql.catalog.spark_catalog.type=hive \
--conf spark.sql.catalog.hadoop_catalog=org.apache.iceberg.spark.SparkCatalog \
--conf spark.sql.catalog.hadoop_catalog.type=hadoop \
--conf spark.sql.catalog.hadoop_catalog.warehouse=/warehouse/tablespace/external/spark \
--conf spark.sql.defaultCatalog=hadoop_catalog

$SPARK_HOME/bin/spark-sql \
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions \
--conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkSessionCatalog \
--conf spark.sql.catalog.spark_catalog.type=hive \
--conf spark.sql.catalog.hadoop_catalog=org.apache.iceberg.spark.SparkCatalog \
--conf spark.sql.catalog.hadoop_catalog.type=hadoop \
--conf spark.sql.catalog.hadoop_catalog.warehouse=/warehouse/tablespace/external/spark \
--conf spark.sql.defaultCatalog=hadoop_catalog \
--conf spark.hadoop.hive.cli.print.header=true

$SPARK_HOME/bin/spark-shell \
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions \
--conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkSessionCatalog \
--conf spark.sql.catalog.spark_catalog.type=hive \
--conf spark.sql.catalog.hadoop_catalog=org.apache.iceberg.spark.SparkCatalog \
--conf spark.sql.catalog.hadoop_catalog.type=hadoop \
--conf spark.sql.catalog.hadoop_catalog.warehouse=/warehouse/tablespace/external/spark \
--conf spark.sql.defaultCatalog=hadoop_catalog

```

# Use Default Spark Catalog
```scala
spark.sql("show tables").show()
spark.sql("USE spark_catalog.default").show()
spark.sql("SELECT * FROM spark_catalog.default.m_students").show()
```

```python
import logging
import os
from pyspark import SparkConf
from pyspark import SparkContext
from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, LongType, DoubleType, StringType

# adding iceberg configs
conf = (
    SparkConf()
    .set("spark.sql.extensions",
         "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions") # Use Iceberg with Spark
    .set("spark.sql.catalog.demo", "org.apache.iceberg.spark.SparkCatalog")
    .set("spark.sql.catalog.demo.io-impl", "org.apache.iceberg.aws.s3.S3FileIO")
    .set("spark.sql.catalog.demo.warehouse", "s3a://openlake/warehouse/")
    .set("spark.sql.catalog.demo.s3.endpoint", "https://play.min.io:50000")
    .set("spark.sql.defaultCatalog", "demo") # Name of the Iceberg catalog
    .set("spark.sql.catalogImplementation", "in-memory")
    .set("spark.sql.catalog.demo.type", "hadoop") # Iceberg catalog type
    .set("spark.executor.heartbeatInterval", "300000")
    .set("spark.network.timeout", "400000")
)

spark.sql("CREATE DATABASE IF NOT EXISTS nyc").show(false)
spark.sql("DESCRIBE DATABASE nyc").show(false)
spark.sql("DESCRIBE DATABASE EXTENDED nyc").show(false)

spark.sql("SHOW TBLPROPERTIES nyc.taxis ;").show(false)
spark.sql("SHOW TBLPROPERTIES nyc.taxis ('current-snapshot-id');").show(false)

val df = spark.read.parquet("s3a://openlake/taxi-data/yellow_tripdata_2021-04.parquet")
df.write.saveAsTable("nyc.taxis")

spark.sql("CREATE TABLE IF NOT EXISTS nyc.taxis").show(false)
spark.sql("DESCRIBE EXTENDED nyc.taxis").show(false)
spark.sql("DESCRIBE FORMATTED nyc.taxis").show(false)

spark.sql("SHOW CREATE TABLE nyc.taxis").show(false)

spark.sql("SELECT COUNT(*) as cnt FROM nyc.taxis").show()
spark.sql("").show()

# Schema Evolution

spark.sql("ALTER TABLE nyc.taxis RENAME COLUMN fare_amount TO fare").show()

spark.sql("ALTER TABLE nyc.taxis RENAME COLUMN trip_distance TO distance").show()

spark.sql("ALTER TABLE nyc.taxis ALTER COLUMN distance COMMENT 'The elapsed trip distance in miles reported by the taximeter.'").show()

spark.sql("ALTER TABLE nyc.taxis ALTER COLUMN distance TYPE double;").show()

spark.sql("ALTER TABLE nyc.taxis ALTER COLUMN distance AFTER fare;").show()

spark.sql("ALTER TABLE nyc.taxis ADD COLUMN fare_per_distance_unit float AFTER distance").show()

spark.sql("UPDATE nyc.taxis SET fare_per_distance_unit = fare/distance").show()

spark.sql("""
SELECT
VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance_unit
FROM nyc.taxis
""").show()

# Expressive SQL for Row Level Changes
spark.sql("""
DELETE FROM nyc.taxis
WHERE fare_per_distance_unit > 4.0 OR distance > 2.0
""").show()

spark.sql("""
DELETE FROM nyc.taxis
WHERE fare_per_distance_unit is null
""").show()

spark.sql("""
SELECT
VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance_unit
FROM nyc.taxis
""").show()

spark.sql("""
SELECT COUNT(*) as cnt
FROM nyc.taxis
""").show()

# Partitioning
spark.sql("""
ALTER TABLE nyc.taxis
ADD PARTITION FIELD VendorID
""").show()

# Metadata Tables
spark.sql("""
SELECT snapshot_id, manifest_list
FROM nyc.taxis.snapshots
""").show(false)

spark.sql("""
SELECT file_path, file_format, record_count, null_value_counts, lower_bounds, upper_bounds
FROM nyc.taxis.files
""").show()

#
val df = spark.sql("""
SELECT *
FROM nyc.taxis.history
""")

val original_snapshot = df.head()(1)

spark.sql("CALL system.rollback_to_snapshot('nyc.taxis', %s)".format(original_snapshot))
original_snapshot



spark.sql("""
SELECT
VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance_unit
FROM nyc.taxis
""").show()

spark.sql("""
SELECT *
FROM nyc.taxis.history
""").show()


spark.sql("").show()
spark.sql("").show()



spark = SparkSession.builder.config(conf=conf).getOrCreate()

# Read CSV file from MinIO
df = spark.read.option("header", "true").csv("s3a://openlake/warehouse/airlines.csv")
df.show()

# Create Iceberg table "hadoop_catalog.flightdb.airlines" from RDD
df.write.mode("overwrite").saveAsTable("hadoop_catalog.flightdb.airlines")

# Query table row count
spark.sql("SELECT COUNT(*) AS cnt FROM hadoop_catalog.flightdb.airlines").show()

# Rename column "fare_amount" in nyc.taxis_large to "fare"
spark.sql("ALTER TABLE hadoop_catalog.flightdb.airlines RENAME COLUMN Code TO ID")

# Rename column "trip_distance" in nyc.taxis_large to "distance"
spark.sql("ALTER TABLE hadoop_catalog.flightdb.airlines RENAME COLUMN Description TO NAME")

# Add description to the new column "distance"
spark.sql("ALTER TABLE hadoop_catalog.flightdb.airlines ALTER COLUMN NAME COMMENT 'The Name of Flight.'")

# Move "distance" next to "fare" column
spark.sql("ALTER TABLE hadoop_catalog.flightdb.airlines ALTER COLUMN distance AFTER fare")

# Add new column "fare_per_distance" of type float
spark.sql("ALTER TABLE hadoop_catalog.flightdb.airlines ADD COLUMN fare_per_distance FLOAT AFTER distance")

# Check the snapshots available
snap_df = spark.sql("SELECT * FROM hadoop_catalog.flightdb.airlines.snapshots")
snap_df.show()  # prints all the available snapshots (1 till now)

# Populate the new column "fare_per_distance"
logger.info("Populating fare_per_distance column...")
spark.sql("UPDATE nyc.taxis_large SET fare_per_distance = fare/distance")

# Check the snapshots available
logger.info("Checking snapshots...")
snap_df = spark.sql("SELECT * FROM nyc.taxis_large.snapshots")
snap_df.show()  # prints all the available snapshots (2 now) since previous operation will create a new snapshot

# Qurey the table to see the results
res_df = spark.sql("""SELECT VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance
FROM nyc.taxis_large LIMIT 15""")
res_df.show()

# Delete rows from "fare_per_distance" based on criteria
logger.info("Deleting rows from fare_per_distance column...")
spark.sql("DELETE FROM nyc.taxis_large WHERE fare_per_distance > 4.0 OR distance > 2.0")
spark.sql("DELETE FROM nyc.taxis_large WHERE fare_per_distance IS NULL")

# Check the snapshots available
logger.info("Checking snapshots...")
snap_df = spark.sql("SELECT * FROM nyc.taxis_large.snapshots")
snap_df.show()  # prints all the available snapshots (4 now) since previous operations will create 2 new snapshots

# Query table row count
count_df = spark.sql("SELECT COUNT(*) AS cnt FROM nyc.taxis_large")
total_rows_count = count_df.first().cnt
logger.info(f"Total Rows for NYC Taxi Data after delete operations: {total_rows_count}")

# Partition table based on "VendorID" column
logger.info("Partitioning table based on VendorID column...")
spark.sql("ALTER TABLE nyc.taxis_large ADD PARTITION FIELD VendorID")

# Query Metadata tables like snapshot, files, history
logger.info("Querying Snapshot table...")
snapshots_df = spark.sql("SELECT * FROM nyc.taxis_large.snapshots ORDER BY committed_at")
snapshots_df.show()  # shows all the snapshots in ascending order of committed_at column

logger.info("Querying Files table...")
files_count_df = spark.sql("SELECT COUNT(*) AS cnt FROM nyc.taxis_large.files")
total_files_count = files_count_df.first().cnt
logger.info(f"Total Data Files for NYC Taxi Data: {total_files_count}")

spark.sql("""SELECT file_path,
file_format,
record_count,
null_value_counts,
lower_bounds,
upper_bounds
FROM nyc.taxis_large.files LIMIT 1""").show()

# Query history table
logger.info("Querying History table...")
hist_df = spark.sql("SELECT * FROM nyc.taxis_large.history")
hist_df.show()

# Time travel to initial snapshot
logger.info("Time Travel to initial snapshot...")
snap_df = spark.sql("SELECT snapshot_id FROM nyc.taxis_large.history LIMIT 1")
spark.sql(f"CALL demo.system.rollback_to_snapshot('nyc.taxis_large', {snap_df.first().snapshot_id})")

# Qurey the table to see the results
res_df = spark.sql("""SELECT VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance
FROM nyc.taxis_large LIMIT 15""")
res_df.show()

# Query history table
logger.info("Querying History table...")
hist_df = spark.sql("SELECT * FROM nyc.taxis_large.history")
hist_df.show()  # 1 new row

# Query table row count
count_df = spark.sql("SELECT COUNT(*) AS cnt FROM nyc.taxis_large")
total_rows_count = count_df.first().cnt
logger.info(f"Total Rows for NYC Taxi Data after time travel: {total_rows_count}")


```

```scala

import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

val schema = StructType( Array(
    StructField("vendor_id", LongType,true),
    StructField("trip_id", LongType,true),
    StructField("trip_distance", FloatType,true),
    StructField("fare_amount", DoubleType,true),
    StructField("store_and_fwd_flag", StringType,true)
))

val df = spark.createDataFrame(spark.sparkContext.emptyRDD[Row],schema)
df.writeTo("hadoop_catalog.nyc.taxis").create()

import org.apache.spark.sql.Row

val schema = spark.table("hadoop_catalog.nyc.taxis").schema
val data = Seq(
  Row(1: Long, 1000371: Long, 1.8f: Float, 15.32: Double, "N": String),
  Row(2: Long, 1000372: Long, 2.5f: Float, 22.15: Double, "N": String),
  Row(2: Long, 1000373: Long, 0.9f: Float, 9.01: Double, "N": String),
  Row(1: Long, 1000374: Long, 8.4f: Float, 42.13: Double, "Y": String)
)
val df = spark.createDataFrame(spark.sparkContext.parallelize(data), schema)
df.writeTo("hadoop_catalog.nyc.taxis").append()

val df = spark.table("hadoop_catalog.nyc.taxis").show()

//

SELECT * FROM hadoop_catalog.nyc.taxis; -- catalog: prod, namespace: db, table: table
val df = spark.table("hadoop_catalog.nyc.taxis")
```
# Listing Metadata
```scala
// History
spark.sql("SELECT * FROM hadoop_catalog.nyc.taxis.history").show(false);

//Metadata Log Entries
spark.sql("SELECT * from hadoop_catalog.nyc.taxis.metadata_log_entries").show(false);

// Snapshots
spark.sql("SELECT * FROM hadoop_catalog.nyc.taxis.snapshots;").show(false)

//
spark.sql("SELECT * FROM hadoop_catalog.nyc.taxis.partitions").show(false)

//
spark.sql("SELECT * FROM hadoop_catalog.nyc.taxis.refs").show(false)

// Inspecting with DataFrames
// named metastore table
spark.read.format("iceberg").load("nyc.taxis.files")

// Hadoop path table
spark.read.format("iceberg").load("s3a://openlake/warehouse/nyc/taxis#files").show()

// Hadoop path table
spark.read.format("iceberg").load("s3a://openlake/warehouse/nyc/taxis#snapshots").show()

```

# Time Travel with Metadata Tables

```scala
// get the table's file manifests at timestamp Sep 20, 2021 08:00:00
spark.sql("SELECT * FROM iceberg.nyc.taxis.manifests TIMESTAMP AS OF '2021-09-20 08:00:00'")

// get the table's partitions with snapshot id 10963874102873L
spark.sql("SELECT * FROM iceberg.nyc.taxis.partitions VERSION AS OF 10963874102873;")

-- time travel to October 26, 1986 at 01:21:00
SELECT * FROM iceberg.nyc.taxis TIMESTAMP AS OF '1986-10-26 01:21:00';

-- time travel to snapshot with id 10963874102873L
SELECT * FROM iceberg.nyc.taxis VERSION AS OF 10963874102873;

-- time travel to the head snapshot of audit-branch
SELECT * FROM iceberg.nyc.taxis VERSION AS OF 'audit-branch';

-- time travel to the snapshot referenced by the tag historical-snapshot
SELECT * FROM iceberg.nyc.taxis VERSION AS OF 'historical-snapshot';


SELECT * FROM iceberg.nyc.taxis FOR SYSTEM_TIME AS OF '1986-10-26 01:21:00';
SELECT * FROM iceberg.nyc.taxis FOR SYSTEM_VERSION AS OF 10963874102873;
SELECT * FROM iceberg.nyc.taxis FOR SYSTEM_VERSION AS OF 'audit-branch';
SELECT * FROM iceberg.nyc.taxis FOR SYSTEM_VERSION AS OF 'historical-snapshot';

-- timestamp in seconds
  SELECT * FROM iceberg.nyc.taxis TIMESTAMP AS OF 499162860;
  SELECT * FROM iceberg.nyc.taxis FOR SYSTEM_TIME AS OF 499162860;

// load the table's file metadata at snapshot-id 10963874102873 as DataFrame
spark.read.format("iceberg").option("snapshot-id", 10963874102873L).load("db.table.files")

// time travel to snapshot with ID 10963874102873L
spark.read
  .option("snapshot-id", 10963874102873L)
  .format("iceberg")
  .load("/warehouse/tablespace/external/spark/nyc/taxis")

// time travel to tag historical-snapshot
spark.read
  .option(SparkReadOptions.TAG, "historical-snapshot")
  .format("iceberg")
  .load("/warehouse/tablespace/external/spark/nyc/taxis")

// time travel to the head snapshot of audit-branch
spark.read
  .option(SparkReadOptions.BRANCH, "audit-branch")
  .format("iceberg")
  .load("/warehouse/tablespace/external/spark/nyc/taxis")

// get the data added after start-snapshot-id (10963874102873L) until end-snapshot-id (63874143573109L)
spark.read()
  .format("iceberg")
  .option("start-snapshot-id", "10963874102873")
  .option("end-snapshot-id", "63874143573109")
  .load("/warehouse/tablespace/external/spark/nyc/taxis")

```

