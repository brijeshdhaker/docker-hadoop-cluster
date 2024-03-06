![iceberg-logo](https://www.apache.org/logos/res/iceberg/iceberg.png)

### [Docker, Spark, and Iceberg: The Fastest Way to Try Iceberg!](https://tabular.io/blog/docker-spark-and-iceberg/)


```python
from pyiceberg import __version__

__version__
```

## Load NYC Taxi/Limousine Trip Data

For this notebook, we will use the New York City Taxi and Limousine Commision Trip Record Data that's available on the AWS Open Data Registry. This contains data of trips taken by taxis and for-hire vehicles in New York City. We'll save this into an iceberg table called `taxis`.

To be able to rerun the notebook several times, let's drop the table if it exists to start fresh.


```sql
%%sql

CREATE DATABASE IF NOT EXISTS nyc;
```


```sql
%%sql
DROP TABLE IF EXISTS nyc.taxis;
```


```sql
%%sql
CREATE TABLE IF NOT EXISTS nyc.taxis (
    VendorID              bigint,
    tpep_pickup_datetime  timestamp,
    tpep_dropoff_datetime timestamp,
    passenger_count       double,
    trip_distance         double,
    RatecodeID            double,
    store_and_fwd_flag    string,
    PULocationID          bigint,
    DOLocationID          bigint,
    payment_type          bigint,
    fare_amount           double,
    extra                 double,
    mta_tax               double,
    tip_amount            double,
    tolls_amount          double,
    improvement_surcharge double,
    total_amount          double,
    congestion_surcharge  double,
    airport_fee           double
)
USING iceberg
PARTITIONED BY (days(tpep_pickup_datetime))
```


```sql
%%sql

TRUNCATE TABLE nyc.taxis
```


```python
from pyspark.sql import SparkSession
spark = SparkSession.builder.appName("Jupyter").getOrCreate()

for filename in [
    "yellow_tripdata_2022-04.parquet",
    "yellow_tripdata_2022-03.parquet",
    "yellow_tripdata_2022-02.parquet",
    "yellow_tripdata_2022-01.parquet",
    "yellow_tripdata_2021-12.parquet",
]:
    df = spark.read.parquet(f"/home/iceberg/data/{filename}")
    df.write.mode("append").saveAsTable("nyc.taxis")
```

## Load data into a PyArrow Dataframe

We'll fetch the table using the REST catalog that comes with the setup.


```python
from pyiceberg.catalog import load_catalog
from pyiceberg.expressions import GreaterThanOrEqual

catalog = load_catalog('default')
```


```python
tbl = catalog.load_table('nyc.taxis')

sc = tbl.scan(row_filter=GreaterThanOrEqual("tpep_pickup_datetime", "2022-01-01T00:00:00.000000+00:00"))
```


```python
df = sc.to_arrow().to_pandas()
```


```python
len(df)
```


```python
df.info()
```


```python
df
```


```python
df.hist(column='fare_amount')
```


```python
import numpy as np
from scipy import stats

stats.zscore(df['fare_amount'])

# Remove everything larger than 3 stddev
df = df[(np.abs(stats.zscore(df['fare_amount'])) < 3)]
# Remove everything below zero
df = df[df['fare_amount'] > 0]
```


```python
df.hist(column='fare_amount')
```

# DuckDB

Use DuckDB to Query the PyArrow Dataframe directly.


```python
%load_ext sql
%config SqlMagic.autopandas = True
%config SqlMagic.feedback = False
%config SqlMagic.displaycon = False
%sql duckdb:///:memory:
```


```python
%sql SELECT * FROM df LIMIT 20
```


```sql
%%sql --save tip-amount --no-execute

SELECT tip_amount
FROM df
```


```python
%sqlplot histogram --table df --column tip_amount --bins 22 --with tip-amount

```


```sql
%%sql --save tip-amount-filtered --no-execute

WITH tip_amount_stddev AS (
    SELECT STDDEV_POP(tip_amount) AS tip_amount_stddev
    FROM df
)

SELECT tip_amount
FROM df, tip_amount_stddev
WHERE tip_amount > 0
  AND tip_amount < tip_amount_stddev * 3
```


```python
%sqlplot histogram --table tip-amount-filtered --column tip_amount --bins 50 --with tip-amount-filtered

```

# Iceberg ❤️ PyArrow and DuckDB

This notebook shows how you can load data into a PyArrow dataframe and query it using DuckDB easily. Iceberg allows you to take a slice out of the data that you need for your analysis, while reducing the time that you have to wait for the data and without polluting the memory with data that you're not going to use.


```python

```
