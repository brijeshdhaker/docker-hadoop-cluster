#!/bin/bash

# /etc/kerberos/setup-osuser-principles.sh

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
OS_USERS=("sandbox" "brijeshdhaker" "hdfs" "yarn" "mapred" "hive" "hbase" "spark" "zeppelin" "flink")
for ou in "${OS_USERS[@]}"
do
  # delete existing keytab files
  rm -Rf /apps/security/keytabs/users/$ou.keytab
  kadmin.local -q "delete_principal -force $ou@$REALM"
  kadmin.local -q "addprinc -randkey $ou@$REALM"
  kadmin.local -q "ktadd -k /apps/security/keytabs/users/$ou.keytab $ou@$REALM"
  kadmin.local -q "getprinc $ou@$REALM"

  klist -k -t /apps/security/keytabs/users/$ou.keytab
  # kinit -k -t /apps/security/keytabs/users/$ou.keytab $ou@$REALM
  chown 1000:root /apps/security/keytabs/users/$ou.keytab
  chmod 775 /apps/security/keytabs/users/$ou.keytab

done

echo ""
echo "OS users principles password successfully generated."
echo ""