#
#
#

FROM minio/mc:latest
RUN microdnf install wget
#
COPY docker-minio/mc/setup.sh /setup.sh
COPY docker-minio/mc/users.parquet /users.parquet
RUN chmod a+x /setup.sh
