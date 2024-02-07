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

echo ""
echo "OS Users principles password successfully changed."
echo ""