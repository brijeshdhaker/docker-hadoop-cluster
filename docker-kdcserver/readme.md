# 1. Add Principle

bash-3.00$ kadmin -w kadmin -p kadmin/admin@SANDBOX.NET
kadmin:
list_principals *
getprinc brijeshdhaker@SANDBOX.NET
delete_principal brijeshdhaker@SANDBOX.NET
change_password brijeshdhaker@SANDBOX.NET

add_principal root@SANDBOX.NET
add_principal brijeshdhaker@SANDBOX.NET


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
wkt /etc/kerberos/keytabs/root.keytab   	-- password root

add_entry -password -p brijeshdhaker@SANDBOX.NET -k 1 -f
wkt /etc/kerberos/keytabs/brijeshdhaker.keytab   	-- password brijeshdhaker

#
kinit -k -t /etc/kerberos/keytabs/root.keytab root@SANDBOX.NET
kinit -k -t /etc/kerberos/keytabs/brijeshdhaker.keytab brijeshdhaker@SANDBOX.NET

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