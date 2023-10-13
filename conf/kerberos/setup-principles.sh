#!/bin/bash

#
#
ROOT_ADMIN_PRINCIPAL=root/admin@SANDBOX.NET
KADMIN_PRINCIPAL_FULL=kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD=kadmin
KUSERS_PASSWORD=kuser

# delete existing keytab files
echo "Adding OS User Principal"
echo ""
OS_USERS=("brijeshdhaker" "hdfs" "yarn" "mapred" "hive" "spark" "hbase" "zookeeper" "zeppelin")
for ou in "${OS_USERS[@]}"
do
  rm -Rf /etc/kerberos/users/$ou.keytab
  kadmin.local -q "delete_principal -force $ou@$REALM"
  kadmin.local -q "addprinc -randkey $ou@$REALM"
  ktmerge="change_password $ou@$REALM\n$KUSERS_PASSWORD\n$KUSERS_PASSWORD\n"
  echo -e "$ktmerge" | kadmin.local
  kadmin.local -q "xst -norandkey -k /etc/kerberos/users/$ou.keytab $ou@$REALM"
  chmod 444 /etc/kerberos/users/$ou.keytab
done

#
echo "Adding service principal for all hosts."
echo ""
#for var in one two three; do echo "$var"; done
SERVICES=("zookeeper" "hdfs" "yarn" "mapred" "hbase" "hive" "spark" "host" "zeppelin" "HTTP", "kafka")
SANDBOX_NODES=(
  "namenode.sandbox.net"
  "datanode.sandbox.net"
  "secondary.sandbox.net"
  "resourcemanager.sandbox.net"
  "nodemanager.sandbox.net"
  "historyserver.sandbox.net"
  "timelineserver.sandbox.net"
  "metastore.sandbox.net"
  "hiveserver.sandbox.net"
  "sparkhistory.sandbox.net"
  "kafkabroker.sandbox.net"
  "schemaregistry.sandbox.net"
  "zookeeper.sandbox.net"
  "hmaster.sandbox.net"
  "hregion.sandbox.net"
  "zeppelin.sandbox.net"
  "gateway.sandbox.net"
  "thinkpad.sandbox.net"
  "hostmaster.sandbox.net"
)
for svc in "${SERVICES[@]}"
do
  echo ""
  for node in "${SANDBOX_NODES[@]}"
  do
      svc_principal=$svc/$node@$REALM
      keytab=$svc.service.$node.keytab
      rm -Rf /etc/kerberos/keytabs/$keytab
      kadmin.local -q "delete_principal -force $svc_principal"
      kadmin.local -q "addprinc -randkey $svc_principal"
      ktmerge="change_password $svc_principal\n$KUSERS_PASSWORD\n$KUSERS_PASSWORD\n"
      echo -e "$ktmerge" | kadmin.local
      kadmin.local -q "xst -norandkey -k /etc/kerberos/keytabs/$keytab $svc_principal"
      chmod 444 /etc/kerberos/keytabs/$keytab
  done
done

# Create unmerged keytab
for svc in "${SERVICES[@]}"
do
  rm -Rf /etc/kerberos/keytabs/$svc.keytab
  ktunmerged=""
  for node in "${SANDBOX_NODES[@]}"
  do
      ktunmerged+="rkt /etc/kerberos/keytabs/$svc.service.$node.keytab\n"
  done
  ktunmerged+="wkt /etc/kerberos/keytabs/$svc.keytab\nquit"
  echo ""
  echo -e "$ktunmerged" | ktutil
  chmod 444 /etc/kerberos/keytabs/$svc.keytab
done

# Create merged keytab for services
UNMERGED_SERVICES=("hdfs" "yarn" "mapred" "hive" "spark" "hbase" "zookeeper" "zeppelin")
for usvc in "${UNMERGED_SERVICES[@]}"
do
  rm -Rf /etc/kerberos/keytabs/$usvc.service.keytab
  ktmerge=""
  ktmerge+="rkt /etc/kerberos/keytabs/$usvc.keytab\n"
  ktmerge+="rkt /etc/kerberos/keytabs/HTTP.keytab\n"
  ktmerge+="rkt /etc/kerberos/keytabs/host.keytab\n"
  ktmerge+="wkt /etc/kerberos/keytabs/$usvc.service.keytab\nquit"
  echo ""
  echo -e "$ktmerge" | ktutil
  chmod 444 /etc/kerberos/keytabs/$usvc.service.keytab
done

# Setup spnego.service.keytab
rm -Rf /etc/kerberos/keytabs/host.service.keytab
cp /etc/kerberos/keytabs/host.keytab /etc/kerberos/keytabs/host.service.keytab
chmod 444 /etc/kerberos/keytabs/host.service.keytab

rm -Rf /etc/kerberos/keytabs/HTTP.service.keytab
cp /etc/kerberos/keytabs/HTTP.keytab /etc/kerberos/keytabs/HTTP.service.keytab
chmod 444 /etc/kerberos/keytabs/HTTP.service.keytab

rm -Rf /etc/kerberos/keytabs/spnego.service.keytab
cp /etc/kerberos/keytabs/HTTP.keytab /etc/kerberos/keytabs/spnego.service.keytab
chmod 444 /etc/kerberos/keytabs/spnego.service.keytab

#
echo "Principles successfully generated."
echo ""