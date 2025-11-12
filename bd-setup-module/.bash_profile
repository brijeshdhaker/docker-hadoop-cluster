#
export TERM=xterm-color

# k8s
alias kubectl='microk8s kubectl'

#conda path
export PATH=/opt/conda/bin:$PATH

# JAVA_HOME for Fedora
# export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk

# JAVA_HOME for Ubuntu
export JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin

# Kerberos
export KRB5_CONFIG=/etc/krb5.conf
export KRB5CCNAME="FILE:$HOME/.krb5cc_$(id -u)"

# Maven Path
export M2_HOME=/opt/maven-3.6.3
export PATH=$M2_HOME/bin:$PATH

# User specific environment and startup programs
export CLUSTER_TYPE=SANDBOX3

#
export PYSPARK_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python
export PYSPARK_DRIVER_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python
#
export SPARK_HOME=/opt/spark-3.4.1
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
export PYTHONPATH=$(ZIPS=("$SPARK_HOME"/python/lib/*.zip); IFS=:; echo "${ZIPS[*]}"):$PYTHONPATH

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
export YARN_HOME=/opt/hadoop-3.3.4
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${HADOOP_HOME}/lib/native
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${YARN_HOME}/bin:${YARN_HOME}/sbin:${HADOOP_MAPRED_HOME}/bin:${HADOOP_MAPRED_HOME}/sbin


