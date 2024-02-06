#!/bin/bash

#
#
REALM=SANDBOX.NET
DOMAIN_REALM=sandbox.net
ROOT_ADMIN_PRINCIPAL=root/admin@SANDBOX.NET
KADMIN_PRINCIPAL_FULL=kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD=kadmin
KUSERS_PASSWORD=kuser

echo ""
echo "Adding OS users kerberos principals."
echo ""
#
OS_USERS=("sandbox" "brijeshdhaker" "hdfs" "yarn" "mapred" "hive" "hbase" "spark" "zeppelin")
for ou in "${OS_USERS[@]}"
do
  # delete existing keytab files
  rm -Rf /etc/kerberos/users/$ou.keytab
  kadmin.local -q "delete_principal -force $ou@$REALM"
  kadmin.local -q "addprinc -randkey $ou@$REALM"
  kadmin.local -q "ktadd -k /etc/kerberos/users/$ou.keytab $ou@$REALM"
  kadmin.local -q "getprinc $ou@$REALM"

  klist -k -t /etc/kerberos/users/$ou.keytab
  # kinit -k -t /etc/kerberos/users/$ou.keytab $ou@$REALM

  chmod 744 /etc/kerberos/users/$key.keytab
done

echo ""
echo "OS users principles password successfully generated."
echo ""