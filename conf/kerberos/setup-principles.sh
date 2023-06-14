#!/bin/bash

#
#
KADMIN_PRINCIPAL_FULL=kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD=kadmin
KUSERS_PASSWORD=kuser

# delete existing keytab files
rm -Rf /etc/kerberos/keytabs/*.keytab

echo "Adding OS User Principal"
echo ""
OS_USERS=("root" "brijeshdhaker" "hdfs" "yarn" "mapred" "hive" "hbase" "spark")
for ou in "${OS_USERS[@]}"
do
  kadmin.local -q "delete_principal -force $ou@$REALM"
  kadmin.local -q "addprinc -pw $KUSERS_PASSWORD $ou@$REALM"
done

# Change passwords for OS Users
for ou in "${OS_USERS[@]}"
do
  ktmerge=""
  ktmerge+="change_password $ou\n$KUSERS_PASSWORD\nk$KUSERS_PASSWORD\nquit"
  echo ""
  echo -e "$ktmerge" | kadmin -w $KADMIN_PASSWORD -p $KADMIN_PRINCIPAL_FULL
  kadmin.local ktadd -k "/etc/kerberos/keytabs/$ou.keytab" "$ou@$REALM"
done

echo "Adding service principal for all hosts."
echo ""
#for var in one two three; do echo "$var"; done
PRINCIPALS=("hdfs" "yarn" "mapred" "hive" "spark" "hbase" "host" "HTTP")
SANDBOX_NODES=(
  "zookeeper.sandbox.net"
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
  "hbasemaster.sandbox.net"
  "regionserver.sandbox.net"
  "gateway.sandbox.net"
  "thinkpad.sandbox.net"
  "hostmaster.sandbox.net"
  "dockerhost.sandbox.net"
)
for pr in "${PRINCIPALS[@]}"
do
  echo ""
  for node in "${SANDBOX_NODES[@]}"
  do
      kadmin.local -q "delete_principal -force $pr/$node@$REALM"
      kadmin.local -q "addprinc -randkey -pw $KUSERS_PASSWORD $pr/$node@$REALM"
  done
done

# Change Passwords for Service Principals
for pr in "${PRINCIPALS[@]}"
do
  echo ""
  for node in "${SANDBOX_NODES[@]}"
  do
    ktmerge=""
    ktmerge+="change_password $pr/$node@$REALM\n$KUSERS_PASSWORD\n$KUSERS_PASSWORD\nquit"
    echo ""
    echo -e "$ktmerge" | kadmin -w $KADMIN_PASSWORD -p $KADMIN_PRINCIPAL_FULL
    echo ""
    kadmin.local ktadd -k "/etc/kerberos/keytabs/$pr.service.$node.keytab" "$pr/$node@$REALM"
  done
done

# Create unmerged keytab
for pr in "${PRINCIPALS[@]}"
do
  ktunmerged=""
  for node in "${SANDBOX_NODES[@]}"
  do
      ktunmerged+="rkt /etc/kerberos/keytabs/$pr.service.$node.keytab\n"
  done
  ktunmerged+="wkt /etc/kerberos/keytabs/$pr-unmerged.keytab\n"
  ktunmerged+="quit"
  echo ""
  echo -e "$ktunmerged" | ktutil
done

# Create merged keytab
UNMPRINC=("hdfs" "yarn" "mapred" "hive" "metastore" "spark" "hbase")
for upr in "${UNMPRINC[@]}"
do
  ktmerge=""
  ktmerge+="rkt /etc/kerberos/keytabs/$upr-unmerged.keytab\n"
  ktmerge+="rkt /etc/kerberos/keytabs/host-unmerged.keytab\n"
  ktmerge+="wkt /etc/kerberos/keytabs/$upr.service.keytab\n"
  ktmerge+="quit"
  echo ""
  echo -e "$ktmerge" | ktutil
done

# Setup spnego.service.keytab
cp /etc/kerberos/keytabs/host-unmerged.keytab /etc/kerberos/keytabs/host.service.keytab
cp /etc/kerberos/keytabs/HTTP-unmerged.keytab /etc/kerberos/keytabs/HTTP.service.keytab
cp /etc/kerberos/keytabs/HTTP-unmerged.keytab /etc/kerberos/keytabs/spnego.service.keytab
chmod -Rf 755 /etc/kerberos/keytabs/*

#
echo "Principles successfully generated."
echo ""