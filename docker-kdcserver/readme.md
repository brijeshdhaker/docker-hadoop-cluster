# 1. Add Principle

bash-3.00$ kadmin -p kadmin/admin@SANDBOX.NET
kadmin:


list_principals *
getprinc kadmin/admin@SANDBOX.NET
delete_principal
change_password jdb

add_principal root@SANDBOX.NET
add_principal brijeshdhaker@SANDBOX.NET

add_principal nn/namenode@SANDBOX.NET
add_principal host/namenode@SANDBOX.NET

add_principal dn/datanode@SANDBOX.NET
add_principal host/datanode@SANDBOX.NET

add_principal rm/resourcemanager@SANDBOX.NET
add_principal host/resourcemanager@SANDBOX.NET

add_principal nm/nodemanager@SANDBOX.NET
add_principal host/nodemanager@SANDBOX.NET

add_principal jhs/historyserver@SANDBOX.NET
add_principal host/historyserver@SANDBOX.NET

add_principal yts/_timelineserver@SANDBOX.NET
add_principal host/timelineserver@SANDBOX.NET

add_principal gw/gateway@SANDBOX.NET
add_principal host/gateway@SANDBOX.NET


# 1. Add Principle

[root@test5~]#ktutil

ktutil: add_entry -password -p brijeshdhaker/admin@SANDBOX.NET -k 1 -e des3-cbc-sha1-kd
Password for brijeshdhaker/admin@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker/admin@SANDBOX.NET -k 1 -e arcfour-hmac-md5
Password for brijeshdhaker/admin@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker/admin@SANDBOX.NET -k 1 -e des-hmac-sha1
Password for brijeshdhaker/admin@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker/admin@SANDBOX.NET -k 1 -e des-cbc-md5
Password for brijeshdhaker/admin@SANDBOX.NET:

ktutil: add_entry -password -p brijeshdhaker/admin@SANDBOX.NET -k 1 -e des-cbc-md4
Password for brijeshdhaker/admin@SANDBOX.NET:

# 3. Write Keytab 
ktutil

add_entry -password -p root/admin@SANDBOX.NET -k 1 -f
add_entry -password -p brijeshdhaker/admin@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/admins.keytab   	-- password admin

add_entry -password -p nn/namenode@SANDBOX.NET -k 1 -f
add_entry -password -p host/namenode@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/nn.service.keytab	-- password nn

add_entry -password -p dn/datanode@SANDBOX.NET -k 1 -f
add_entry -password -p host/datanode@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/dn.service.keytab  -- password dn

add_entry -password -p rm/resourcemanager@SANDBOX.NET -k 1 -f
add_entry -password -p host/resourcemanager@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/rm.service.keytab  -- password rm

add_entry -password -p nm/nodemanager@SANDBOX.NET -k 1 -f
add_entry -password -p host/nodemanager@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/nm.service.keytab 		-- password rm

add_entry -password -p jhs/historyserver@SANDBOX.NET -k 1 -f
add_entry -password -p host/historyserver@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/jhs.service.keytab 	-- password rm

add_entry -password -p yts/timelineserver@SANDBOX.NET -k 1 -f
add_entry -password -p host/timelineserver@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/yts.service.keytab 	-- password rm

add_entry -password -p gw/gateway@SANDBOX.NET -k 1 -f
add_entry -password -p host/gateway@SANDBOX.NET -k 1 -f
wkt /etc/security/keytab/gw.service.keytab 		-- password rm

HTTP/namenode@SANDBOX.NET
HTTP/datanode@SANDBOX.NET


#
kinit -k -t /etc/kerberos/keytabs/root.keytab root@SANDBOX.NET
kinit -k -t /etc/kerberos/keytabs/brijeshdhaker.keytab brijeshdhaker/admin@SANDBOX.NET

# OS Users
klist -e -k -t /etc/kerberos/keytabs/root.keytab
klist -e -k -t /etc/kerberos/keytabs/brijeshdhaker.keytab
klist -e -k -t /etc/kerberos/keytabs/yarn.keytab
klist -e -k -t /etc/kerberos/keytabs/hdfs.keytab
klist -e -k -t /etc/kerberos/keytabs/mapred.keytab

# Services
klist -e -k -t /etc/kerberos/keytabs/hdfs.service.keytab
klist -e -k -t /etc/kerberos/keytabs/yarn.service.keytab
klist -e -k -t /etc/kerberos/keytabs/mapred.service.keytab
klist -e -k -t /etc/kerberos/keytabs/host.service.keytab
klist -e -k -t /etc/kerberos/keytabs/HTTP.service.keytab

# Services