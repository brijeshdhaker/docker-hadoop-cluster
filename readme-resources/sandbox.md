
```shell

${SPARK_HOME}/bin/spark-shell \
--principal zeppelin@SANDBOX.NET \
--keytab /apps/security/keytabs/users/zeppelin.keytab

${SPARK_HOME}/bin/spark-shell \
--master yarn \
--principal zeppelin@SANDBOX.NET \
--keytab /apps/security/keytabs/users/zeppelin.keytab \
--conf spark.yarn.jars=/opt/spark/jars/*.jar \
--conf spark.yarn.archive=hdfs://namenode:9000/archives/spark/spark-3.5.0.zip \
--conf spark.yarn.dist.archives=hdfs://namenode:9000/archives/pyspark/pyspark37-20221125.tar.gz#environment \
--conf spark.shuffle.service.enabled=true  \
--conf spark.dynamicAllocation.enabled=true \
--conf spark.dynamicAllocation.minExecutors=0 \
--conf spark.dynamicAllocation.initialExecutors=1 \
--conf spark.dynamicAllocation.maxExecutors=5 \
--conf spark.security.credentials.hbase.enabled=false \
--conf spark.security.credentials.hive.enabled=false \

\
spark.conf.get("spark.sql.catalogImplementation")


${SPARK_HOME}/bin/spark-submit \
--class org.apache.zeppelin.interpreter.remote.RemoteInterpreterServer \
--driver-java-options "-Dfile.encoding=UTF-8 -Dlog4j.configuration=log4j_yarn_cluster.properties -Djava.security.krb5.conf=/etc/krb5.conf" \
--conf spark.yarn.historyServer.allowTracking=true \
--conf spark.yarn.dist.archives=hdfs://namenode:9000/archives/pyspark/pyspark37-20221125.tar.gz#environment \
--conf spark.yarn.isPython=true \
--conf spark.jars=/opt/spark/examples/jars/scopt_2.12-3.7.1.jar \
--conf spark.submit.deployMode=cluster \
--conf spark.eventLog.enabled=true \
--conf spark.yarn.maxAppAttempts=1 \
--conf spark.yarn.queue=engineering \
--conf spark.yarn.archive=hdfs://namenode:9000/archives/spark/spark-3.5.0.zip \
--conf spark.yarn.submit.waitAppCompletion=false \
--conf spark.jars.packages=io.delta:delta-core_2.12:1.0.0,org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.0 \
--conf spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension \
--conf spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog \
--conf spark.master=yarn \
--conf spark.driver.memory=640m \
--conf spark.driver.cores=1 \
--conf spark.executor.memory=640m \
--conf spark.executor.cores=2 \
--conf spark.webui.yarn.useProxy=true \
--conf spark.executor.instances=2 \
--conf spark.app.name=zeppelin-app \
--conf spark.kerberos.keytab=/apps/security/keytabs/users/zeppelin.keytab \
--conf spark.kerberos.principal=zeppelin@SANDBOX.NET \
--conf spark.eventLog.dir=hdfs://namenode:9000/apps/var/log/spark \
/opt/spark/examples/jars/spark-examples_2.12-3.5.0.jar 10

${SPARK_HOME}/bin/spark-submit \
    --keytab /apps/security/keytabs/users/zeppelin.keytab \
    --principal zeppelin@SANDBOX.NET \
    --master yarn \
    --deploy-mode cluster \
    --driver-memory 640m \
    --executor-memory 640m \
    --executor-cores 1 \
    --queue engineering \
    --name spark-app \
    --class org.apache.spark.examples.SparkPi \
    /opt/spark/examples/jars/spark-examples_2.12-3.5.0.jar 10
    
${SPARK_HOME}/bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--conf "spark.submit.deployMode=cluster" \
--conf "spark.app.name=spark-pi-example" \
--conf "spark.master=yarn" \
--conf "spark.yarn.queue=engineering" \
--conf "spark.driver.memory=640m" \
--conf "spark.driver.cores=1" \
--conf "spark.executor.memory=640m" \
--conf "spark.executor.cores=2" \
--conf "spark.yarn.archive=hdfs://namenode:9000/archives/spark/spark-3.5.0.zip" \
--conf "spark.jars.packages=io.delta:delta-core_2.12:1.0.0,org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.0" \
--conf "spark.security.credentials.hive.enabled=false" \
--conf "spark.security.credentials.hbase.enabled=false" \
--conf "spark.kerberos.keytab=/apps/security/keytabs/users/zeppelin.keytab" \
--conf "spark.kerberos.principal=zeppelin@SANDBOX.NET" \
--conf "spark.driver.extraJavaOptions='-Divy.cache.dir=/tmp -Divy.home=/tmp -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.executor.extraJavaOptions='-Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.eventLog.enabled=true" \
--conf "spark.eventLog.dir=hdfs://namenode:9000/apps/var/log/spark" \
--conf "spark.yarn.historyServer.allowTracking=true" \
--conf "spark.webui.yarn.useProxy=true" \
/opt/spark/examples/jars/spark-examples_2.12-3.5.0.jar 10

```

#
# HDFS
#
```shell
Options:

-d: f the path is a directory, return 0.
-e: if the path exists, return 0.
-f: if the path is a file, return 0.
-s: if the path is not empty, return 0.
-z: if the file is zero length, return 0.

CHECK_PATH=/user
if $HADOOP_HOME/bin/hdfs dfs -test -e $CHECK_PATH; then
    echo "[$CHECK_PATH] exists on HDFS"
    hdfs dfs -ls $CHECK_PATH
fi

```
#
# YARN
#
```shell

yarn app -list
# ALL, NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED
yarn app -list -appStates RUNNING
yarn app -status application_1707705309527_0001
yarn app -kill application_1707705309527_0001

yarn logs -applicationId application_1707218631234_0019 > application_1707218631234_0019.log 2>&1

yarn node  -list
yarn node  -list -all
yarn node  -list -states running
yarn node -status nodemanager.sandbox.net:41755

```
#
# Hive
#
```shell

$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET"
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" -e "show tables"
$HIVE_HOME/bin/beeline -u "jdbc:hive2://hiveserver.sandbox.net:10000/default;principal=hive/_HOST@SANDBOX.NET" -f "/apps/sandbox/hive/hql/table.hql"

```

#
# Zookeeper
#
```shell

zookeeper-shell zookeeper:2181 ls /

zookeeper-shell zookeeper.sandbox.net:2182 -zk-tls-config-file /apps/sandbox/kafka/cnf/zookeeper-client.config ls /brokers/ids

```