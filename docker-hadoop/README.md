[![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/brijeshdhaker-europe/Lobby)

# Changes

Version 2.0.0 introduces uses wait_for_it script for the cluster startup

# Hadoop Docker

## Supported Hadoop Versions
See repository branches for supported hadoop versions

## Quick Start

To deploy an example HDFS cluster, run:
```
  docker-compose up
```

Run example wordcount job:
```
  make wordcount
```

Or deploy in swarm:
```
docker stack deploy -c docker-compose-v3.yml hadoop
```

`docker-compose` creates a docker network that can be found by running `docker network list`, e.g. `dockerhadoop_default`.

Run `docker network inspect` on the network (e.g. `dockerhadoop_default`) to find the IP the hadoop interfaces are published on. Access these interfaces with the following URLs:

* Namenode: http://<dockerhadoop_IP_address>:9870/dfshealth.html#tab-overview
* History server: http://<dockerhadoop_IP_address>:8188/applicationhistory
* Datanode: http://<dockerhadoop_IP_address>:9864/
* Nodemanager: http://<dockerhadoop_IP_address>:8042/node
* Resource manager: http://<dockerhadoop_IP_address>:8088/

## Configure Environment Variables

The configuration parameters can be specified in the hadoop.env file or as environmental variables for specific services (e.g. namenode, datanode etc.):
```
  CORE_CONF_fs_defaultFS=hdfs://namenode:8020
```

CORE_CONF corresponds to core-site.xml. fs_defaultFS=hdfs://namenode:8020 will be transformed into:
```
  <property><name>fs.defaultFS</name><value>hdfs://namenode:8020</value></property>
```
To define dash inside a configuration parameter, use triple underscore, such as YARN_CONF_yarn_log___aggregation___enable=true (yarn-site.xml):
```
  <property><name>yarn.log-aggregation-enable</name><value>true</value></property>
```

The available configurations are:
* /etc/hadoop/core-site.xml CORE_CONF
* /etc/hadoop/hdfs-site.xml HDFS_CONF
* /etc/hadoop/yarn-site.xml YARN_CONF
* /etc/hadoop/httpfs-site.xml HTTPFS_CONF
* /etc/hadoop/kms-site.xml KMS_CONF
* /etc/hadoop/mapred-site.xml  MAPRED_CONF

If you need to extend some other configuration file, refer to base/entrypoint.sh bash script.

ENV JAR_FILEPATH="/opt/hadoop/applications/WordCount.jar"
ENV CLASS_TO_RUN="WordCount"
ENV PARAMS="/input /output"

$HADOOP_HOME/bin/hadoop jar $JAR_FILEPATH $CLASS_TO_RUN $PARAMS

bin/hadoop jar hadoop-mapreduce-examples-<ver>.jar wordcount -files cachefile.txt -libjars mylib.jar -archives myarchive.zip input output

yarn jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.2.4.jar wordcount "books/*" output

yarn jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.2.4.jar -Dmapreduce.job.queuename=bronze wordcount "books/*" output


#
$SPARK_HOME/bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode cluster \
--driver-memory 640M \
--executor-memory 640M \
--num-executors 2 \
--conf "spark.yarn.archive=hdfs://namenode:9000/archives/spark-3.1.2.zip" \
--conf "spark.yarn.queue=default" \
$SPARK_HOME/examples/jars/spark-examples_*.jar 50000

# --proxy-user

$SPARK_HOME/bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode cluster \
--driver-memory 640M \
--executor-memory 640M \
--num-executors 2 \
--principal brijeshdhaker@SANDBOX.NET \
--keytab /etc/kerberos/users/brijeshdhaker.keytab \
--conf "spark.yarn.archive=hdfs://namenode.sandbox.net:9000/archives/spark-3.1.2.zip" \
--conf "spark.yarn.queue=engineering" \
--conf "spark.eventLog.enabled=true" \
--conf "spark.eventLog.dir=hdfs://namenode.sandbox.net:9000/apps/var/log/spark" \
--conf "hadoop.yarn.timeline-service.enabled=false" \
$SPARK_HOME/examples/jars/spark-examples_*.jar 10000


$SPARK_HOME/bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode cluster \
--driver-memory 640M \
--executor-memory 640M \
--num-executors 2 \
--conf "spark.yarn.archive=hdfs://namenode.sandbox.net:9000/archives/spark-3.1.2.zip" \
--conf "spark.yarn.queue=engineering" \
--conf "spark.eventLog.enabled=true" \
--conf "spark.eventLog.dir=hdfs://namenode.sandbox.net:9000/apps/var/log/spark" \
--conf "hadoop.yarn.timeline-service.enabled=false" \
--conf "spark.kerberos.keytab=/etc/kerberos/users/brijeshdhaker.keytab" \
--conf "spark.kerberos.principal=brijeshdhaker@SANDBOX.NET" \
$SPARK_HOME/examples/jars/spark-examples_*.jar 10000