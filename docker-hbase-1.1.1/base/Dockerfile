#
# docker build -t brijeshdhaker/hbase-base:1.1.1 -f docker-hbase-1.1.1/base/Dockerfile .
#
FROM openjdk:8-jre-slim

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends net-tools curl netcat

ENV BASE_DIR 		/opt
ENV HBASE_CONF_DIR  /etc/hbase
ENV USER            root
ENV HBASE_VERSION   1.1.1
ENV HBASE_HOME 		$BASE_DIR/hbase-$HBASE_VERSION
ENV PATH            $HBASE_HOME/bin/:$PATH

#
ADD resources/tars/hbase-$HBASE_VERSION-bin.tar.gz $BASE_DIR

#
RUN mkdir -p /hbase-data && mkdir -p /zookeeper-data && mkdir -p $HBASE_HOME/logs && \
    ln -s $HBASE_HOME/conf /etc/hbase

#
ADD docker-hbase-1.1.1/base/hbase-site.xml $HBASE_HOME/conf

ADD docker-hbase-1.1.1/base/entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

WORKDIR ${HBASE_HOME}

ENTRYPOINT ["/entrypoint.sh"]