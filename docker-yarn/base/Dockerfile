#
# docker build -t brijeshdhaker/yarn-base:3.2.1 -f docker-yarn/base/Dockerfile .
# docker push brijeshdhaker/yarn-base:3.2.1
#

FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
ENV HADOOP_VERSION=3.2.1
ENV BASE_DIR=/opt
ENV YARN_HOME=$BASE_DIR/hadoop
ENV YARN_SITE_DIR=/etc/yarn/conf
ENV PATH=$PATH:${YARN_HOME}/bin:${YARN_HOME}/sbin
ENV YARN_LOG_DIR=${YARN_LOG_DIR:-"/apps/var/log/yarn"}
ENV MAPRED_LOG_DIR=${MAPRED_LOG_DIR:-"/apps/var/log/mapred"}

#
COPY resources/tars/hadoop-3.2.1.tar.gz ${BASE_DIR}/hadoop-3.2.1.tar.gz
ADD docker-yarn/base/entrypoint.sh /entrypoint.sh

RUN set -x && \
    mkdir -p  ${YARN_HOME} && tar --strip-components=1 -zxvf  ${BASE_DIR}/hadoop-3.2.1.tar.gz -C ${YARN_HOME} && \
    mkdir -p /etc/yarn && ln -s ${YARN_HOME}/etc/hadoop /etc/yarn/conf && chmod -Rf 755 /etc/yarn/conf && \
    mkdir -p $YARN_LOG_DIR && chmod -Rf 775 $YARN_LOG_DIR && chown -Rf yarn:hadoop $YARN_LOG_DIR && \
    mkdir -p $MAPRED_LOG_DIR && chmod -Rf 775 $MAPRED_LOG_DIR && chown -Rf mapred:hadoop $MAPRED_LOG_DIR && \
    rm -f ${BASE_DIR}/hadoop-3.2.1.tar.gz && \
    chmod -Rf 775 /entrypoint.sh && \
    echo "done...!!!"

#Custom configuration goes here
COPY conf/hadoop/conf/* ${YARN_HOME}/etc/hadoop
RUN chmod -Rf 755 ${YARN_HOME}/etc/hadoop

ENTRYPOINT ["/entrypoint.sh"]
