
# Structured kafka Streaming
```shell
$SPARK_HOME/bin/spark-submit \
--name "structured-stream" \
--master local[*] \
--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.0 \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 
```

# Kerberos
```shell
$SPARK_HOME/bin/spark-submit \
--name "structured-stream" \
--master local[*] \
--conf "spark.driver.extraJavaOptions='-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.executor.extraJavaOptions='-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.kerberos.keytab=/apps/security/keytabs/users/spark.keytab" \
--conf "spark.kerberos.principal=spark@SANDBOX.NET" \
```

# Apache IceBerg 
```shell
$SPARK_HOME/bin/spark-shell \
--packages "org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.4.3,org.apache.iceberg:iceberg-aws:1.4.3" \
--conf "spark.jars.ivy=/apps/hostpath/.ivy2" \
--conf "spark.driver.extraJavaOptions=-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2" \
--conf "spark.executor.extraJavaOptions=-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2" \
 \
```

# Delta Lake
```shell
$SPARK_HOME/bin/spark-shell \
--packages io.delta:delta-core_2.12:1.0.0 \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--conf spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension \
--conf spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog \
```

# Azure Blob Integration
```shell
$SPARK_HOME/bin/spark-shell \
--packages "org.apache.hadoop:hadoop-azure:3.3.1" \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--conf "spark.hadoop.fs.defaultFS=wasb://warehouse@devstoreaccount1" \
--conf "spark.hadoop.fs.azure.account.key.devstoreaccount1.blob.core.windows.net=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==" \
--conf "spark.hadoop.fs.azure.storage.emulator.account.name=devstoreaccount1"
```

val df = spark.read.format("csv").option("header","true").load("/flight_data/airports.csv")


# AWS S3 Integration
```shell
$SPARK_HOME/bin/spark-shell \
--packages "org.apache.spark:spark-hadoop-cloud_2.12:3.5.0" \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--conf spark.hadoop.fs.s3a.endpoint=http://minio.sandbox.net:9010 \
--conf spark.hadoop.fs.s3a.access.key=ffaJ6a2MOj8mZ5lI3P6h \
--conf spark.hadoop.fs.s3a.secret.key=9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.aws.credentials.provider=org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem


$SPARK_HOME/bin/pyspark \
--packages "org.apache.spark:spark-hadoop-cloud_2.12:3.5.0" \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--conf spark.hadoop.fs.s3a.endpoint=http://minio.sandbox.net:9010 \
--conf spark.hadoop.fs.s3a.access.key=ffaJ6a2MOj8mZ5lI3P6h \
--conf spark.hadoop.fs.s3a.secret.key=9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.aws.credentials.provider=org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem

```

```shell

--conf spark.yarn.jars=file:///opt/spark/jars/*.jar \
--packages org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.4.3 \
--properties-file $SPARK_HOME/conf/spark-iceburg.conf \

${SPARK_HOME}/bin/spark-shell \
--master yarn \
--principal zeppelin@SANDBOX.NET \
--keytab /apps/security/keytabs/users/zeppelin.keytab \
--packages org.apache.spark:spark-hadoop-cloud_2.12:3.5.0 \
--conf spark.jars=/opt/spark/jars/iceberg-aws-bundle-1.4.3.jar,/opt/spark/jars/iceberg-spark-runtime-3.5_2.12-1.4.3.jar \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--conf spark.yarn.queue=engineering \
--conf spark.yarn.archive=hdfs://namenode:9000/archives/spark/spark-3.5.0.zip \
--conf spark.yarn.dist.archives=hdfs://namenode:9000/archives/pyspark/pyspark37-20221125.tar.gz#environment \
--conf spark.yarn.dist.files=/opt/spark/conf/log4j.properties \
--conf spark.security.credentials.hive.enabled=false \
--conf spark.security.credentials.hbase.enabled=false \
--conf spark.eventLog.enabled=true \
--conf spark.eventLog.dir=/apps/var/log/spark \
--conf spark.yarn.historyServer.allowTracking=true \
--conf spark.webui.yarn.useProxy=true \
--conf spark.yarn.am.extraJavaOptions='-Divy.home=/apps/hostpath/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf -DAWS_ACCESS_KEY_ID=ffaJ6a2MOj8mZ5lI3P6h -DAWS_SECRET_ACCESS_KEY=9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs' \
--conf spark.driver.extraJavaOptions='-Divy.home=/apps/hostpath/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf -DAWS_ACCESS_KEY_ID=ffaJ6a2MOj8mZ5lI3P6h -DAWS_SECRET_ACCESS_KEY=9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs' \
--conf spark.executor.extraJavaOptions='-Divy.home=/apps/hostpath/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf -DAWS_ACCESS_KEY_ID=ffaJ6a2MOj8mZ5lI3P6h -DAWS_SECRET_ACCESS_KEY=9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs' \
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions \
--conf spark.sql.catalog.demo=org.apache.iceberg.spark.SparkCatalog \
--conf spark.sql.catalog.demo.type=hadoop \
--conf spark.sql.catalog.demo.warehouse=s3a://openlake/warehouse/ \
--conf spark.sql.catalog.demo.s3.endpoint=http://minio.sandbox.net:9010 \
--conf spark.sql.defaultCatalog=demo \
--conf spark.hadoop.fs.s3a.endpoint=http://minio.sandbox.net:9010 \
--conf spark.hadoop.fs.s3a.access.key=ffaJ6a2MOj8mZ5lI3P6h \
--conf spark.hadoop.fs.s3a.secret.key=9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.aws.credentials.provider=org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem \

#--conf spark.sql.catalog.demo.io-impl=org.apache.iceberg.aws.s3.S3FileIO \
```

```scala
# Read CSV file from MinIO
val df = spark.read.option("header", "true").csv("s3a://openlake/warehouse/airlines.csv")
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


