#
# docker build -t dhc/hadoop:3.2.1 -f Dockerfile .
#

FROM dhc/hadoop:3.2.1

#
ARG hive_uid=186

####################
# HIVE
####################
ENV BASE_DIR 		/opt
ENV HADOOP_HOME		$BASE_DIR/hadoop
ENV HIVE_HOME 		$BASE_DIR/hive
ENV HADOOP_VERSION	3.2.1
ENV HIVE_VERSION	3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

#wget https://apache.osuosl.org/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz
ADD docker/hive/tars/apache-hive-$HIVE_VERSION-bin.tar.gz $BASE_DIR

RUN mv $BASE_DIR/apache-hive-$HIVE_VERSION-bin $HIVE_HOME && \
	mkdir -p $HIVE_HOME/work-dir && \
	mkdir -p /tmp/hive_io && \
	mkdir -p /tmp/hive-scratch/operation_logs &&\
	mkdir -p /tmp/hive-scratch/query_logs && \
	rm $HIVE_HOME/lib/log4j-slf4j-impl-2.10.0.jar $HIVE_HOME/lib/guava*.jar

#
COPY docker/hive/conf/hive-site.xml $HIVE_HOME/conf/
COPY docker/hive/libs/*.jar $HIVE_HOME/lib/

# Helper script for starting namenode
ADD docker/hive/start-hive.sh /usr/local/bin/start-hive.sh

WORKDIR $HIVE_HOME/work-dir

RUN chmod a+rwx $HIVE_HOME/*
RUN chmod -Rf 777 /tmp/hive-scratch

# Specify the User that the actual main process will run as
USER ${hive_uid}

ENTRYPOINT ["/start-hive.sh"]
#


