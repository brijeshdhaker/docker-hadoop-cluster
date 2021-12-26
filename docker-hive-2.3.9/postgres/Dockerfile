#
# docker build -t brijeshdhaker/hive-postgresql:3.1.2 -f docker-hive/postgres/Dockerfile .
#

FROM postgres:9.5.3

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

ENV PGDATA=/apps/hostpath/postgres/hive-2.3.9

#
COPY docker-hive-2.3.9/postgres/hive-schema-2.3.0.postgres.sql /hive/hive-schema-2.3.0.postgres.sql
COPY docker-hive-2.3.9/postgres/hive-txn-schema-2.3.0.postgres.sql /hive/hive-txn-schema-2.3.0.postgres.sql
#
COPY docker-hive-2.3.9/postgres/init-hive-db.sh /docker-entrypoint-initdb.d/init-user-db.sh

#

