#!/bin/bash
set -e

if [ ! -f /var/lib/postgresql/data/.already_setup ]; then

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL

  CREATE USER hive WITH PASSWORD 'hive';
  CREATE DATABASE metastore;
  GRANT ALL PRIVILEGES ON DATABASE metastore TO hive;

  CREATE USER docker WITH PASSWORD 'dockeradmin';
  CREATE DATABASE dockerdb;
  GRANT ALL PRIVILEGES ON DATABASE dockerdb TO docker;

  CREATE USER dbadmin WITH PASSWORD 'dbadmin';
  CREATE DATABASE dbstore;
  GRANT ALL PRIVILEGES ON DATABASE dbstore TO dbadmin;

  \c metastore

  \i /hive/hive-schema-3.1.0.postgres.sql

  \pset tuples_only

  \o /tmp/grant-privs

  SELECT 'GRANT SELECT,INSERT,UPDATE,DELETE ON "' || schemaname || '"."' || tablename || '" TO hive ;'
  FROM pg_tables
  WHERE tableowner = CURRENT_USER and schemaname = 'public';

  SELECT 'GRANT SELECT,INSERT,UPDATE,DELETE ON "' || schemaname || '"."' || tablename || '" TO dbadmin ;'
  FROM pg_tables
  WHERE tableowner = CURRENT_USER and schemaname = 'public';


  SELECT 'GRANT SELECT,INSERT,UPDATE,DELETE ON "' || schemaname || '"."' || tablename || '" TO docker ;'
  FROM pg_tables
  WHERE tableowner = CURRENT_USER and schemaname = 'public';

  \o

  \i /tmp/grant-privs

EOSQL

  touch /var/lib/postgresql/data/.already_setup

fi



