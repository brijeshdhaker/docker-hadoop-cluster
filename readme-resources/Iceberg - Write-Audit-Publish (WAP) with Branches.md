![iceberg-logo](https://www.apache.org/logos/res/iceberg/iceberg.png)

## Write-Audit-Publish with Branches in Apache Iceberg

This notebook runs using the Docker Compose at https://github.com/tabular-io/docker-spark-iceberg. 
It's based on the [Iceberg - Integrated Audits Demo.ipynb](https://github.com/tabular-io/docker-spark-iceberg/blob/main/spark/notebooks/Iceberg%20-%20Integrated%20Audits%20Demo.ipynb) notebook. 


```python
from pyspark.sql import SparkSession
spark = SparkSession.builder.appName("Jupyter").getOrCreate()

spark
```

To be able to rerun the notebook several times, let's drop the `permits` table if it exists to start fresh.


```sql
%%sql

CREATE DATABASE IF NOT EXISTS nyc
```


```sql
%%sql

DROP TABLE IF EXISTS nyc.permits
```

# Load NYC Film Permits Data

For this demo, we will use the [New York City Film Permits dataset](https://data.cityofnewyork.us/City-Government/Film-Permits/tg4x-b46p) available as part of the NYC Open Data initiative. We're using a locally saved copy of a 1000 record sample, but feel free to download the entire dataset to use in this notebook!

We'll save the sample dataset into an iceberg table called `permits`.


```python
df = spark.read.option("inferSchema","true").option("multiline","true").json("/home/iceberg/data/nyc_film_permits.json")
df.write.saveAsTable("nyc.permits")
```

Taking a quick peek at the data, you can see that there are a number of permits for different boroughs in New York.


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits
GROUP BY borough
```

# The Setup

Tables by default are not configured to allow integrated audits, therefore the first step is enabling this by setting the `write.wap.enabled` table metadata property to `true`


```sql
%%sql

ALTER TABLE nyc.permits
SET TBLPROPERTIES (
    'write.wap.enabled'='true'
)
```

We create a branch for the work we want to do. This is a copy-on-write branch, so "free" until we start making changes (and "cheap" thereafter) since only data that's changed needs to be written. 


```sql
%%sql

ALTER TABLE nyc.permits
CREATE BRANCH etl_job_42
```

# Write

Before writing to the table we set `spark.wap.branch` so that writes (and reads) are against the specified branch of the table. 


```python
spark.conf.set('spark.wap.branch', 'etl_job_42')
```

Now make the change to the table


```sql
%%sql

DELETE FROM nyc.permits
WHERE borough='Manhattan'
```

## Inspecting the staged/unpublished data

### Staged/unpublished data

The changes are reflected in the table:


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits
GROUP BY borough
```

Note that because `spark.wap.branch` is set the above query is effectively the same as this one with `VERSION AS OF` for the branch


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits VERSION AS OF 'etl_job_42'
GROUP BY borough
```

Another syntax (albiet less clear IMHO) for `VERSION AS OF` is a `branch_<branch_name>` suffix to the table: 


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits.branch_etl_job_42
GROUP BY borough
```

### Published data

We can also inspect the unmodified `main` version of the table with `VERSION AS OF`: 


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits VERSION AS OF 'main'
GROUP BY borough
```

The same `branch_` suffix words here too: 


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits.branch_main
GROUP BY borough
```

Any other user of the table will see the full set of data. We can reassure ourselves of this by unsetting `spark.wap.branch` for the session and querying the table without any `VERSION AS OF` modifier


```python
spark.conf.unset('spark.wap.branch')
```


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits
GROUP BY borough
```

# Audit

How you audit the data is up to you. The nice thing about the data being staged is that you can do it within the same ETL job, or have another tool do it. 

Here's a very simple example of doing in Python. We're going to programatically check that only the four expected boroughs remain in the data. 

First, we define those that are expected: 


```python
expected_boroughs = {"Queens", "Brooklyn", "Bronx", "Staten Island"}
```

Then we get a set of the actual boroughs in the staged data


```python
distinct_boroughs = spark.read \
    .option("branch", "etl_job_42") \
    .format("iceberg") \
    .load("nyc.permits") \
    .select("borough") \
    .distinct() \
    .toLocalIterator()
boroughs = {row[0] for row in distinct_boroughs}
```

Now we do two checks: 

1. Compare the length of the expected vs actual set
2. Check that the two sets when unioned are still the same length. This is necessary, since the first test isn't sufficient alone


```python
if (   (len(boroughs)          != len(expected_boroughs)) \
      or (len(boroughs)          != len(set.union(boroughs, expected_boroughs))) \
      or (len(expected_boroughs) != len(set.union(boroughs, expected_boroughs)))):
    raise ValueError(f"Audit failed, borough set does not match expected boroughs: {boroughs} != {expected_boroughs}")
else:
    print(f"Audit has passed 🙌🏻")
```

# Publish

Iceberg supports fast-forward merging of branches back to `main`, using the [`manageSnapshots().fastForwardBranch`](https://iceberg.apache.org/javadoc/latest/org/apache/iceberg/ManageSnapshots.html#fastForwardBranch-java.lang.String-java.lang.String-) API.

This isn't yet exposed in Spark, so the existing [`cherrypick`](https://iceberg.apache.org/javadoc/latest/org/apache/iceberg/ManageSnapshots.html#cherrypick-long-) can be used as a slightly less elegant option.

ℹ️ Note that `cherrypick` only works for one commit. 

First, we need the snapshot ID of our branch, which we can get from the `.refs` table:


```sql
%%sql

SELECT * FROM nyc.permits.refs 
```


```python
query = f"""
SELECT snapshot_id
FROM nyc.permits.refs
WHERE name = 'etl_job_42'
"""

wap_snapshot_id = spark.sql(query).head().snapshot_id
```

Now we do the publish, using `cherrypick_snapshot` and the snapshot id:


```python
publish_query = f"CALL system.cherrypick_snapshot('nyc.permits', {wap_snapshot_id})"

%sql $publish_query
```

Finally, we look at the table and revel in the glory that is our published changes 🎉


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits.branch_etl_job_42
GROUP BY borough
```

We can also inspect the unmodified `main` version of the table with `VERSION AS OF`: 


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits VERSION AS OF 'main'
GROUP BY borough
```

---

# What if You Don't Want to Publish Changes?

If you don't want to merge the branch you can simply `DROP` it. 

## Create a new branch


```sql
%%sql

ALTER TABLE nyc.permits
CREATE BRANCH new_etl_job
```

## Set `spark.wap.branch`


```python
spark.conf.set('spark.wap.branch', 'new_etl_job')
```

## Write


```sql
%%sql

DELETE FROM nyc.permits WHERE borough LIKE '%'
```

## Audit


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits 
GROUP BY borough
```

### Whoops 🤭 
We deleted all the data

### Reassure ourselves that the data is still there in `main`


```sql
%%sql

SELECT borough, count(*) permit_cnt
FROM nyc.permits VERSION AS OF 'main'
GROUP BY borough
```

## Abandon changes


```sql
%%sql

ALTER TABLE nyc.permits
DROP BRANCH new_etl_job
```

---

# Where Next?

For more information about write-audit-publish see [this talk from Michelle Winters](https://www.youtube.com/watch?v=fXHdeBnpXrg&t=1001s) and [this talk from Sam Redai](https://www.dremio.com/wp-content/uploads/2022/05/Sam-Redai-The-Write-Audit-Publish-Pattern-via-Apache-Iceberg.pdf).
