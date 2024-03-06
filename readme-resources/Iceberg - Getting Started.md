![iceberg-logo](https://www.apache.org/logos/res/iceberg/iceberg.png)

### [Docker, Spark, and Iceberg: The Fastest Way to Try Iceberg!](https://tabular.io/blog/docker-spark-and-iceberg/)


```python
from pyspark.sql import SparkSession
spark = SparkSession.builder.appName("Jupyter").getOrCreate()

spark
```

## Load One Month of NYC Taxi/Limousine Trip Data

For this notebook, we will use the New York City Taxi and Limousine Commision Trip Record Data that's available on the AWS Open Data Registry. This contains data of trips taken by taxis and for-hire vehicles in New York City. We'll save this into an iceberg table called `taxis`.

To be able to rerun the notebook several times, let's drop the table if it exists to start fresh.


```sql
%%sql

CREATE DATABASE IF NOT EXISTS nyc
```


```sql
%%sql

DROP TABLE IF EXISTS nyc.taxis
```


```python
df = spark.read.parquet("/home/iceberg/data/yellow_tripdata_2021-04.parquet")
df.write.saveAsTable("nyc.taxis")
```


```sql
%%sql

DESCRIBE EXTENDED nyc.taxis
```


```sql
%%sql

SELECT COUNT(*) as cnt FROM nyc.taxis
```

## Schema Evolution

Adding, dropping, renaming, or altering columns is easy and safe in Iceberg. In this example, we'll rename `fare_amount` to `fare` and `trip_distance` to `distance`. We'll also add a float column `fare_per_distance_unit` immediately after `distance`.


```sql
%%sql

ALTER TABLE nyc.taxis RENAME COLUMN fare_amount TO fare
```


```sql
%%sql

ALTER TABLE nyc.taxis RENAME COLUMN trip_distance TO distance
```


```sql
%%sql

ALTER TABLE nyc.taxis ALTER COLUMN distance COMMENT 'The elapsed trip distance in miles reported by the taximeter.'
```


```sql
%%sql

ALTER TABLE nyc.taxis ALTER COLUMN distance TYPE double;
```


```sql
%%sql

ALTER TABLE nyc.taxis ALTER COLUMN distance AFTER fare;
```


```sql
%%sql

ALTER TABLE nyc.taxis
ADD COLUMN fare_per_distance_unit float AFTER distance
```

Let's update the new `fare_per_distance_unit` to equal `fare` divided by `distance`.


```sql
%%sql

UPDATE nyc.taxis
SET fare_per_distance_unit = fare/distance
```


```sql
%%sql

SELECT
VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance_unit
FROM nyc.taxis
```

## Expressive SQL for Row Level Changes
With Iceberg tables, `DELETE` queries can be used to perform row-level deletes. This is as simple as providing the table name and a `WHERE` predicate. If the filter matches an entire partition of the table, Iceberg will intelligently perform a metadata-only operation where it simply deletes the metadata for that partition.

Let's perform a row-level delete for all rows that have a `fare_per_distance_unit` greater than 4 or a `distance` greater than 2. This should leave us with relatively short trips that have a relatively high fare per distance traveled.


```sql
%%sql

DELETE FROM nyc.taxis
WHERE fare_per_distance_unit > 4.0 OR distance > 2.0
```

There are some fares that have a `null` for `fare_per_distance_unit` due to the distance being `0`. Let's remove those as well.


```sql
%%sql

DELETE FROM nyc.taxis
WHERE fare_per_distance_unit is null
```


```sql
%%sql

SELECT
VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance_unit
FROM nyc.taxis
```


```sql
%%sql

SELECT COUNT(*) as cnt
FROM nyc.taxis
```

## Partitioning

A table’s partitioning can be updated in place and applied only to newly written data. Query plans are then split, using the old partition scheme for data written before the partition scheme was changed, and using the new partition scheme for data written after. People querying the table don’t even have to be aware of this split. Simple predicates in WHERE clauses are automatically converted to partition filters that prune out files with no matches. This is what’s referred to in Iceberg as *Hidden Partitioning*.


```sql
%%sql

ALTER TABLE nyc.taxis
ADD PARTITION FIELD VendorID
```

## Metadata Tables

Iceberg tables contain very rich metadata that can be easily queried. For example, you can retrieve the manifest list for any snapshot, simply by querying the table's `snapshots` table.


```sql
%%sql

SELECT snapshot_id, manifest_list
FROM nyc.taxis.snapshots
```

The `files` table contains loads of information on data files, including column level statistics such as null counts, lower bounds, and upper bounds.


```sql
%%sql

SELECT file_path, file_format, record_count, null_value_counts, lower_bounds, upper_bounds
FROM nyc.taxis.files
```

## Time Travel

The history table lists all snapshots and which parent snapshot they derive from. The `is_current_ancestor` flag let's you know if a snapshot is part of the linear history of the current snapshot of the table.


```sql
%%sql

SELECT *
FROM nyc.taxis.history
```

You can time-travel by altering the `current-snapshot-id` property of the table to reference any snapshot in the table's history. Let's revert the table to it's original state by traveling to the very first snapshot ID.


```sql
%%sql --var df

SELECT *
FROM nyc.taxis.history
```


```python
original_snapshot = df.head().snapshot_id
spark.sql(f"CALL system.rollback_to_snapshot('nyc.taxis', {original_snapshot})")
original_snapshot
```


```sql
%%sql

SELECT
VendorID
,tpep_pickup_datetime
,tpep_dropoff_datetime
,fare
,distance
,fare_per_distance_unit
FROM nyc.taxis
```

Another look at the history table shows that the original state of the table has been added as a new entry
with the original snapshot ID.


```sql
%%sql

SELECT *
FROM nyc.taxis.history
```


```sql
%%sql

SELECT COUNT(*) as cnt
FROM nyc.taxis
```
