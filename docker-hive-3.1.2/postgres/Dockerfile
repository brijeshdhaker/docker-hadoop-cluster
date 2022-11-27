#
# docker build -t brijeshdhaker/hive-postgresql:3.1.2 -f docker-hive-3.1.2/postgres/Dockerfile .
#

FROM postgres:9.5.3

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
COPY docker-hive-3.1.2/postgres/hive-schema-3.1.0.postgres.sql /hive/hive-schema-3.1.0.postgres.sql

#
COPY docker-hive-3.1.2/postgres/init-hive-db.sh /docker-entrypoint-initdb.d/init-user-db.sh

#