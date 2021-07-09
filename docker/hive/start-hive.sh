#!/bin/bash

if [ ! -f /tmp/hive-scratch ]; then
  echo "Creating warehouse  and strach dir for hive"
  mkdir -p /user/hive/warehouse
  mkdir -p /tmp/hive-scratch/operation_logs
fi

## Create Hive Metastore Derby Database
if [ ! -d /user/hive/metastore/metastore_db ]; then
  
  echo "Create Metastore Derby Database for Hive ."
  ${HIVE_HOME}/bin/schematool -initSchema -dbType derby
  
fi

${HIVE_HOME}/bin/hiveserver2