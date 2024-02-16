#!/bin/bash
#
#
KADMIN_PRINCIPAL_FULL=kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD=kadmin
KUSERS_PASSWORD=kuser
#
#
OS_USERS=("root" "brijeshdhaker" "hdfs" "yarn" "mapred" "hive" "spark")
# Change Passwords for OS Users
for ou in "${OS_USERS[@]}"
do
  ktmerge=""
  ktmerge+="change_password $ou\n$KUSERS_PASSWORD\n$KUSERS_PASSWORD\nquit"
  echo ""
  echo -e "$ktmerge" | kadmin -w $KADMIN_PASSWORD -p $KADMIN_PRINCIPAL_FULL
done

#for var in one two three; do echo "$var"; done
PRINCIPALS=("hdfs" "yarn" "mapred" "hive" "metastore" "host" "HTTP")
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
  "gateway.sandbox.net"
  "thinkpad.sandbox.net"
  "hostmaster.sandbox.net"
  "dockerhost.sandbox.net"
)
# Change Passwords for OS Users
for pr in "${PRINCIPALS[@]}"
do
  for node in "${SANDBOX_NODES[@]}"
  do
    ktmerge=""
    ktmerge+="change_password $pr/$node@$REALM\n$KUSERS_PASSWORD\n$KUSERS_PASSWORD\nquit"
    echo ""
    echo -e "$ktmerge" | kadmin -w $KADMIN_PASSWORD -p $KADMIN_PRINCIPAL_FULL
  done
done
echo ""
echo "cluster principles password successfully generated."
echo ""