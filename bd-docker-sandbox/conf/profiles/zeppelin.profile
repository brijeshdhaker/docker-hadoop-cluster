#
export TERM=xterm-color

# JAVA_HOME for Ubuntu
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin

# Maven Path
export M2_HOME=/opt/maven-3.6.3
export PATH=$M2_HOME/bin:$PATH

# Python
export PATH=/opt/conda/bin:$PATH
export PYSPARK_PYTHON=/usr/bin/python3
export PYSPARK_DRIVER_PYTHON=/usr/bin/python3

#
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

#
export HBASE_HOME=/opt/hbase
export PATH=$PATH:$HBASE_HOME/bin

#
export HIVE_HOME=/opt/hive
export HIVE_CONF_DIR=/etc/hive/conf
export PATH=$PATH:$HIVE_HOME/bin


#
export HADOOP_HOME=/opt/hadoop
export HADOOP_CONF_DIR=/opt/hadoop/etc/hadoop
export HADOOP_MAPRED_HOME=/opt/hadoop
export HADOOP_COMMON_HOME=/opt/hadoop
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${HADOOP_HOME}/lib/native
export PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin
export HADOOP_CLIENT_OPTS="-Djava.security.krb5.conf=/etc/krb5.conf"

#
export YARN_HOME=/opt/hadoop
export YARN_SITE_DIR=/opt/hadoop/etc/hadoop
export YARN_OPTS="-Djava.security.krb5.conf=/etc/krb5.conf"

# Kerberos
export KRB5_CONFIG=/etc/krb5.conf
export KRB5CCNAME="FILE:$HOME/krb5cc_$(id -u)"
export USER=zeppelin
kinit -k -t /apps/security/keytabs/users/zeppelin.keytab zeppelin@SANDBOX.NET



