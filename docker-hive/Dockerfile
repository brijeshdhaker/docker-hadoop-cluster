#
# docker build -t brijeshdhaker/hive:3.1.3 -f docker-hive/Dockerfile .
# docker push brijeshdhaker/hive:3.1.3

FROM brijeshdhaker/ubuntu:20.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# Allow buildtime config of HIVE_VERSION
ARG HIVE_VERSION

#
ENV HIVE_VERSION=${HIVE_VERSION:-"3.1.3"}
ENV HIVE_LOG_DIR=${HIVE_LOG_DIR:-"/apps/var/log/hive"}
ENV BASE_DIR=/opt
ENV HADOOP_HOME=$BASE_DIR/hadoop
ENV HIVE_HOME=$BASE_DIR/hive
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

#	wget https://archive.apache.org/dist/hive/hive-$HIVE_VERSION/apache-hive-$HIVE_VERSION-bin.tar.gz && \
#	tar --strip-components=1 -xzvf apache-hive-$HIVE_VERSION-bin.tar.gz -C ${HIVE_HOME}

#ADD resources/tars/apache-hive-3.1.3-bin.tar.gz ${BASE_DIR}/

COPY resources/tars/apache-hive-3.1.3-bin.tar.gz ${BASE_DIR}/apache-hive-3.1.3-bin.tar.gz

# groupadd --system -g ${hive_gid} hive &&  usermod -aG hive hive
RUN set -x && \
    mkdir -p  ${HIVE_HOME} && tar --strip-components=1 -zxvf  ${BASE_DIR}/apache-hive-3.1.3-bin.tar.gz -C ${HIVE_HOME} && \
    rm -f ${HIVE_HOME}/lib/log4j-slf4j-impl-2.17.1.jar ${HIVE_HOME}/lib/guava*.jar && \
    mkdir -p ${HIVE_LOG_DIR} && chmod -Rf 777 ${HIVE_LOG_DIR} && chown -Rf hive:mapred ${HIVE_LOG_DIR} && \
    rm -f ${BASE_DIR}/apache-hive-3.1.3-bin.tar.gz && \
    rm -rf /var/lib/apt/lists/* && \
    echo "done."

# Add Drivers & Other Libs
COPY resources/libs/mysql-connector-java-8.0.23.jar $HIVE_HOME/lib/
COPY resources/libs/postgresql-42.2.23.jar $HIVE_HOME/lib/
COPY resources/libs/guava-27.0-jre.jar $HIVE_HOME/lib/

#Custom configuration goes here
COPY conf/hive/* $HIVE_HOME/conf
RUN chmod -Rf 777 $HIVE_HOME && chown -Rf hive:mapred $HIVE_HOME

#
COPY docker-hive/*.sh /usr/local/bin/
RUN chmod -Rf 755 /usr/local/bin/*.sh && chown -Rf hive:mapred /usr/local/bin/*.sh

#
WORKDIR ${HIVE_HOME}

#
ENTRYPOINT ["entrypoint.sh"]

# Specify the User that the actual main process will run as
USER hive:mapred

#
CMD /usr/local/bin/startup.sh
