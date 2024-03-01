
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
--packages "org.apache.spark:spark-hadoop-cloud_2.12:3.1.2" \
--conf spark.jars.ivy=/apps/hostpath/.ivy2 \
--conf spark.hadoop.fs.s3a.endpoint=http://localhost:9000 \
--conf spark.hadoop.fs.s3a.access.key=abc \
--conf spark.hadoop.fs.s3a.secret.key=xyzxyzxyz \
--conf spark.hadoop.fs.s3a.path.style.access=True) \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem

```