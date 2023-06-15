#
# docker build -t brijeshdhaker/hbase-base:2.4.9 -f docker-hbase-2.4.9/base/Dockerfile .
# docker push brijeshdhaker/hbase-base:2.4.9
#
FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
ENV HBASE_VERSION=${HBASE_VERSION:-2.4.9}
ENV HBASE_LOG_DIR=${HBASE_LOG_DIR:-"/var/log/hbase"}
ENV BASE_DIR 		/opt
ENV HBASE_CONF_DIR  /etc/hbase
ENV HBASE_HOME 		$BASE_DIR/hbase
ENV PATH            $HBASE_HOME/bin:$HBASE_HOME/bin/:$PATH

#
COPY resources/tars/hbase-$HBASE_VERSION-bin.tar.gz $BASE_DIR/hbase-$HBASE_VERSION-bin.tar.gz


RUN set -x && \
    mkdir -p  ${HBASE_HOME} && tar --strip-components=1 -zxvf  /opt/hbase-$HBASE_VERSION-bin.tar.gz -C ${HBASE_HOME} && \
    mkdir -p $HBASE_LOG_DIR && chmod -Rf 777 $HBASE_LOG_DIR && chown -Rf hbase:hbase $HBASE_LOG_DIR && \
    rm -f /opt/hbase-$HBASE_VERSION-bin.tar.gz && \
    rm -rf /var/lib/apt/lists/* && \
    ln -s $HBASE_HOME/conf /etc/hbase && \
    echo "done." \

#
ADD docker-hbase-2.4.9/base/hbase-site.xml $HBASE_HOME/conf

#
ADD docker-hbase-2.4.9/base/entrypoint.sh /entrypoint.sh
RUN chown -Rf hbase:hbase /entrypoint.sh && chmod -Rf 755 /entrypoint.sh

WORKDIR ${HBASE_HOME}

ENTRYPOINT ["/entrypoint.sh"]
