#!/bin/bash
# This script would delete an existing running container and re-create it
set -x

if [ $# -eq 0 ]; then
 echo "Missing stack name ..."
 exit 1
fi
sandboxStack=$1
registry="hortonworks"

if [ "X${sandboxStack}" = "Xhdp" ]; then

   hostname="sandbox-hdp.hortonworks.com"
   sandboxName=$(docker ps | awk ' { print $NF }' | grep sandbox-hdp)
   [ -z "$sandboxName" ] && sandboxName="sandbox-hdp"
   sandboxVersion=$(docker ps | grep $sandboxName | awk -F" " ' { print $2 }' | cut -d":" -f2)
   [ -z "$sandboxVersion" ] && sandboxVersion="2.6.5"
   docker rm -f $sandboxName
   [ $? -eq 0 ] && echo "Deleted container $sandboxName ..."
   docker run --privileged --name $sandboxName -h $hostname --network=cda --network-alias=$hostname -d "$registry/$sandboxName:$sandboxVersion"
   [ $? -eq 0 ] && echo "Started $sandboxName ..."

elif [ "X${sandboxStack}" = "Xhdf" ]; then

   hostname="sandbox-hdf.hortonworks.com"
   sandboxName=$(docker ps | awk ' { print $NF }' | grep sandbox-hdf)
   [ -z "$sandboxName" ] && sandboxName="sandbox-hdf"
   sandboxVersion=$(docker ps | grep $sandboxName | awk -F" " ' { print $2 }' | cut -d":" -f2)
   [ -z "$sandboxVersion" ] && sandboxVersion="3.3.4"
   docker rm -f $sandboxName
   [ $? -eq 0 ] && echo "Deleted container $sandboxName ..."
   docker run --privileged --name $sandboxName -h $hostname --network=cda --network-alias=$hostname -d "$registry/$sandboxName:$sandboxVersion"
   [ $? -eq 0 ] && echo "Started $sandboxName ..."

fi
