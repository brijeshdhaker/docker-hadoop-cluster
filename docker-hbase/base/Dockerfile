#
# docker build -t brijeshdhaker/hbase-base:2.4.6 -f docker-hbase/base/Dockerfile .
#
FROM openjdk:8-jre-slim

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends net-tools curl netcat

ENV BASE_DIR 		/opt
ENV HBASE_CONF_DIR  /etc/hbase
ENV USER            root
ENV HBASE_VERSION   2.4.6
ENV HBASE_HOME 		$BASE_DIR/hbase-$HBASE_VERSION
ENV PATH            $HBASE_HOME/bin/:$PATH

#
ADD resources/tars/hbase-$HBASE_VERSION-bin.tar.gz $BASE_DIR

#
RUN mkdir -p /hbase-data && mkdir -p /zookeeper-data && mkdir -p $HBASE_HOME/logs && \
    ln -s $HBASE_HOME/conf /etc/hbase

#
ADD docker-hbase/base/hbase-site.xml $HBASE_HOME/conf

ADD docker-hbase/base/entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

WORKDIR ${HBASE_HOME}

ENTRYPOINT ["/entrypoint.sh"]