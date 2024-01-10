====================================================
==== Kerberos KDC and Kadmin ======================================================
===================================================================================

REALM: SANDBOX.NET
ROOT_ADMIN_PRINCIPAL: root/admin@SANDBOX.NET
KADMIN_PRINCIPAL_FULL: kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD: kadmin
KUSERS_PASSWORD: kuser

# 1. Add Principle

bash-3.00$ kadmin -w kadmin -p kadmin/admin@SANDBOX.NET
kadmin:
list_principals *
getprinc brijeshdhaker@SANDBOX.NET

delete_principal root@SANDBOX.NET
delete_principal brijeshdhaker@SANDBOX.NET
delete_principal hdfs@SANDBOX.NET
delete_principal yarn@SANDBOX.NET
delete_principal mapred@SANDBOX.NET
delete_principal hive@SANDBOX.NET

change_password brijeshdhaker@SANDBOX.NET

add_principal root@SANDBOX.NET
add_principal brijeshdhaker@SANDBOX.NET
add_principal hdfs@SANDBOX.NET
add_principal yarn@SANDBOX.NET
add_principal mapred@SANDBOX.NET
add_principal hive@SANDBOX.NET

# 1. Add Principle

[root@test5~]# ktutil

ktutil: add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -e des3-cbc-sha1-kd
Password for brijeshdhaker@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -e arcfour-hmac-md5
Password for brijeshdhaker@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -e des-hmac-sha1
Password for brijeshdhaker@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -e des-cbc-md5
Password for brijeshdhaker@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -e des-cbc-md4
Password for brijeshdhaker@SANDBOX.NET:

# 3. Write Keytab 
ktutil

add_entry -password -p root@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/keytabs/root.keytab  	-- password root

add_entry -password -p HTTP/nginx.sandbox.net@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/keytabs/nginx.service.keytab   	-- password kuser

add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/users/brijeshdhaker.keytab   	-- password kuser

add_entry -password -p hdfs@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/users/hdfs.keytab   	-- password kuser

add_entry -password -p hive@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/users/hive.keytab

#
kinit -k -t /etc/kerberos/keytabs/root.keytab root@SANDBOX.NET
kinit -k -t /etc/kerberos/users/brijeshdhaker.keytab brijeshdhaker@SANDBOX.NET
kinit -k -t /etc/kerberos/users/zeppelin.keytab zeppelin@SANDBOX.NET

kinit -k -t /etc/kerberos/keytabs/hdfs.service.keytab hdfs/thinkpad.sandbox.net@SANDBOX.NET
kinit -k -t /etc/kerberos/keytabs/hive.service.keytab hive/hiveserver.sandbox.net@SANDBOX.NET

# OS Users
klist -e -k -t /etc/kerberos/users/root.keytab
klist -e -k -t /etc/kerberos/users/brijeshdhaker.keytab
klist -e -k -t /etc/kerberos/users/hdfs.keytab
klist -e -k -t /etc/kerberos/users/hive.keytab
klist -e -k -t /etc/kerberos/users/hbase.keytab
klist -e -k -t /etc/kerberos/users/mapred.keytab
klist -e -k -t /etc/kerberos/users/sandbox.keytab
klist -e -k -t /etc/kerberos/users/spark.keytab
klist -e -k -t /etc/kerberos/users/yarn.keytab
klist -e -k -t /etc/kerberos/users/zookeeper.keytab

# Services
klist -e -k -t /etc/kerberos/keytabs/hdfs.service.keytab
klist -e -k -t /etc/kerberos/keytabs/yarn.service.keytab
klist -e -k -t /etc/kerberos/keytabs/mapred.service.keytab
klist -e -k -t /etc/kerberos/keytabs/hive.service.keytab
klist -e -k -t /etc/kerberos/keytabs/host.service.keytab
klist -e -k -t /etc/kerberos/keytabs/HTTP.service.keytab

# spnego
klist -e -k -t /etc/kerberos/keytabs/spnego.service.keytab


# Services

python -m http.server 8000

#
# Adding Service Principal
#

$ sudo kadmin.local

or
$ kadmin -w kadmin -p kadmin/admin@SANDBOX.NET

kadmin:  addprinc -randkey spark/sparkhistory.sandbox.net@SANDBOX.NET
kadmin:  addprinc -randkey host/sparkhistory.sandbox.net@SANDBOX.NET
kadmin:  addprinc -randkey HTTP/sparkhistory.sandbox.net@SANDBOX.NET

## -- To create the Kerberos keytab files

kadmin:  xst -norandkey -k hdfs.keytab hdfs/fully.qualified.domain.name HTTP/fully.qualified.domain.name

or
kadmin:  xst -k /etc/kerberos/keytabs/spark-unmerged.keytab spark/sparkhistory.sandbox.net
kadmin:  xst -k /etc/kerberos/keytabs/HTTP.service.keytab HTTP/sparkhistory.sandbox.net

$ ktutil
ktutil:  rkt /etc/kerberos/keytabs/spark-unmerged.keytab
ktutil:  rkt /etc/kerberos/keytabs/HTTP.service.keytab
ktutil:  wkt /etc/kerberos/keytabs/spark.service.keytab
ktutil:  clear
ktutil:  rkt mapred-unmerged.keytab
ktutil:  rkt http.keytab
ktutil:  wkt mapred.keytab
ktutil:  clear
ktutil:  rkt yarn-unmerged.keytab
ktutil:  rkt http.keytab
ktutil:  wkt yarn.keytab
ktutil:  clear
ktutil:  q


#
#
kadmin: ktadd -k /etc/kerberos/keytabs/brijesh-headless.keytab brijesh@SANDBOX.NET

$ kadmin: addprinc -randkey zookeeper/fqdn.example.com@YOUR-REALM

# 1. Create a service principal for the ZooKeeper server
kadmin: addprinc -randkey zookeeper/zookeeper.sandbox.net@SANDBOX.NET

# 2. Create a keytab file for the ZooKeeper server
$ kadmin
kadmin: xst -norandkey -k zookeeper.service.keytab zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: xst -norandkey -k /etc/kerberos/keytabs/appuser.keytab appuser/kafkabroker.sandbox.net@SANDBOX.NET

# 3. Create Keytab for kafka broker & registry 
$ kadmin
kadmin: delete_principal zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: delete_principal kafka/kafkabroker.sandbox.net@SANDBOX.NET
kadmin: delete_principal zkclient@SANDBOX.NET
kadmin: delete_principal consumer@SANDBOX.NET
kadmin: delete_principal producer@SANDBOX.NET
kadmin: delete_principal kafkaclient@SANDBOX.NET
kadmin: delete_principal schemaregistry/schemaregistry.sandbox.net@SANDBOX.NET

kadmin: addprinc -randkey zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zkclient@SANDBOX.NET
kadmin: addprinc -randkey consumer@SANDBOX.NET
kadmin: addprinc -randkey producer@SANDBOX.NET
kadmin: addprinc -randkey kafkaclient@SANDBOX.NET
kadmin: addprinc -randkey schemaregistry/schemaregistry.sandbox.net@SANDBOX.NET

kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper.keytab zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkabroker.keytab kafka/kafkabroker.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/zkclient.keytab zkclient@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/consumer.keytab consumer@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/producer.keytab producer@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkaclient.keytab kafkaclient@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/schemaregistry.keytab schemaregistry/schemaregistry.sandbox.net@SANDBOX.NET


delete_principal zookeeper/zookeeper-a.sandbox.net@SANDBOX.NET
delete_principal zookeeper/zookeeper-b.sandbox.net@SANDBOX.NET
delete_principal zookeeper/zookeeper-c.sandbox.net@SANDBOX.NET
delete_principal kafka/kafkabroker-a.sandbox.net@SANDBOX.NET
delete_principal kafka/kafkabroker-b.sandbox.net@SANDBOX.NET
delete_principal kafka/kafkabroker-c.sandbox.net@SANDBOX.NET

kadmin: addprinc -randkey zookeeper/zookeeper-a.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zookeeper/zookeeper-b.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zookeeper/zookeeper-c.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker-a.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker-b.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker-c.sandbox.net@SANDBOX.NET


kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper-a.keytab zookeeper/zookeeper-a.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper-b.keytab zookeeper/zookeeper-b.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper-c.keytab zookeeper/zookeeper-c.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkabroker-a.keytab kafka/kafkabroker-a.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkabroker-b.keytab kafka/kafkabroker-b.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkabroker-c.keytab kafka/kafkabroker-c.sandbox.net@SANDBOX.NET

kadmin:  quit

$ ktutil
ktutil:  rkt /etc/kerberos/keytabs/kafkabroker.keytab
ktutil:  rkt /etc/kerberos/keytabs/schemaregistry.keytab
ktutil:  wkt /etc/kerberos/keytabs/kafka.keytab
ktutil:  clear
ktutil:  quit
