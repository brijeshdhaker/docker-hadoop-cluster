
## Delta Lake Architecture

![deltalake_architecture.png](images%2Fdeltalake_architecture.png)
## Advantages of Delta Lake

It's important for us to understand the benefits of Delta Lake before we deep dive into the details of Delta Lake. For example, why do we need Delta Lake?

There are many features available in Delta Lake but two of them are very important to consider(at least based on my experience):

ACID support. Traditionally, when we write data into a file system using Spark, the whole write operation is not atomic. For example, if the job fails in the middle, the data in the target folder will be corrupted. The walkaround is usually to backup the target before overwriting. With Delta Lake format, we don't need to worry about that any more.
Merge statement. In relational databases like SQL Server, Oracle, Teradata, PostgreSQL, etc. we can use MERGE statement to merge data from staging table to targeted table (usually SCD type 2 table). In my previous post, I did publish an article about how to merge into SCD type 2 table using Spark, but the code is not concise and the performance might not be that great.
Streaming data ingestion. Delta table can be used as target for streaming ingestion We can also use delta table as target for Spark structured streaming. This provides the opportunity to unify both batch and streaming within one pipeline.
Time travel. We can read older versions of data via time travel feature since the data is stored as versions.

# Install delta-spark Python package
```shell
conda install pyspark==3.5.3 

pip install -i https://test.pypi.org/simple/ delta-spark==3.3.2
pip install importlib-metadata

export HADOOP_CONF_DIR=${HOME}/IdeaProjects/spark-python-examples/docker-sandbox/conf/hadoop/client
spark-submit --packages "io.delta:delta-core_2.12:3.3.2" spark-delta-lake.py
hadoop fs -ls -R /deltalake/test_table

pyspark --packages io.delta:delta-core_2.12:2.4.0 --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"

```

* Delta Lake doesn't rely on Hive or Hive metastore as the metadata information is stored in file system directly via log files.

#
#
#
bin/spark-sql --packages io.delta:delta-spark_2.12:3.2.0 --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"

#
# For Spark 3.5.3
#
pyspark --packages io.delta:delta-spark_2.12:3.3.2 --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"

#
# For Spark 3.4.1
#
bin/spark-shell --packages io.delta:delta-core_2.12:2.4.0 --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"
