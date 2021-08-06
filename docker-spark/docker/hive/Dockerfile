#
# docker build -t hive:3.1.2 -f Dockerfile .
#

FROM hadoop-3.2.1

ARG hive_uid=185

####################
# HADOOP
####################

ENV HADOOP_VERSION	3.2.1
ENV HADOOP_HOME		/usr/local/hadoop
ENV HADOOP_OPTS		-Djava.library.path=/usr/local/hadoop/lib/native

####################
# HIVE
####################
ENV BASE_DIR 		/usr/local
ENV HIVE_HOME 		/usr/local/hive
ENV HIVE_VERSION	3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

#wget https://apache.osuosl.org/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz
ADD tar-balls/apache-hive-$HIVE_VERSION-bin.tar.gz $BASE_DIR

RUN mv $BASE_DIR/apache-hive-$HIVE_VERSION-bin $HIVE_HOME && \
	mkdir -p $HIVE_HOME/work-dir && \
	mkdir -p /tmp/hive_io && \
	mkdir -p /tmp/hive-scratch/operation_logs &&\
	mkdir -p /tmp/hive-scratch/query_logs && \
	rm $HIVE_HOME/lib/log4j-slf4j-impl-2.10.0.jar

#
COPY conf/hive-site.xml $HIVE_HOME/conf/

# Helper script for starting namenode
ADD start-hive.sh /usr/local/bin/start-hive.sh

WORKDIR $HIVE_HOME/work-dir

RUN chmod a+rwx $HIVE_HOME/*
RUN chmod -Rf 777 /tmp/hive-scratch

#ENTRYPOINT ["/usr/local/hive/bin/hiveserver2"]

# Specify the User that the actual main process will run as
#USER ${hive_uid}
