#
# docker build -t brijeshdhaker/hive:3.1.2 -f docker-hive-3.1.2/hive/Dockerfile .
#

FROM brijeshdhaker/hadoop-base:3.2.1
#
ARG hive_uid=1500

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# Allow buildtime config of HIVE_VERSION
ARG HIVE_VERSION
# Set HIVE_VERSION from arg if provided at build, env if provided at run, or default
# https://docs.docker.com/engine/reference/builder/#using-arg-variables
# https://docs.docker.com/engine/reference/builder/#environment-replacement

ENV HIVE_VERSION=${HIVE_VERSION:-3.1.2}
ENV BASE_DIR 		/opt
ENV HADOOP_HOME     $BASE_DIR/hadoop-3.2.1
ENV HIVE_HOME       $BASE_DIR/hive-3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

USER root

WORKDIR ${HIVE_HOME}

COPY resources/tars/apache-hive-3.1.2-bin.tar.gz /opt/apache-hive-3.1.2-bin.tar.gz

#	wget https://archive.apache.org/dist/hive/hive-$HIVE_VERSION/apache-hive-$HIVE_VERSION-bin.tar.gz && \
#	tar -xzvf apache-hive-$HIVE_VERSION-bin.tar.gz && \

#Install Hive and PostgreSQL JDBC
RUN apt-get update && apt-get install -y wget procps && \
    useradd -u 1500 hive && \
	tar --strip-components=1 -zxvf  /opt/apache-hive-3.1.2-bin.tar.gz -C ${HIVE_HOME} && \
	rm ${HIVE_HOME}/lib/log4j-slf4j-impl-2.10.0.jar ${HIVE_HOME}/lib/guava*.jar && \
	apt-get --purge remove -y wget && \
	apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    rm /opt/apache-hive-3.1.2-bin.tar.gz

COPY resources/libs/postgresql-42.2.23.jar $HIVE_HOME/lib/
COPY resources/libs/guava-27.0-jre.jar $HIVE_HOME/lib/

#Spark should be compiled with Hive to be able to use it
#hive-site.xml should be copied to $SPARK_HOME/conf folder

#Custom configuration goes here
COPY docker-hive-3.1.2/hive/conf $HIVE_HOME/conf
COPY docker-hbase-2.4.6/client/hbase-site.xml $HIVE_HOME/conf/
COPY docker-hadoop-3.2.1/client/*.xml $HADOOP_HOME/etc/hadoop/

#
COPY docker-hive-3.1.2/hive/startup.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/startup.sh

COPY docker-hive-3.1.2/hive/entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

#
ENTRYPOINT ["entrypoint.sh"]

#
CMD startup.sh

# Specify the User that the actual main process will run as
#USER ${hive_uid}
