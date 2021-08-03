#
# docker build -t hadoop-3.2.1:latest -f Dockerfile .
#


FROM openjdk:8-jre-slim

####################
# HADOOP 3.2.1
####################
ENV HADOOP_VERSION	3.2.1
ENV JAVA_HOME       /usr/local/openjdk-8
ENV HADOOP_HOME		/opt/hadoop
ENV HADOOP_OPTS		-Djava.library.path=/opt/hadoop/lib/native
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

#
ADD docker/hadoop/tar-balls/hadoop-3.2.1.tar.gz /opt

#
RUN apt-get update && \
	DEBIAN_FRONTEND=noninteractive apt-get install -y wget libssl-dev && \
	mv /opt/hadoop-$HADOOP_VERSION ${HADOOP_HOME} && \
    mkdir -p $HADOOP_HOME/logs && mkdir -p /tmp/work && \
    ln -s $JAVA_HOME/bin/java /bin/java && \
    rm -f /opt/hadoop*.gz  && \
    rm -rf /var/lib/apt/lists/*

# Overwrite default HADOOP configuration files with our config files
COPY docker/hadoop/conf/*.xml  $HADOOP_HOME/etc/hadoop/

#
COPY docker/hadoop/hadoop-env.sh  $HADOOP_HOME/etc/hadoop/

#
COPY docker/hadoop/hadoop-functions.sh  $HADOOP_HOME/libexec/

# Helper script for starting namenode
COPY docker/hadoop/start-*.sh /

# Formatting HDFS
RUN mkdir -p /data && \
	chmod +x /start-namenode.sh && \
	chmod +x /start-hdfs.sh && \
    chmod +x /start-yarn-historyserver.sh

#




