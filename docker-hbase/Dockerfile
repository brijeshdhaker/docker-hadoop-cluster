#
# docker build -t brijeshdhaker/hbase:2.4.6 -f Dockerfile .
#

FROM brijeshdhaker/hadoop-base:3.2.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

####################
# HBASE
####################
ENV BASE_DIR 		/opt
ENV HBASE_VERSION	2.4.6
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64/
ENV HBASE_HOME 		$BASE_DIR/hbase-2.4.6
ENV HADOOP_HOME		$BASE_DIR/hadoop-3.2.1
ENV PATH		    $PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HBASE_HOME/bin

USER root

WORKDIR ${BASE_DIR}

ADD resources/tars/hbase-2.4.6-bin.tar.gz $BASE_DIR

RUN mkdir -p $HBASE_HOME/work-dir && \
	mkdir -p /tmp/hbase && \
	mkdir -p /tmp/hbase/zookeeper
#
ADD docker-hbase/conf/hbase-site.xml $HBASE_HOME/conf

# Helper script for starting hbasemaster
ADD docker-hbase/*.sh $BASE_DIR/hbase-2.4.6/
RUN chmod +x $BASE_DIR/hbase-2.4.6/*.sh

WORKDIR $HBASE_HOME

RUN chmod a+rwx $HBASE_HOME/*
RUN chmod -Rf 777 /tmp/hbase

# HBASE echo system Ports
# REST API
EXPOSE 8080
# REST Web UI at :8085/rest.jsp
EXPOSE 8085
# Thrift API
EXPOSE 9090
# Thrift Web UI at :9095/thrift.jsp
EXPOSE 9095
# HBase's Embedded zookeeper cluster
EXPOSE 10231
# HBase Master web UI at :16010/master-status;  ZK at :16010/zk.jsp
EXPOSE 16010

ENTRYPOINT ["/opt/hbase-2.4.6/start-hbasemaster.sh"]
#
#CMD ["/opt/hbase-2.4.6/start-hbaseserver.sh"]
# Specify the User that the actual main process will run as
#USER ${hive_uid}
