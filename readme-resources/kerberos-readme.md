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


kadmin.local -q "list_principals *"
kadmin.local -q "getprinc brijeshdhaker@SANDBOX.NET"

# Add Principle & Passwordless Keytab

kadmin.local -q "delete_principal brijeshdhaker@SANDBOX.NET"
kadmin.local -q "addprinc -randkey brijeshdhaker@SANDBOX.NET"
kadmin.local -q "ktadd -k /apps/security/keytabs/users/brijeshdhaker.keytab brijeshdhaker@SANDBOX.NET"
kadmin.local -q "getprinc brijeshdhaker@SANDBOX.NET"
klist -k -t /apps/security/keytabs/users/brijeshdhaker.keytab
kinit -k -t /apps/security/keytabs/users/brijeshdhaker.keytab zeppelin@SANDBOX.NET
kdestroy

kadmin.local -q "delete_principal zeppelin@SANDBOX.NET"
kadmin.local -q "addprinc -randkey zeppelin@SANDBOX.NET"
kadmin.local -q "ktadd -k /apps/security/keytabs/users/zeppelin.keytab zeppelin@SANDBOX.NET"
kadmin.local -q "getprinc zeppelin@SANDBOX.NET"
klist -k -t /apps/security/keytabs/users/zeppelin.keytab
kinit -k -t /apps/security/keytabs/users/zeppelin.keytab zeppelin@SANDBOX.NET
kdestroy


delete_principal root@SANDBOX.NET
delete_principal hdfs@SANDBOX.NET
delete_principal yarn@SANDBOX.NET
delete_principal mapred@SANDBOX.NET
delete_principal hive@SANDBOX.NET

change_password brijeshdhaker@SANDBOX.NET

add_principal root@SANDBOX.NET
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
wkt /apps/security/keytabs/services/root.keytab  	-- password root

add_entry -password -p HTTP/nginx.sandbox.net@SANDBOX.NET -k 1 -f
wkt /apps/security/keytabs/services/nginx.service.keytab   	-- password kuser

add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -f
wkt /apps/security/keytabs/users/brijeshdhaker.keytab   	-- password kuser

add_entry -password -p hdfs@SANDBOX.NET -k 1 -f
wkt /apps/security/keytabs/users/hdfs.keytab   	-- password kuser

add_entry -password -p hive@SANDBOX.NET -k 1 -f
wkt /apps/security/keytabs/users/hive.keytab

# OS Users
klist -e -k -t /apps/security/keytabs/users/root.keytab
klist -e -k -t /apps/security/keytabs/users/brijeshdhaker.keytab
klist -e -k -t /apps/security/keytabs/users/hdfs.keytab
klist -e -k -t /apps/security/keytabs/users/hive.keytab
klist -e -k -t /apps/security/keytabs/users/hbase.keytab
klist -e -k -t /apps/security/keytabs/users/mapred.keytab
klist -e -k -t /apps/security/keytabs/users/sandbox.keytab
klist -e -k -t /apps/security/keytabs/users/spark.keytab
klist -e -k -t /apps/security/keytabs/users/yarn.keytab
klist -e -k -t /apps/security/keytabs/users/zookeeper.keytab

# Services
klist -e -k -t /apps/security/keytabs/services/hdfs.service.keytab
klist -e -k -t /apps/security/keytabs/services/yarn.service.keytab
klist -e -k -t /apps/security/keytabs/services/mapred.service.keytab
klist -e -k -t /apps/security/keytabs/services/hive.service.keytab
klist -e -k -t /apps/security/keytabs/services/host.service.keytab
klist -e -k -t /apps/security/keytabs/services/HTTP.service.keytab

# spnego
klist -e -k -t /apps/security/keytabs/services/spnego.service.keytab


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
kadmin:  xst -k /apps/security/keytabs/services/spark-unmerged.keytab spark/sparkhistory.sandbox.net
kadmin:  xst -k /apps/security/keytabs/services/HTTP.service.keytab HTTP/sparkhistory.sandbox.net

$ ktutil
ktutil:  rkt /apps/security/keytabs/services/spark-unmerged.keytab
ktutil:  rkt /apps/security/keytabs/services/HTTP.service.keytab
ktutil:  wkt /apps/security/keytabs/services/spark.service.keytab
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
kadmin: ktadd -k /apps/security/keytabs/services/brijesh-headless.keytab brijesh@SANDBOX.NET

$ kadmin: addprinc -randkey zookeeper/fqdn.example.com@YOUR-REALM

# 1. Create a service principal for the ZooKeeper server
kadmin: addprinc -randkey zookeeper/zookeeper.sandbox.net@SANDBOX.NET

# 2. Create a keytab file for the ZooKeeper server
$ kadmin
kadmin: xst -norandkey -k zookeeper.service.keytab zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: xst -norandkey -k /apps/security/keytabs/services/appuser.keytab appuser/kafka-broker.sandbox.net@SANDBOX.NET

# 3. Create Keytab for kafka broker & registry 
$ kadmin
kadmin: delete_principal zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: delete_principal kafka/kafka-broker.sandbox.net@SANDBOX.NET
kadmin: delete_principal zkclient@SANDBOX.NET
kadmin: delete_principal consumer@SANDBOX.NET
kadmin: delete_principal producer@SANDBOX.NET
kadmin: delete_principal kafkaclient@SANDBOX.NET
kadmin: delete_principal schema-registry/schema-registry.sandbox.net@SANDBOX.NET

kadmin: addprinc -randkey zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafka-broker.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zkclient@SANDBOX.NET
kadmin: addprinc -randkey consumer@SANDBOX.NET
kadmin: addprinc -randkey producer@SANDBOX.NET
kadmin: addprinc -randkey kafkaclient@SANDBOX.NET
kadmin: addprinc -randkey schema-registry/schema-registry.sandbox.net@SANDBOX.NET

kadmin: ktadd -k /apps/security/keytabs/services/zookeeper.keytab zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/kafka-broker.keytab kafka/kafka-broker.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/zkclient.keytab zkclient@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/consumer.keytab consumer@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/producer.keytab producer@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/kafkaclient.keytab kafkaclient@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/schema-registry.keytab schema-registry/schema-registry.sandbox.net@SANDBOX.NET


delete_principal zookeeper/zookeeper-a.sandbox.net@SANDBOX.NET
delete_principal zookeeper/zookeeper-b.sandbox.net@SANDBOX.NET
delete_principal zookeeper/zookeeper-c.sandbox.net@SANDBOX.NET
delete_principal kafka/kafka-broker-a.sandbox.net@SANDBOX.NET
delete_principal kafka/kafka-broker-b.sandbox.net@SANDBOX.NET
delete_principal kafka/kafka-broker-c.sandbox.net@SANDBOX.NET

kadmin: addprinc -randkey zookeeper/zookeeper-a.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zookeeper/zookeeper-b.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zookeeper/zookeeper-c.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafka-broker-a.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafka-broker-b.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafka-broker-c.sandbox.net@SANDBOX.NET


kadmin: ktadd -k /apps/security/keytabs/services/zookeeper-a.keytab zookeeper/zookeeper-a.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/zookeeper-b.keytab zookeeper/zookeeper-b.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/zookeeper-c.keytab zookeeper/zookeeper-c.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/kafka-broker-a.keytab kafka/kafka-broker-a.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/kafka-broker-b.keytab kafka/kafka-broker-b.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /apps/security/keytabs/services/kafka-broker-c.keytab kafka/kafka-broker-c.sandbox.net@SANDBOX.NET

kadmin:  quit

$ ktutil
ktutil:  rkt /apps/security/keytabs/services/kafka-broker.keytab
ktutil:  rkt /apps/security/keytabs/services/schema-registry.keytab
ktutil:  wkt /apps/security/keytabs/services/kafka.keytab
ktutil:  clear
ktutil:  quit
