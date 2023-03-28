#
# docker build -t brijeshdhaker/hive-postgresql:3.1.2 -f docker-hive-3.1.2/postgres/Dockerfile .
#

FROM postgres:15.2

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
COPY docker-postgres/hive-schema-3.1.0.postgres.sql /docker-entrypoint-initdb.d/hive-schema-3.1.0.postgres.sql

#
COPY docker-postgres/init-hive-db.sh /docker-entrypoint-initdb.d/init-user-db.sh

#