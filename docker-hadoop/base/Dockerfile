#
# docker build -t brijeshdhaker/hadoop-base:3.1.1 -f docker-hadoop/base/Dockerfile .
# docker push brijeshdhaker/hadoop-base:3.1.1
#

FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
ENV HADOOP_VERSION=3.1.1
ENV BASE_DIR=/opt
ENV HADOOP_HOME=$BASE_DIR/hadoop
ENV HADOOP_CONF_DIR=$BASE_DIR/hadoop/etc/hadoop
ENV HADOOP_ETC_CONF=/etc/conf/hadoop
ENV PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin
ENV HADOOP_LOG_DIR=${HADOOP_LOG_DIR:-"/apps/var/log/hadoop"}

#
ADD resources/bin/jsvc /usr/bin/jsvc

#
COPY resources/tars/hadoop-${HADOOP_VERSION}.tar.gz ${BASE_DIR}/hadoop-${HADOOP_VERSION}.tar.gz
ADD docker-hadoop/base/entrypoint.sh /entrypoint.sh

RUN set -x && \
    mkdir -p  ${HADOOP_HOME} && mkdir -p  ${HADOOP_ETC_CONF} && \
    tar --strip-components=1 -zxvf  ${BASE_DIR}/hadoop-${HADOOP_VERSION}.tar.gz -C ${HADOOP_HOME} && \
    chown -Rf hdfs:hadoop ${HADOOP_HOME} && chmod -Rf 775 ${HADOOP_HOME} && \
    mkdir -p $HADOOP_LOG_DIR && chmod -Rf 775 $HADOOP_LOG_DIR && chown -Rf hdfs:hadoop $HADOOP_LOG_DIR && \
    rm -f ${BASE_DIR}/hadoop-${HADOOP_VERSION}.tar.gz && \
    chmod -Rf 775 /entrypoint.sh && chmod -Rf 775 /usr/bin/jsvc && \
    echo "done...!!!"

#Custom configuration goes here
COPY conf/hadoop/conf/* ${HADOOP_ETC_CONF}/
RUN chmod -Rf 777 ${HADOOP_ETC_CONF}

ENTRYPOINT ["/entrypoint.sh"]
