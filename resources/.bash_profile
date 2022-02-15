# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs
export CLUSTER_TYPE=SANDBOX3

#
export TERM=xterm-color

# Maven Path
export M2_HOME=/opt/sandbox/maven-3.6.3
export PATH=$M2_HOME/bin:$PATH

#conda path
export PATH=/opt/sandbox/conda/bin:$PATH

#
#export JAVA_HOME=/usr/java/jdk1.8.0_181-cloudera
# Fedora
#export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk
# Ubuntu
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin

if [ "$CLUSTER_TYPE" == "SANDBOX2"  ]
then
#
export PYSPARK_PYTHON=/usr/bin/python2
export PYSPARK_DRIVER_PYTHON=/usr/bin/python2
#
export SPARK_HOME=/opt/sandbox/spark-2.3.4
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
#
export HBASE_HOME=/opt/sandbox/hbase-2.4.9
export PATH=$PATH:$HBASE_HOME/bin
#
export HIVE_HOME=/opt/sandbox/hive-2.3.9
export PATH=$PATH:$HIVE_HOME/bin
#
export HADOOP_HOME=/opt/sandbox/hadoop-2.10.1
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export HADOOP_MAPRED_HOME=/opt/sandbox/hadoop-2.10.1
export HADOOP_COMMON_HOME=/opt/sandbox/hadoop-2.10.1
export YARN_HOME=/opt/sandbox/hadoop-2.10.1
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${HADOOP_HOME}/lib/native
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${HADOOP_HDFS_HOME}/bin:${HADOOP_HDFS_HOME}/sbin:${YARN_HOME}/bin:${YARN_HOME}/sbin:${HADOOP_MAPRED_HOME}/bin:${HADOOP_MAPRED_HOME}/sbin

fi
#
#
if [ "$CLUSTER_TYPE" == "SANDBOX3"  ]
then
#
export PYSPARK_PYTHON=/usr/bin/python3
export PYSPARK_DRIVER_PYTHON=/usr/bin/python3
#
export SPARK_HOME=/opt/sandbox/spark-3.1.2
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
#
export HBASE_HOME=/opt/sandbox/hbase-2.4.9
export PATH=$PATH:$HBASE_HOME/bin
#
export HIVE_HOME=/opt/sandbox/hive-3.1.2
export PATH=$PATH:$HIVE_HOME/bin
#
export HADOOP_HOME=/opt/sandbox/hadoop-3.2.1
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export HADOOP_MAPRED_HOME=/opt/sandbox/hadoop-3.2.1
export HADOOP_COMMON_HOME=/opt/sandbox/hadoop-3.2.1
#export HADOOP_HDFS_HOME=/opt/sandbox/hadoop-3.2.1
export YARN_HOME=/opt/sandbox/hadoop-3.2.1
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${HADOOP_HOME}/lib/native
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${HADOOP_HDFS_HOME}/bin:${HADOOP_HDFS_HOME}/sbin:${YARN_HOME}/bin:${YARN_HOME}/sbin:${HADOOP_MAPRED_HOME}/bin:${HADOOP_MAPRED_HOME}/sbin

fi
#
# CDH Cluster Setup
#
if [ "$CLUSTER_TYPE" == "CDH"  ]
then
#
export PYSPARK_PYTHON=/usr/bin/python2
export PYSPARK_DRIVER_PYTHON=/usr/bin/python2
#
export SPARK_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
#
export HADOOP_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hadoop
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export HADOOP_MAPRED_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hadoop-mapreduce
export HADOOP_COMMON_HOME=${HADOOP_HOME}
export HADOOP_HDFS_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hadoop-hdfs
export YARN_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hadoop-yarn
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${HADOOP_HDFS_HOME}/bin:${HADOOP_HDFS_HOME}/sbin:${YARN_HOME}/bin:${YARN_HOME}/sbin:${HADOOP_MAPRED_HOME}/bin:${HADOOP_MAPRED_HOME}/sbin
#
export HBASE_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hbase
export PATH=$PATH:$HBASE_HOME/bin
#
export HIVE_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hive
export PATH=$PATH:$HIVE_HOME/sbin:$HIVE_HOME/bin
# export CLASSPATH=$CLASSPATH:$HADOOP_HOME/lib/*:$HIVE_HOME/lib/*

fi
