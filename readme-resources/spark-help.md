
# Structured kafka Streaming
```shell
$SPARK_HOME/bin/spark-submit \
--name "spark-structured-avro-stream" \
--master local[*] \
--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 
```

# Kerberos
```shell
$SPARK_HOME/bin/spark-submit \
--name "structured-stream" \
--master local[*] \
--conf "spark.driver.extraJavaOptions='-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.executor.extraJavaOptions='-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.kerberos.keytab=/apps/security/keytabs/users/zeppelin.keytab" \
--conf "spark.kerberos.principal=zeppelin@SANDBOX.NET" \
```

# Apache IceBerg 
```shell
$SPARK_HOME/bin/spark-shell \
--packages "org.apache.iceberg:iceberg-spark-runtime-3.1_2.12:0.13.1" \
--conf "spark.driver.extraJavaOptions=-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2" \
--conf "spark.executor.extraJavaOptions=-Divy.cache.dir=/apps/hostpath/.ivy2/cache -Divy.home=/apps/hostpath/.ivy2" \
--conf "spark.jars.ivy=/apps/hostpath/.ivy2" \
```

# Delta Lake
```shell
$SPARK_HOME/bin/spark-shell \
--packages io.delta:delta-core_2.12:1.0.0 \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--conf spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension \
--conf spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog \
```


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


