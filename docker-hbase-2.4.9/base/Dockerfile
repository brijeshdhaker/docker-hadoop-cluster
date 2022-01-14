#
# docker build -t brijeshdhaker/hbase-base:2.4.9 -f docker-hbase-2.4.9/base/Dockerfile .
#
FROM ubuntu:20.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    openjdk-8-jdk net-tools curl netcat gnupg libsnappy-dev vim \
    && rm -rf /var/lib/apt/lists/*


ENV BASE_DIR 		/opt
ENV HBASE_CONF_DIR  /etc/hbase
ENV USER            root
ENV HBASE_VERSION   2.4.9
ENV HBASE_HOME 		$BASE_DIR/hbase-$HBASE_VERSION
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64/
ENV PATH            $JAVA_HOME/bin:$HBASE_HOME/bin/:$PATH

#
ADD resources/tars/hbase-$HBASE_VERSION-bin.tar.gz $BASE_DIR

#
RUN mkdir -p /hbase-data && mkdir -p /zookeeper-data && mkdir -p $HBASE_HOME/logs && \
    ln -s $HBASE_HOME/conf /etc/hbase

#
ADD docker-hbase-2.4.9/base/hbase-site.xml $HBASE_HOME/conf
ADD docker-hbase-2.4.9/base/entrypoint.sh /entrypoint.sh
ADD docker-hbase-2.4.9/base/lib/*.jar $HBASE_HOME/lib/
RUN chmod a+x /entrypoint.sh

WORKDIR ${HBASE_HOME}

ENTRYPOINT ["/entrypoint.sh"]