#
# docker build -t big-data/hive-base:3.2.1 -f Dockerfile .
#

FROM big-data/hadoop-base:3.2.1
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
ENV SPARK_HOME 	    $BASE_DIR/spark-3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$SPARK_HOME/bin:$SPARK_HOME/sbin:$HIVE_HOME/bin

USER root

WORKDIR ${BASE_DIR}

ADD resources/tars/apache-hive-${HIVE_VERSION}-bin.tar.gz /opt

#	wget https://archive.apache.org/dist/hive/hive-$HIVE_VERSION/apache-hive-$HIVE_VERSION-bin.tar.gz && \
#	tar -xzvf apache-hive-$HIVE_VERSION-bin.tar.gz && \

#Install Hive and PostgreSQL JDBC
RUN apt-get update && apt-get install -y wget procps && \
    useradd -u 1500 hive && \
	mv apache-hive-$HIVE_VERSION-bin ${HIVE_HOME} && \
	rm ${HIVE_HOME}/lib/log4j-slf4j-impl-2.10.0.jar ${HIVE_HOME}/lib/guava*.jar && \
	apt-get --purge remove -y wget && \
	apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY resources/libs/postgresql-42.2.23.jar $HIVE_HOME/lib/
COPY resources/libs/guava-27.0-jre.jar $HIVE_HOME/lib/

#Spark should be compiled with Hive to be able to use it
#hive-site.xml should be copied to $SPARK_HOME/conf folder

#Custom configuration goes here
ADD docker-hive/hive/conf/hive-site.xml $HIVE_HOME/conf
ADD docker-hive/hive/conf/beeline-log4j2.properties $HIVE_HOME/conf
ADD docker-hive/hive/conf/hive-env.sh $HIVE_HOME/conf
ADD docker-hive/hive/conf/hive-exec-log4j2.properties $HIVE_HOME/conf
ADD docker-hive/hive/conf/hive-log4j2.properties $HIVE_HOME/conf
ADD docker-hive/hive/conf/ivysettings.xml $HIVE_HOME/conf
ADD docker-hive/hive/conf/llap-daemon-log4j2.properties $HIVE_HOME/conf

COPY docker-hive/hive/startup.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/startup.sh

COPY docker-hive/hive/entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

#
ENTRYPOINT ["entrypoint.sh"]

#
CMD startup.sh

# Specify the User that the actual main process will run as
#USER ${hive_uid}
