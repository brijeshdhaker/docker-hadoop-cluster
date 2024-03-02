#
# docker build -t brijeshdhaker/mc:2024.03.02 -f bd-hadoop-sandbox/docker-minio/mc/Dockerfile .
# docker push brijeshdhaker/mc:2024.03.02
#

FROM minio/mc:latest

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN set -x && \
    install wget && \
    echo "done...!!!"

#
COPY bd-hadoop-sandbox/docker-minio/mc/setup.sh /setup.sh
COPY bd-hadoop-sandbox/docker-minio/mc/users.parquet /users.parquet

RUN chmod a+x /setup.sh
