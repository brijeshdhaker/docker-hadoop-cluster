#
# docker build -t brijeshdhaker/hive:2.3.9 -f docker-hive-2.3.9/hive/Dockerfile .
#

FROM brijeshdhaker/hadoop-base:2.10.1
#
ARG hive_uid=1500

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# Allow buildtime config of HIVE_VERSION
ARG HIVE_VERSION
# Set HIVE_VERSION from arg if provided at build, env if provided at run, or default
# https://docs.docker.com/engine/reference/builder/#using-arg-variables
# https://docs.docker.com/engine/reference/builder/#environment-replacement

ENV HIVE_VERSION=${HIVE_VERSION:-2.3.9}
ENV BASE_DIR 		/opt
ENV HADOOP_HOME     $BASE_DIR/hadoop-2.10.1
ENV HIVE_HOME       $BASE_DIR/hive-2.3.9
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

USER root

WORKDIR ${HIVE_HOME}

COPY resources/tars/apache-hive-2.3.9-bin.tar.gz /opt/apache-hive-2.3.9-bin.tar.gz

#	wget https://archive.apache.org/dist/hive/hive-$HIVE_VERSION/apache-hive-$HIVE_VERSION-bin.tar.gz && \
#

#Install Hive and PostgreSQL JDBC
RUN apt-get update && apt-get install -y wget procps && \
    useradd -u 1500 hive && \
    tar --strip-components=1 -zxvf  /opt/apache-hive-2.3.9-bin.tar.gz -C ${HIVE_HOME} && \
    rm ${HIVE_HOME}/lib/log4j-slf4j-impl-2.6.2.jar && \
    apt-get --purge remove -y wget && \
	apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    rm /opt/apache-hive-2.3.9-bin.tar.gz

COPY resources/libs/postgresql-42.2.23.jar $HIVE_HOME/lib/

#Spark should be compiled with Hive to be able to use it
#hive-site.xml should be copied to $SPARK_HOME/conf folder

#Custom configuration goes here
COPY docker-hive-2.3.9/hive/conf $HIVE_HOME/conf
COPY docker-hbase-2.4.9/client/hbase-site.xml $HIVE_HOME/conf/
COPY docker-hadoop-2.10.1/client/*.xml $HADOOP_HOME/etc/hadoop/

#
COPY docker-hive-2.3.9/hive/startup.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/startup.sh

COPY docker-hive-2.3.9/hive/entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

#
ENTRYPOINT ["entrypoint.sh"]

#
CMD startup.sh

# Specify the User that the actual main process will run as
#USER ${hive_uid}
