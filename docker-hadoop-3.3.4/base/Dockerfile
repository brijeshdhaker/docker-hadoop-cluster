#
# docker build -t brijeshdhaker/hadoop-base:3.3.4 -f docker-hadoop-3.3.4/base/Dockerfile .
# docker push brijeshdhaker/hadoop-base:3.3.4
#

FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
ENV HADOOP_VERSION=3.3.4
ENV BASE_DIR=/opt
ENV HADOOP_HOME=$BASE_DIR/hadoop
ENV HADOOP_CONF_DIR=/etc/hadoop
ENV PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin
ENV HADOOP_LOG_DIR=${HADOOP_LOG_DIR:-"/var/log/hadoop"}
ENV YARN_LOG_DIR=${YARN_LOG_DIR:-"/var/log/yarn"}


# RUN curl -O https://dist.apache.org/repos/dist/release/hadoop/common/KEYS
# RUN gpg --import KEYS
#ENV HADOOP_URL https://www.apache.org/dist/hadoop/common/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz

#RUN set -x \
#    && curl -fSL "$HADOOP_URL" -o /tmp/hadoop.tar.gz \
#    && curl -fSL "$HADOOP_URL.asc" -o /tmp/hadoop.tar.gz.asc \
#    && gpg --verify /tmp/hadoop.tar.gz.asc \
#    && tar -xvf /tmp/hadoop.tar.gz -C /opt/ \
#    && rm /tmp/hadoop.tar.gz*
#

#
ADD resources/bin/jsvc /usr/bin/jsvc

#
ADD resources/tars/hadoop-3.3.4.tar.gz ${BASE_DIR}/
RUN mv -f ${BASE_DIR}/hadoop-3.3.4 ${BASE_DIR}/hadoop

ADD docker-hadoop-3.3.4/base/entrypoint.sh /entrypoint.sh

RUN set -x && \
    ln -s ${HADOOP_HOME}/etc/hadoop /etc/hadoop && chmod -Rf 777 /etc/hadoop && \
    chown -Rf hdfs:hadoop ${HADOOP_HOME} && chmod -Rf 777 ${HADOOP_HOME} && \
    mkdir -p $HADOOP_LOG_DIR && chmod -Rf 777 $HADOOP_LOG_DIR && chown -Rf hdfs:hadoop $HADOOP_LOG_DIR && \
    mkdir -p $YARN_LOG_DIR && chmod -Rf 777 $YARN_LOG_DIR && chown -Rf hdfs:hadoop $YARN_LOG_DIR && \
    chmod -Rf 777 /entrypoint.sh && chmod -Rf 777 /usr/bin/jsvc

ENTRYPOINT ["/entrypoint.sh"]
