#
# docker build -t brijeshdhaker/hbase-base:2.4.6 -f docker-hbase/base/Dockerfile .
# docker push brijeshdhaker/hbase-base:2.4.6
#
FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
ENV HBASE_VERSION=${HBASE_VERSION:-"2.4.6"}
ENV HBASE_LOG_DIR=${HBASE_LOG_DIR:-"/apps/var/log/hbase"}
ENV BASE_DIR=/opt
ENV HBASE_HOME=$BASE_DIR/hbase
ENV HBASE_CONF_DIR=$HBASE_HOME/conf
ENV PATH=$HBASE_HOME/bin:$HBASE_HOME/bin/:$PATH

#
COPY resources/tars/hbase-2.4.6-bin.tar.gz $BASE_DIR/hbase-2.4.6-bin.tar.gz


RUN set -x && \
    mkdir -p  ${HBASE_HOME} && tar --strip-components=1 -zxvf  /opt/hbase-2.4.6-bin.tar.gz -C ${HBASE_HOME} && \
    mkdir -p $HBASE_LOG_DIR && chmod -Rf 777 $HBASE_LOG_DIR && chown -Rf hbase:hbase $HBASE_LOG_DIR && \
    rm -f /opt/hbase-2.4.6-bin.tar.gz && \
    rm -rf /var/lib/apt/lists/* && \
    mkdir -p /etc/hbase && ln -s $HBASE_HOME/conf /etc/hbase/conf && chmod -Rf 777 /etc/hbase/conf && \
    echo "done."

#
COPY docker-hbase/base/hbase-site.xml $HBASE_HOME/conf/
RUN chown -Rf hbase:hbase $HBASE_HOME/conf/hbase-site.xml && chmod -Rf 777 $HBASE_HOME/conf/hbase-site.xml
#
ADD docker-hbase/base/entrypoint.sh /entrypoint.sh
RUN chown -Rf hbase:hbase /entrypoint.sh && chmod -Rf 755 /entrypoint.sh

WORKDIR ${HBASE_HOME}

ENTRYPOINT ["/entrypoint.sh"]
