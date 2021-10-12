#
# docker build -t brijeshdhaker/hbase-base:2.4.6 -f docker-hbase/base/Dockerfile .
#
FROM openjdk:8-jre-slim

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends net-tools curl netcat

ENV BASE_DIR 		/opt
ENV HBASE_VERSION   2.4.6
ENV HBASE_HOME 		$BASE_DIR/hbase-2.4.6

ADD resources/tars/hbase-$HBASE_VERSION-bin.tar.gz $BASE_DIR

RUN mkdir -p /tmp/hbase && \
	mkdir -p /tmp/zookeeper && \
    ln -s /opt/hbase-$HBASE_VERSION/conf /etc/hbase && \
    mkdir -p /opt/hbase-$HBASE_VERSION/logs && \
    mkdir -p /hadoop-data

#

ENV HBASE_PREFIX=/opt/hbase-$HBASE_VERSION
ENV HBASE_CONF_DIR=/etc/hbase

ENV USER=root
ENV PATH $HBASE_PREFIX/bin/:$PATH

ADD docker-hbase/base/entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]