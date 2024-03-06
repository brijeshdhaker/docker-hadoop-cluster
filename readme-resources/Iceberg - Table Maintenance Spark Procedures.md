![iceberg-logo](https://www.apache.org/logos/res/iceberg/iceberg.png)

### [Table Maintenance: The Key To Keeping Your Iceberg Tables Healthy and Performant](https://tabular.io/blog/table-maintenance/)


```scala
spark
```


```scala
spark.sql("DROP TABLE IF EXISTS demo.nyc.taxis_sample")
```


```scala
spark.sql("""
CREATE TABLE demo.nyc.taxis_sample (
  `VendorID` BIGINT,
  `tpep_pickup_datetime` TIMESTAMP,
  `tpep_dropoff_datetime` TIMESTAMP,
  `passenger_count` DOUBLE,
  `trip_distance` DOUBLE,
  `RatecodeID` DOUBLE,
  `store_and_fwd_flag` STRING,
  `PULocationID` BIGINT,
  `DOLocationID` BIGINT,
  `payment_type` BIGINT,
  `fare_amount` DOUBLE,
  `extra` DOUBLE,
  `mta_tax` DOUBLE,
  `tip_amount` DOUBLE,
  `tolls_amount` DOUBLE,
  `improvement_surcharge` DOUBLE,
  `total_amount` DOUBLE,
  `congestion_surcharge` DOUBLE,
  `airport_fee` DOUBLE)
USING iceberg
TBLPROPERTIES(
  'write.target-file-size-bytes'='5242880'
)
""")
```


```scala
val df_202201 = spark.read.parquet("/home/iceberg/data/yellow_tripdata_2022-01.parquet")
val df_202202 = spark.read.parquet("/home/iceberg/data/yellow_tripdata_2022-02.parquet")
val df_202203 = spark.read.parquet("/home/iceberg/data/yellow_tripdata_2022-03.parquet")
val df_q1 = df_202201.union(df_202202).union(df_202203)
df_q1.write.insertInto("nyc.taxis_sample")
```

## Rewriting Data Files


```scala
spark.sql("SELECT file_path, file_size_in_bytes FROM nyc.taxis_sample.files").show(100)
```


```scala
spark.sql("ALTER TABLE nyc.taxis_sample UNSET TBLPROPERTIES ('write.target-file-size-bytes')")
```


```scala
spark.sql("CALL demo.system.rewrite_data_files(table => 'nyc.taxis_sample', options => map('target-file-size-bytes','52428800'))").show()
```


```scala
spark.sql("SELECT file_path, file_size_in_bytes FROM nyc.taxis_sample.files").show(100)
```

## Expiring Snapshots


```scala
spark.sql("SELECT committed_at, snapshot_id, operation FROM nyc.taxis_sample.snapshots").show(truncate=false)
```


```scala
val now = java.util.Calendar.getInstance().getTime()
val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
val now_str = format.format(now)

spark.sql(s"CALL demo.system.expire_snapshots(table => 'nyc.taxis_sample', older_than => TIMESTAMP '$now_str', retain_last => 1)").show()
```


```scala
spark.sql("SELECT committed_at, snapshot_id, operation FROM nyc.taxis_sample.snapshots").show(truncate=false)
```

## Rewriting Manifest Files


```scala
spark.sql("CALL demo.system.rewrite_manifests('nyc.taxis_sample')").show()
```
