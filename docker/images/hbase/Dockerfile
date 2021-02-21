#
# docker build -t hbase:2.4.1 -f Dockerfile .
#

FROM openjdk:8-jre-slim

####################
# HBASE
####################
ENV BASE_DIR 		/usr/local
ENV HBASE_HOME 		/usr/local/hbase
ENV HBASE_VERSION	2.4.1
ENV PATH		$PATH:$HBASE_HOME/bin

ADD tar-balls/hbase-2.4.1-bin.tar.gz $BASE_DIR

RUN mv $BASE_DIR/hbase-2.4.1 $HBASE_HOME && \
	mkdir -p $HBASE_HOME/work-dir && \
	mkdir -p /tmp/hbase && \
	mkdir -p /tmp/hbase/zookeeper 

#
COPY conf/hbase-site.xml $HBASE_HOME/conf/hbase-site.xml

# Helper script for starting namenode
ADD start-hbase.sh /usr/local/bin/start-hbase.sh

WORKDIR $HBASE_HOME/work-dir

RUN chmod a+rwx $HBASE_HOME/*
RUN chmod -Rf 777 /tmp/hbase

#ENTRYPOINT ["/usr/local/bin/start-hbase.sh"]

# Specify the User that the actual main process will run as
#USER ${hive_uid}
