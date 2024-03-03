#
export TERM=xterm-color

#conda path
export PATH=/opt/conda/bin:$PATH

# JAVA_HOME for cloudera
#export JAVA_HOME=/usr/java/jdk1.8.0_181-cloudera

# JAVA_HOME for Fedora
#export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk

# JAVA_HOME for Ubuntu
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin

# Maven Path
export M2_HOME=/opt/maven-3.6.3
export PATH=$M2_HOME/bin:$PATH

# User specific environment and startup programs
export CLUSTER_TYPE=SANDBOX3

#
#
#
if [ "$CLUSTER_TYPE" == "SANDBOX3"  ]
then
    #
    export PYSPARK_PYTHON=/opt/conda/envs/pyspark37/bin/python
    export PYSPARK_DRIVER_PYTHON=/opt/conda/envs/pyspark37/bin/python
    #
    export SPARK_HOME=/opt/spark-3.5.0
    export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
    #
    export HBASE_HOME=/opt/hbase-2.4.6
    export PATH=$PATH:$HBASE_HOME/bin
    #
    export HIVE_HOME=/opt/hive-3.1.3
    export PATH=$PATH:$HIVE_HOME/bin
    #
    export HADOOP_HOME=/opt/hadoop-3.3.4
    export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
    export HADOOP_MAPRED_HOME=/opt/hadoop-3.3.4
    export HADOOP_COMMON_HOME=/opt/hadoop-3.3.4
    #export HADOOP_HDFS_HOME=/opt/hadoop-3.3.4
    export YARN_HOME=/opt/hadoop-3.3.4
    export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${HADOOP_HOME}/lib/native
    export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${HADOOP_HDFS_HOME}/bin:${HADOOP_HDFS_HOME}/sbin:${YARN_HOME}/bin:${YARN_HOME}/sbin:${HADOOP_MAPRED_HOME}/bin:${HADOOP_MAPRED_HOME}/sbin

fi

# Kerberos
export KRB5_CONFIG=/etc/krb5.conf
export KRB5CCNAME="FILE:$HOME/.krb5cc_$(id -u)"
