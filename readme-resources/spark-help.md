
# Structured kafka Streaming
```shell
$SPARK_HOME/bin/spark-submit \
--name "structured-stream" \
--master local[*] \
--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 
```

# Kerberos
```shell
$SPARK_HOME/bin/spark-shell \
--name "structured-stream" \
--master local[*] \
--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 \
--conf "spark.driver.extraJavaOptions='-Divy.cache.dir=/apps/.ivy2/cache -Divy.home=/apps/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.executor.extraJavaOptions='-Divy.cache.dir=/apps/.ivy2/cache -Divy.home=/apps/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.kerberos.keytab=/apps/security/keytabs/users/spark.keytab" \
--conf "spark.kerberos.principal=spark@SANDBOX.NET"

```

# Apache IceBerg 
```shell
$SPARK_HOME/bin/spark-shell \
--packages "org.apache.iceberg:iceberg-spark-runtime-3.4_2.12:1.4.1,org.apache.iceberg:iceberg-aws:1.4.3,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1" \
--conf "spark.jars.ivy=/apps/.ivy2" \
--conf "spark.driver.extraJavaOptions=-Divy.cache.dir=/apps/.ivy2/cache -Divy.home=/apps/.ivy2" \
--conf "spark.executor.extraJavaOptions=-Divy.cache.dir=/apps/.ivy2/cache -Divy.home=/apps/.ivy2"  

```

# Delta Lake With Spark 3.4.1

```shell
$SPARK_HOME/bin/spark-shell \
--packages io.delta:delta-core_2.12:2.4.0,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 \
--conf spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension \
--conf spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog

```

# Azure Blob Integration
```shell
$SPARK_HOME/bin/spark-shell \
--packages "org.apache.spark:spark-hadoop-cloud_2.12:3.4.1" \
--conf spark.jars.ivy=/apps/.ivy2 \
--conf "spark.hadoop.fs.defaultFS=wasb://warehouse@devstoreaccount1" \
--conf "spark.hadoop.fs.azure.account.key.devstoreaccount1.blob.core.windows.net=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==" \
--conf "spark.hadoop.fs.azure.storage.emulator.account.name=devstoreaccount1"
```

val df = spark.read.format("csv").option("header","true").load("/flight_data/airports.csv")


# AWS S3 Integration
```shell
$SPARK_HOME/bin/spark-shell \
--packages "org.apache.spark:spark-hadoop-cloud_2.12:3.4.1" \
--conf spark.jars.ivy=/apps/.ivy2 \
--conf spark.hadoop.fs.s3a.endpoint=http://minio.sandbox.net:9010 \
--conf spark.hadoop.fs.s3a.access.key=pgm2H2bR7a5kMc5XCYdO \
--conf spark.hadoop.fs.s3a.secret.key=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.aws.credentials.provider=org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem


$SPARK_HOME/bin/pyspark \
--properties-file $SPARK_HOME/conf/spark-yarn.conf 

```


```shell

--conf spark.yarn.jars=file:///opt/spark/jars/*.jar \
--packages org.apache.iceberg:iceberg-spark-runtime-3.4_2.12:1.4.1,org.apache.iceberg:iceberg-aws:1.4.3,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1 \
--properties-file $SPARK_HOME/conf/spark-iceburg.conf \


${SPARK_HOME}/bin/spark-shell \
--master local[*] \
--properties-file $SPARK_HOME/conf/spark-yarn.conf 

${SPARK_HOME}/bin/spark-shell \
--master yarn \
--properties-file $SPARK_HOME/conf/spark-yarn.conf

--principal zeppelin@SANDBOX.NET \
--keytab /apps/security/keytabs/users/zeppelin.keytab \
--properties-file $SPARK_HOME/conf/spark-iceburg.conf \
--conf spark.jars.packages=org.apache.iceberg:iceberg-spark-runtime-3.4_2.12:1.4.1,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1



${SPARK_HOME}/bin/spark-shell \
--master yarn \
--conf spark.jars.ivy=/apps/.ivy2 \
--conf spark.kerberos.keytab=/apps/security/keytabs/users/zeppelin.keytab \
--conf spark.kerberos.principal=zeppelin@SANDBOX.NET \
--conf spark.security.credentials.hive.enabled=false \
--conf spark.security.credentials.hbase.enabled=false \
--conf spark.yarn.queue=engineering \
--conf spark.yarn.archive=hdfs://namenode:9000/archives/spark/spark-3.5.0.zip \
--conf spark.yarn.dist.archives=hdfs://namenode:9000/archives/pyspark/pyspark37-20221125.tar.gz#environment \
--conf spark.yarn.dist.files=/opt/spark/conf/log4j.properties \
--conf spark.yarn.jars=${SPARK_HOME}/jars/iceberg-spark-runtime-3.4_2.12:1.4.1 \
--conf spark.yarn.historyServer.allowTracking=true \
--conf spark.yarn.am.extraJavaOptions='-Divy.home=/apps/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf -DAWS_ACCESS_KEY_ID=pgm2H2bR7a5kMc5XCYdO -DAWS_SECRET_ACCESS_KEY=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG' \
--conf spark.eventLog.enabled=true \
--conf spark.eventLog.dir=/apps/var/logs/spark \
--conf spark.webui.yarn.useProxy=true \
--conf spark.driver.extraJavaOptions='-Divy.home=/apps/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf -DAWS_ACCESS_KEY_ID=pgm2H2bR7a5kMc5XCYdO -DAWS_SECRET_ACCESS_KEY=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG' \
--conf spark.executor.extraJavaOptions='-Divy.home=/apps/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf -DAWS_ACCESS_KEY_ID=pgm2H2bR7a5kMc5XCYdO -DAWS_SECRET_ACCESS_KEY=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG' \
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions \
--conf spark.sql.catalog.spark_catalog=org.apache.iceberg.spark.SparkSessionCatalog \
--conf spark.sql.catalog.spark_catalog.type=hive \
--conf spark.sql.catalog.iceberg=org.apache.iceberg.spark.SparkCatalog \
--conf spark.sql.catalog.iceberg.type=hadoop \
--conf spark.sql.catalog.iceberg.warehouse=s3a://openlake/warehouse/ \
--conf spark.sql.catalog.iceberg.s3.endpoint=http://minio.sandbox.net:9010 \
--conf spark.sql.defaultCatalog=iceberg \
--conf spark.hadoop.fs.s3a.endpoint=http://minio.sandbox.net:9010 \
--conf spark.hadoop.fs.s3a.access.key=pgm2H2bR7a5kMc5XCYdO \
--conf spark.hadoop.fs.s3a.secret.key=zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.aws.credentials.provider=org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem \

```

```scala
# Read CSV file from MinIO
val df = spark.read.option("header", "true").csv("s3a://openlake/flight-data/airlines.csv")
df.show()
```

#
# Spark SQL CLI
#

set hive.cli.print.header=false;
set hive.cli.print.footer=false;
set hive.cli.print.current.db=true;

spark-sql \
--hiveconf hive.cli.print.header=true \
--hiveconf hive.cli.print.current.db=true \
-e "show tables"

spark-sql --hiveconf hive.cli.print.header=true --hiveconf hive.cli.print.current.db=true -e "dfs -ls /user" | awk "/zeppelin {print $1}"

spark-sql --hiveconf hive.cli.print.header=true --hiveconf hive.cli.print.current.db=true -e "select * from transaction_details" | awk '/VISA/ {print(NR, "\t", $2, "\t", $5)}'

#
# Java 17 & Spark
#
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED
--add-opens=java.base/java.io=ALL-UNNAMED
--add-opens=java.base/java.net=ALL-UNNAMED
--add-opens=java.base/java.nio=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED
--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED
--add-opens=java.base/sun.nio.cs=ALL-UNNAMED
--add-opens=java.base/sun.security.action=ALL-UNNAMED
--add-opens=java.base/sun.util.calendar=ALL-UNNAMED
