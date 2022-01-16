---------------------------
Cloudera QuickStart VM 6.3.2
CentOS 7 + GNOME Based
Java Eclipse & Scala Eclipse IDE Included
MySql With 'retail_db' Installed


---------------------------
Minimum System Requirement - 2/4 Cores + 16GB RAM
https://www.peazip.org/
http://bit.ly/GetVMPlayer

---------------------------
Download In 3 Parts (Direct Links)
Part 1 - https://bit.ly/CDH_6_3_2_CentOS7_1
Part 2 - https://bit.ly/CDH_6_3_2_CentOS7_2
Part 3 - https://bit.ly/CDH_6_3_2_CentOS7_3
OR 
http://bit.ly/Minus1By12Repo


---------------------------
CentOS GUI Login 'Base User' Password - BaseUser@123
'root'  Password - BaseUser@123

#
#---------------------------
#
sudo user - osboxes
sudo password - BaseUser@123

#
#---------------------------
#
MySql user - root
MySql password - bigdata
---------------------------
Cloudera Manager user - admin
Cloudera Manager password - admin
---------------------------
http://www.Minus1By12.com  
http://bit.ly/Minus1By12YouTube
http://bit.ly/Minus1By12Repo
https://app.vagrantup.com/Minus1By12
https://hub.docker.com/u/minus1by12
https://pypi.org/project/Minus1By12
https://github.com/MinusOneByTwelve


JAVA_HOME=/usr/java/jdk1.8.0_181-cloudera
SPARK_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/spark
HADOOP_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hadoop
HIVE_HOME=/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hive
HUE_HOME=/opt/cloudera/parcels/CDH/lib/hue

export PYSPARK_DRIVER_PYTHON="/usr/bin/python2"
export PYSPARK_PYTHON="/usr/bin/python2"

```commandline
brijeshdhaker@thinkpad:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs$ sudo useradd -M hadoop
brijeshdhaker@thinkpad:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs$ sudo useradd -M hive
brijeshdhaker@thinkpad:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs$ sudo useradd -M spark
brijeshdhaker@thinkpad:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs$ sudo useradd -M hdfs
brijeshdhaker@thinkpad:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs$ sudo useradd -M hue
brijeshdhaker@thinkpad:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs$ sudo useradd -M impala
brijeshdhaker@thinkpad:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs$ sudo useradd -M mapred
```
#
# HDFS Web Access
#

http://quickstart-bigdata:9870/webhdfs/v1/user/?op=LISTSTATUS

#
#
#
sudo apt-get install virtualbox-guest-utils virtualbox-guest-x11 virtualbox-guest-dkms

#
setup mac : 080027E5AC20

#
#
#
vi /etc/hosts

192.168.1.101	quickstart-bigdata

#
#
#

vi /etc/cloudera-scm-agent/config.ini

host = quickstart-bigdata

#
#
#
sudo systemctl restart cloudera-scm-agent
sudo systemctl restart cloudera-scm-server

#
#
#


#
# Install VM Player
#


#
# Uninstall VM Player
#

vmware-installer -u vmware-player
vmware-installer -u vmware-workstation
vmware-uninstall-tools.pl


#
#
#
sudo useradd -m brijeshdhaker
sudo usermod -aG wheel brijeshdhaker

#
# Service Account
#
sudo adduser -d /home/zeppelin -s /sbin/nologin zeppelin
sudo -u hdfs hadoop fs -mkdir -p /user/zeppelin
sudo -u hdfs hadoop fs -chown -R zeppelin:zeppelin /user/zeppelin
sudo -u hdfs hadoop fs -chmod -R 777 /user/zeppelin

#
#
#
sudo -u hdfs hadoop fs -mkdir -p /user/brijeshdhaker
sudo -u hdfs hadoop fs -chown -R brijeshdhaker:brijeshdhaker /user/brijeshdhaker
sudo -u hdfs hadoop fs -mkdir -p /user/brijeshdhaker/archives
sudo -u hdfs hadoop fs -chmod -R 777 /user/brijeshdhaker/archives
sudo -u hdfs hadoop fs -chown -R brijeshdhaker:brijeshdhaker /user/brijeshdhaker/archives



sudo -u hdfs hdfs dfs –put /opt/cloudera/parcels/CDH/lib/spark/jars/* /user/brijeshdhaker/jars/spark-2.4.0/
sudo -u hdfs hdfs dfs –put *.zip /user/brijeshdhaker/jars/spark-2.4.0/


zip -rq spark-2.4.0.zip *.jar

sudo zip -rq spark-2.4.0.zip . -i /libs/spark-2.4.0/*.jar
sudo -u hdfs hdfs dfs –put /opt/cloudera/parcels/CDH/lib/spark/jars/* /user/brijeshdhaker/jars/spark-2.4.0/


/opt/cloudera/parcels/CDH/lib/spark/jars

#
## Copy spark2 jars in created folder
#
sudo -u hdfs hadoop fs -mkdir -p /archives
sudo -u hdfs hadoop fs -chmod -R 777 /archives
sudo -u hdfs hadoop fs -mkdir -p /libs
sudo -u hdfs hadoop fs -chmod -R 777 /libs

/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/spark
/opt/cloudera/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554/lib/hadoop

export HADOOP_CONF_DIR=/opt/hadoop-3.0.0/conf

#
$ spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode cluster \
--executor-memory 512MB \
--num-executors 2 \
--conf "spark.yarn.archive=hdfs:///archives/spark-2.4.0.zip" \
/opt/cloudera/parcels/CDH/lib/spark/examples/jars/spark-examples_*.jar 2


spark-submit \
    --name "Python Spark Application" \
    --master local[*] \
    --conf "spark.executorEnv.PYSPARK_DRIVER_PYTHON=$PYSPARK_DRIVER_PYTHON" \
    --conf "spark.executorEnv.PYSPARK_PYTHON=$PYSPARK_PYTHON" \
    --archives "/apps/hostpath/spark/artifacts/py/venv.zip#venv" \
    --py-files "/apps/hostpath/spark/artifacts/py/application.zip" /apps/hostpath/spark/artifacts/py/py-hello.py
    
    
spark-submit \
    --name "Python Spark Application" \
    --master local[*] \
    --py-files /home/brijeshdhaker/PycharmProjects/pyspark-hive-integration.zip pyspark-hive-integration/pyspark-hive.py


spark-submit \
    --name "Python Spark Application" \
    --master local[*] \
    --py-files /home/brijeshdhaker/PycharmProjects/pyspark-hive-integration.zip pyspark-hive-integration/pyspark-hive-session.py
    
#
# Schema Registory
#

$> mysql -u root -p
$> Enter password: bigdata
mysql> create database schema_registry;
mysql> CREATE USER 'registry_user'@'localhost' IDENTIFIED BY 'registry_password';
mysql> GRANT ALL PRIVILEGES ON schema_registry.* TO 'registry_user'@'localhost' WITH GRANT OPTION;
mysql> commit;

./bootstrap/bootstrap-storage.sh
./bin/registry-server-start.sh ./conf/registry-mysql.yaml &

/opt/hortonworks-registry-0.5.0/bin/registry-server-start.sh /opt/hortonworks-registry-0.5.0/conf/registry-mysql.yaml

http://sandbox-hdp.hortonworks.com:7788/api/v1
