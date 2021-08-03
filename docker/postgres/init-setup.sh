#!/bin/bash

cd /

echo "CREATE DATABASE hive_store;" | psql -U postgres

echo "CREATE USER hive_admin WITH PASSWORD 'hive_admin';" | psql -U postgres

echo "GRANT ALL PRIVILEGES ON DATABASE hive_store TO hive_admin;" | psql -U postgres

