### Activate Python VENV
```bash

conda activate env_python3_11_13
conda install jupyter -c defaults

```
### jupyter lab
```bash

$ jupyter lab --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=~/IdeaProjects/docker-hadoop-cluster/bd-notebooks-module/notebooks
```

### jupyter notebook
#
```bash

$ jupyter notebook --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-notebooks-module/notebooks
```

### Check the newly built image
```bash

$ docker run -it --rm jupyter/all-spark-notebook:latest pyspark --version
```
```bash

$ docker run -it --rm jupyter/all-spark-notebook:latest spark-shell \
--conf spark.jars.ivy=/home/brijeshdhaker/.ivy2 \
--conf spark.jars.packages=org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.6.1,org.apache.hadoop:hadoop-aws:3.0.0 \
--conf spark.hadoop.fs.s3a.endpoint=http://minio.sandbox.net:9010 \
--conf spark.hadoop.fs.s3a.access.key=pgm2H2bR7a5kMc5XCYdO \
--conf spark.hadoop.fs.s3a.secret.key=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.aws.credentials.provider=org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem \
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions \
--conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkSessionCatalog \
--conf spark.sql.catalog.spark_catalog.type=hive \
--conf spark.sql.catalog.iceberg=org.apache.iceberg.spark.SparkCatalog \
--conf spark.sql.catalog.iceberg.type=hadoop \
--conf spark.sql.catalog.iceberg.warehouse="s3a://warehouse/tablespace/external/spark" \
--conf spark.sql.defaultCatalog=iceberg \
--conf spark.hadoop.hive.cli.print.header=true
```

// Replace Key with your AWS account key (You can find this on IAM
sc.hadoopConfiguration.set("fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO")
sc.hadoopConfiguration.set("fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG")
sc.hadoopConfiguration.set("fs.s3a.endpoint", "http://minio.sandbox.net:9010")
sc.hadoopConfiguration.set("fs.s3a.path.style.access", "true")
sc.hadoopConfiguration.set("fs.s3a.attempts.maximum", "1")
sc.hadoopConfiguration.set("fs.s3a.connection.establish.timeout", "5000")
sc.hadoopConfiguration.set("fs.s3a.connection.timeout", "10000")

--packages org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.6.1





