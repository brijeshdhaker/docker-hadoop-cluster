#!/bin/bash
set -e

if [ ! -f /apps/hostpath/postgres/hive-2.3.9/.already_setup ]; then

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL

  CREATE USER hive WITH PASSWORD 'hive';
  CREATE DATABASE metastore;
  GRANT ALL PRIVILEGES ON DATABASE metastore TO hive;

  CREATE USER pgadmin WITH PASSWORD 'pgadmin';
  CREATE DATABASE dbstore;
  GRANT ALL PRIVILEGES ON DATABASE dbstore TO pgadmin;

  \c metastore

  \i /hive/hive-schema-2.3.0.postgres.sql

  \pset tuples_only

  \o /tmp/grant-privs

SELECT 'GRANT SELECT,INSERT,UPDATE,DELETE ON "' || schemaname || '"."' || tablename || '" TO hive ;'
FROM pg_tables
WHERE tableowner = CURRENT_USER and schemaname = 'public';

  \o

  \i /tmp/grant-privs

EOSQL

  touch /apps/hostpath/postgres/hive-2.3.9/.already_setup

fi



