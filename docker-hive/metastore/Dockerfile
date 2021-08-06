#
# docker build -t bde2020/hive-metastore-postgresql:3.1.2 -f Dockerfile .
#

FROM postgres:9.5.3

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
COPY docker-hive/metastore/hive-schema-3.1.0.postgres.sql /hive/hive-schema-3.1.0.postgres.sql

#
COPY docker-hive/metastore/init-hive-db.sh /docker-entrypoint-initdb.d/init-user-db.sh

#

