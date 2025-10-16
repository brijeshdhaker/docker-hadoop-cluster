#
#
#

conda activate env_python3_11_13

#
#
#

```bash
$ jupyter notebook --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-notebooks-module/notebooks
```

#
#
#
```bash
$ jupyter lab --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=~/IdeaProjects/docker-hadoop-cluster/bd-notebooks-module/notebooks

# Check the newly built image
$ docker run -it --rm quay.io/jupyter/all-spark-notebook:spark-3.5.2 pyspark --version
```

Entered start.sh with args: pyspark --version
Running hooks in: /usr/local/bin/start-notebook.d as uid: 1000 gid: 100
Done running hooks in: /usr/local/bin/start-notebook.d
Running hooks in: /usr/local/bin/before-notebook.d as uid: 1000 gid: 100
Sourcing shell script: /usr/local/bin/before-notebook.d/10activate-conda-env.sh
Sourcing shell script: /usr/local/bin/before-notebook.d/10spark-config.sh
Done running hooks in: /usr/local/bin/before-notebook.d
Executing the command: pyspark --version
Welcome to
____              __
/ __/__  ___ _____/ /__
_\ \/ _ \/ _ `/ __/  '_/
/___/ .__/\_,_/_/ /_/\_\   version 3.5.2
/_/

Using Scala version 2.12.18, OpenJDK 64-Bit Server VM, 17.0.12
Branch HEAD
Compiled by user ubuntu on 2024-08-06T11:36:15Z
Revision bb7846dd487f259994fdc69e18e03382e3f64f42
Url https://github.com/apache/spark
Type --help for more information.



$ docker run -it --rm quay.io/jupyter/all-spark-notebook:spark-3.5.2 spark-shell \
--conf spark.jars.ivy=/apps/.ivy2 \
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


// Replace Key with your AWS account key (You can find this on IAM
sc.hadoopConfiguration.set("fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO")
sc.hadoopConfiguration.set("fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG")
sc.hadoopConfiguration.set("fs.s3a.endpoint", "http://minio.sandbox.net:9010")
sc.hadoopConfiguration.set("fs.s3a.path.style.access", "true")
sc.hadoopConfiguration.set("fs.s3a.attempts.maximum", "1")
sc.hadoopConfiguration.set("fs.s3a.connection.establish.timeout", "5000")
sc.hadoopConfiguration.set("fs.s3a.connection.timeout", "10000")

--packages org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.6.1





