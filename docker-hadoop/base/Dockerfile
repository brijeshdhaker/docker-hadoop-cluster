#
# docker build -t brijeshdhaker/hadoop-base:3.2.4 -f docker-hadoop/base/Dockerfile .
# docker push brijeshdhaker/hadoop-base:3.2.4
#

FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
ENV HADOOP_VERSION=3.2.4
ENV BASE_DIR=/opt
ENV HADOOP_HOME=$BASE_DIR/hadoop
ENV HADOOP_CONF_DIR=/etc/hadoop/conf
ENV PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin
ENV HADOOP_LOG_DIR=${HADOOP_LOG_DIR:-"/apps/var/log/hadoop"}

#
ADD resources/bin/jsvc /usr/bin/jsvc

#
COPY resources/tars/hadoop-3.2.4.tar.gz ${BASE_DIR}/hadoop-3.2.4.tar.gz
ADD docker-hadoop/base/entrypoint.sh /entrypoint.sh

RUN set -x && \
    mkdir -p  ${HADOOP_HOME} && tar --strip-components=1 -zxvf  ${BASE_DIR}/hadoop-3.2.4.tar.gz -C ${HADOOP_HOME} && \
    mkdir -p /etc/hadoop && ln -s ${HADOOP_HOME}/etc/hadoop $HADOOP_CONF_DIR && chmod -Rf 755 $HADOOP_CONF_DIR && \
    chown -Rf hdfs:hadoop ${HADOOP_HOME} && chmod -Rf 775 ${HADOOP_HOME} && \
    mkdir -p $HADOOP_LOG_DIR && chmod -Rf 775 $HADOOP_LOG_DIR && chown -Rf hdfs:hadoop $HADOOP_LOG_DIR && \
    rm -f ${BASE_DIR}/hadoop-3.2.4.tar.gz && \
    chmod -Rf 775 /entrypoint.sh && chmod -Rf 775 /usr/bin/jsvc && \
    echo "done...!!!"

#Custom configuration goes here
COPY conf/hadoop/conf/* ${HADOOP_HOME}/etc/hadoop
RUN chmod -Rf 755 $HADOOP_CONF_DIR

ENTRYPOINT ["/entrypoint.sh"]
