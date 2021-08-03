#!/bin/bash

cd /

#
echo "CREATE DATABASE hive_store;" | psql -U postgres
echo "hive store successfully created."
#
echo "CREATE USER hive_admin WITH PASSWORD 'hive_admin';" | psql -U postgres
echo "hive store admin user successfully created."
#
echo "GRANT ALL PRIVILEGES ON DATABASE hive_store TO hive_admin;" | psql -U postgres
echo "hive store role successfully granted."

