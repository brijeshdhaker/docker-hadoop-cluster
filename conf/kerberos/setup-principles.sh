#!/bin/bash

# delete existing keytab files
rm -Rf /etc/kerberos/keytabs/*.keytab

echo "Adding OS User Principal"
echo ""
OS_USERS=("root" "brijeshdhaker" "hdfs" "yarn" "mapred" "hive" "spark")
for ou in "${OS_USERS[@]}"
do
  rm -Rf "/etc/kerberos/keytabs/$ou.keytab"
  echo "Add kerberos principal $ou start ."
  kadmin -p kadmin/admin@SANDBOX.NET -w kadmin
  kadmin.local -q "delete_principal -force $ou@$REALM"
  kadmin.local -q "addprinc -pw $ou $ou@$REALM"
  kadmin.local ktadd -k "/etc/kerberos/keytabs/$ou.keytab" "$ou@$REALM"
done

echo "Adding service principal for all hosts."
echo ""
#for var in one two three; do echo "$var"; done
PRINCIPALS=("hdfs" "yarn" "mapred" "host" "HTTP")
SANDBOX_NODES=("namenode.sandbox.net" "datanode.sandbox.net" "secondary.sandbox.net" "resourcemanager.sandbox.net" "nodemanager.sandbox.net" "historyserver.sandbox.net" "timelineserver.sandbox.net" "gateway.sandbox.net")
for pr in "${PRINCIPALS[@]}"
do
  echo ""
  for node in "${SANDBOX_NODES[@]}"
  do
      rm -Rf "/etc/kerberos/keytabs/$pr.service.$node.keytab"
      kadmin.local -q "delete_principal -force $pr/$node@$REALM"
      kadmin.local -q "addprinc -pw $pr $pr/$node@$REALM"
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
UNMPRINC=("hdfs" "yarn" "mapred")
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
chmod -Rf 775 /etc/kerberos/keytabs/*

touch /var/lib/krb5kdc/.already_setup
echo "Principles successfully generated."
echo ""