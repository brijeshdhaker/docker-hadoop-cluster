#
# docker build -t brijeshdhaker/zookeeper:3.7.1 -f docker-zookeeper/Dockerfile .
# docker push brijeshdhaker/zookeeper:3.7.1
#

FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# Allow buildtime config of ZOO_VERSION
ARG ZOO_VERSION

#
ENV ZOO_VERSION=${ZOO_VERSION:-3.7.1}
ENV ZOO_LOG_DIR=${ZOO_LOG_DIR:-"/apps/var/log/zookeeper"}
ENV ZOO_DATA_DIR=${ZOO_DATA_DIR:-"/zookeeper/data"}
ENV ZOO_DATA_LOG_DIR=${ZOO_DATA_LOG_DIR:-"/zookeeper/log"}
ENV BASE_DIR=/opt
ENV ZOO_HOME=$BASE_DIR/zookeeper
ENV PATH=$PATH:$ZOO_HOME/bin:$ZOO_HOME/sbin

#
COPY resources/tars/apache-zookeeper-3.7.1-bin.tar.gz $BASE_DIR/apache-zookeeper-3.7.1-bin.tar.gz

RUN set -x && \
    mkdir -p  ${ZOO_HOME} && tar --strip-components=1 -zxvf  $BASE_DIR/apache-zookeeper-3.7.1-bin.tar.gz -C ${ZOO_HOME} && \
    mkdir -p $ZOO_LOG_DIR && chmod -Rf 775 $ZOO_LOG_DIR && chown -Rf zookeeper:zookeeper $ZOO_LOG_DIR && \
    mkdir -p $ZOO_DATA_DIR && chmod -Rf 775 $ZOO_DATA_DIR && chown -Rf zookeeper:zookeeper $ZOO_DATA_DIR && \
    mkdir -p $ZOO_DATA_LOG_DIR && chmod -Rf 775 $ZOO_DATA_LOG_DIR && chown -Rf zookeeper:zookeeper $ZOO_DATA_LOG_DIR && \
    rm -f $BASE_DIR/apache-zookeeper-3.7.1-bin.tar.gz && \
    rm -rf /var/lib/apt/lists/* && \
    echo "done."

#Custom configuration goes here
RUN chown -Rf 775 $ZOO_HOME && chown -Rf zookeeper:zookeeper $ZOO_HOME

#
COPY docker-zookeeper/*.sh /usr/local/bin/
RUN chmod -Rf 775 /usr/local/bin/*.sh && chown -Rf zookeeper:zookeeper /usr/local/bin/*.sh

#
WORKDIR ${ZOO_HOME}

#
ENTRYPOINT ["/entrypoint.sh"]

# Specify the User that the actual main process will run as
USER zookeeper:zookeeper

#
CMD /usr/local/bin/startup.sh
