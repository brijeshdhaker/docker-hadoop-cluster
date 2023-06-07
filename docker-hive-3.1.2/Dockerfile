#
# docker build -t brijeshdhaker/hive:3.1.2 -f docker-hive-3.1.2/Dockerfile .
# docker push brijeshdhaker/hive:3.1.2

FROM ubuntu:20.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# Allow buildtime config of HIVE_VERSION
ARG hive_gid=1500
ARG hive_uid=1500
ARG HIVE_VERSION

#
ENV HIVE_VERSION=${HIVE_VERSION:-3.1.2}
ENV BASE_DIR=/opt
ENV HADOOP_HOME=$BASE_DIR/hadoop
ENV HIVE_HOME=$BASE_DIR/hive
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

USER root

# COPY resources/tars/apache-hive-3.1.2-bin.tar.gz /opt/apache-hive-3.1.2-bin.tar.gz
ADD resources/tars/apache-hive-3.1.2-bin.tar.gz ${BASE_DIR}/

RUN mv ${BASE_DIR}/apache-hive-3.1.2-bin ${HIVE_HOME}

#	wget https://archive.apache.org/dist/hive/hive-$HIVE_VERSION/apache-hive-$HIVE_VERSION-bin.tar.gz && \
#	tar -xzvf apache-hive-$HIVE_VERSION-bin.tar.gz && \

#Install Hive and PostgreSQL JDBC
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends openjdk-8-jdk net-tools tar curl netcat gnupg libsnappy-dev vim && \
    groupadd --system -g ${hive_gid} hive && useradd -m -u ${hive_uid} -g hive hive && \
	# tar --strip-components=1 -zxvf  /opt/apache-hive-3.1.2-bin.tar.gz -C ${HIVE_HOME} && \
    rm -f ${HIVE_HOME}/lib/log4j-slf4j-impl-2.10.0.jar ${HIVE_HOME}/lib/guava*.jar && \
    # rm -f /opt/apache-hive-3.1.2-bin.tar.gz && \
    rm -rf /var/lib/apt/lists/*


# Add Drivers & Other Libs
COPY resources/libs/mysql-connector-java-8.0.23.jar $HIVE_HOME/lib/
COPY resources/libs/postgresql-42.2.23.jar $HIVE_HOME/lib/
COPY resources/libs/guava-27.0-jre.jar $HIVE_HOME/lib/

#Spark should be compiled with Hive to be able to use it
#hive-site.xml should be copied to $SPARK_HOME/conf folder

#Custom configuration goes here
COPY docker-hive-3.1.2/conf $HIVE_HOME/conf
COPY docker-hbase-2.4.9/client/hbase-site.xml $HIVE_HOME/conf/

#
COPY docker-hive-3.1.2/startup.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/startup.sh

COPY docker-hive-3.1.2/entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

#
VOLUME $BASE_DIR/hadoop

#
WORKDIR ${HIVE_HOME}

#
ENTRYPOINT ["entrypoint.sh"]

#
CMD startup.sh

# Specify the User that the actual main process will run as
# USER ${hive_uid}:${hive_gid}
