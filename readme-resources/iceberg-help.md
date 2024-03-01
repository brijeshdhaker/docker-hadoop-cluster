```shell

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
--conf "spark.hadoop.hive.cli.print.header=true"

$SPARK_HOME/bin/spark-shell \
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions \
--conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkSessionCatalog \
--conf spark.sql.catalog.spark_catalog.type=hive \
--conf spark.sql.catalog.hadoop_catalog=org.apache.iceberg.spark.SparkCatalog \
--conf spark.sql.catalog.hadoop_catalog.type=hadoop \
--conf spark.sql.catalog.hadoop_catalog.warehouse=/warehouse/tablespace/external/spark \
--conf spark.sql.defaultCatalog=hadoop_catalog

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
spark.read.format("iceberg").load("hdfs://namenode.sandbox.net:9000/warehouse/tablespace/external/spark/nyc/taxis#files").show()

// Hadoop path table
spark.read.format("iceberg").load("hdfs://namenode.sandbox.net:9000/warehouse/tablespace/external/spark/nyc/taxis#snapshots").show()

```

# Time Travel with Metadata Tables

```scala
// get the table's file manifests at timestamp Sep 20, 2021 08:00:00
spark.sql("SELECT * FROM hadoop_catalog.nyc.taxis.manifests TIMESTAMP AS OF '2021-09-20 08:00:00'")

// get the table's partitions with snapshot id 10963874102873L
spark.sql("SELECT * FROM hadoop_catalog.nyc.taxis.partitions VERSION AS OF 10963874102873;")

-- time travel to October 26, 1986 at 01:21:00
SELECT * FROM hadoop_catalog.nyc.taxis TIMESTAMP AS OF '1986-10-26 01:21:00';

-- time travel to snapshot with id 10963874102873L
SELECT * FROM hadoop_catalog.nyc.taxis VERSION AS OF 10963874102873;

-- time travel to the head snapshot of audit-branch
SELECT * FROM hadoop_catalog.nyc.taxis VERSION AS OF 'audit-branch';

-- time travel to the snapshot referenced by the tag historical-snapshot
SELECT * FROM hadoop_catalog.nyc.taxis VERSION AS OF 'historical-snapshot';


SELECT * FROM hadoop_catalog.nyc.taxis FOR SYSTEM_TIME AS OF '1986-10-26 01:21:00';
SELECT * FROM hadoop_catalog.nyc.taxis FOR SYSTEM_VERSION AS OF 10963874102873;
SELECT * FROM hadoop_catalog.nyc.taxis FOR SYSTEM_VERSION AS OF 'audit-branch';
SELECT * FROM hadoop_catalog.nyc.taxis FOR SYSTEM_VERSION AS OF 'historical-snapshot';

-- timestamp in seconds
  SELECT * FROM hadoop_catalog.nyc.taxis TIMESTAMP AS OF 499162860;
  SELECT * FROM hadoop_catalog.nyc.taxis FOR SYSTEM_TIME AS OF 499162860;

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

