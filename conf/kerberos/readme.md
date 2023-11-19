====================================================
==== Kerberos KDC and Kadmin ======================================================
===================================================================================

REALM: SANDBOX.NET
ROOT_ADMIN_PRINCIPAL: root/admin@SANDBOX.NET
KADMIN_PRINCIPAL_FULL: kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD: kadmin
KUSERS_PASSWORD: kuser

===================================================================================
==== /etc/krb5.conf ===============================================================
===================================================================================

[logging]
  default = FILE:/var/log/krb5libs.log
  kdc = FILE:/var/log/krb5kdc.log
  admin_server = FILE:/var/log/kadmind.log

[libdefaults]
  default_realm = SANDBOX.NET
  dns_lookup_realm = false
  dns_lookup_kdc = false
  ticket_lifetime = 24h
  renew_lifetime = 7d
  forwardable = true
# udp_preference_limit = 1  # when TCP only should be used.

# uncomment the following if AD cross realm auth is ONLY providing DES encrypted tickets
# allow-weak-crypto = true

[realms]
	SANDBOX.NET = {
		kdc_ports = 88,750
		kadmind_port = 749
		kdc = kdcserver.sandbox.net
		admin_server = kdcserver.sandbox.net
	}
[domain_realm]
  .sandbox.net = SANDBOX.NET
  sandbox.net = SANDBOX.NET


===================================================================================
==== /etc/krb5kdc/kdc.conf ========================================================
===================================================================================
[logging]
    kdc = FILE:/var/log/krb5kdc.log
    admin_server = FILE:/var/log/kadmin.log

[kdcdefaults]
    kdc_ports = 88
    kdc_tcp_ports = 88

[realms]
	SANDBOX.NET = {
		acl_file = /etc/krb5kdc/kadm5.acl
		dict_file = /usr/share/dict/words
		kadmind_port = 749
		max_life = 12h 0m 0s
		max_renewable_life = 7d 0h 0m 0s
		# master_key_type = aes256-cts-hmac-sha1-96:normal
		supported_enctypes = aes256-cts:normal aes128-cts:normal des3-hmac-sha1:normal arcfour-hmac:normal camellia256-cts:normal camellia128-cts:normal
		default_principal_flags = +renewable, +forwardable
	}

===================================================================================
==== /etc/krb5kdc/kadm5.acl =======================================================
===================================================================================
root/admin@SANDBOX.NET  *
kadmin/admin@SANDBOX.NET *
noPermissions@SANDBOX.NET X

===================================================================================
==== Creating realm ===============================================================
===================================================================================
Database Master Password : bMqCgWBP2wDqYtCK36BnoC1CNXj9Ee
This script should be run on the master KDC/admin server to initialize
a Kerberos realm.  It will ask you to type in a master key password.
This password will be used to generate a key that is stored in
/etc/krb5kdc/stash.  You should try to remember this password, but it
is much more important that it be a strong password than that it be
remembered.  However, if you lose the password and /etc/krb5kdc/stash,
you cannot decrypt your Kerberos database.
Loading random data
Initializing database '/var/lib/krb5kdc/principal' for realm 'SANDBOX.NET',
master key name 'K/M@SANDBOX.NET'
You will be prompted for the database Master Password.
It is important that you NOT FORGET this password.
Enter KDC database master key:
Re-enter KDC database master key to verify:
 * Starting Kerberos KDC krb5kdc
   ...done.
 * Starting Kerberos administrative servers kadmind
   ...done.


Now that your realm is set up you may wish to create an administrative
principal using the addprinc subcommand of the kadmin.local program.
Then, this principal can be added to /etc/krb5kdc/kadm5.acl so that
you can use the kadmin program on other computers.  Kerberos admin
principals usually belong to a single user and end in /admin.  For
example, if jruser is a Kerberos administrator, then in addition to
the normal jruser principal, a jruser/admin principal should be
created.

Don't forget to set up DNS information so your clients can find your
KDC and admin servers.  Doing so is documented in the administration
guide.

===================================================================================
==== Creating default principals in the acl =======================================
===================================================================================
Adding root/admin@SANDBOX.NET principal
delete_principal: Principal does not exist while deleting principal "root/admin@SANDBOX.NET"
No policy specified for root/admin@SANDBOX.NET; defaulting to no policy
Principal "root/admin@SANDBOX.NET" created.

Adding kadmin/admin principal
Principal "kadmin/admin@SANDBOX.NET" deleted.
Principal "kadmin/admin@SANDBOX.NET" created.

Adding noPermissions principal
delete_principal: Principal does not exist while deleting principal "noPermissions@SANDBOX.NET"
Principal "noPermissions@SANDBOX.NET" created.




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
wkt /etc/kerberos/keytabs/nginx.service.keytab   	-- password brijeshdhaker

add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/keytabs/brijeshdhaker.keytab   	-- password brijeshdhaker

add_entry -password -p hive@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/keytabs/hive.keytab

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
kadmin: addprinc -randkey zkclient@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey consumer@SANDBOX.NET
kadmin: addprinc -randkey producer@SANDBOX.NET
kadmin: addprinc -randkey kafkaclient@SANDBOX.NET
kadmin: addprinc -randkey schemaregistry/schemaregistry.sandbox.net@SANDBOX.NET

kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper.keytab zookeeper/zookeeper.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafka.keytab kafka/kafkabroker.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/zkclient.keytab zkclient@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/consumer.keytab consumer@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/producer.keytab producer@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkaclient.keytab kafkaclient@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/schemaregistry.keytab schemaregistry/schemaregistry.sandbox.net@SANDBOX.NET


kadmin: addprinc -randkey zookeeper/zookeeper_a.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zookeeper/zookeeper_b.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey zookeeper/zookeeper_c.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker_a.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker_b.sandbox.net@SANDBOX.NET
kadmin: addprinc -randkey kafka/kafkabroker_c.sandbox.net@SANDBOX.NET


kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper_a.keytab zookeeper/zookeeper_a.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper_b.keytab zookeeper/zookeeper_b.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/zookeeper_c.keytab zookeeper/zookeeper_c.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkabroker_a.keytab kafka/kafkabroker_a.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkabroker_b.keytab kafka/kafkabroker_b.sandbox.net@SANDBOX.NET
kadmin: ktadd -k /etc/kerberos/keytabs/kafkabroker_c.keytab kafka/kafkabroker_c.sandbox.net@SANDBOX.NET

kadmin:  quit

$ ktutil
ktutil:  rkt /etc/kerberos/keytabs/kafkabroker.keytab
ktutil:  rkt /etc/kerberos/keytabs/schemaregistry.keytab
ktutil:  wkt /etc/kerberos/keytabs/kafka.keytab
ktutil:  clear
ktutil:  quit
