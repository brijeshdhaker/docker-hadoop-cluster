#
# docker build -t brijeshdhaker/hadoop-nodemanager:3.1.1 -f docker-hadoop/nodemanager/Dockerfile .
# docker push brijeshdhaker/hadoop-nodemanager:3.1.1
#

FROM brijeshdhaker/hadoop-base:3.1.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8042/ || exit 1

ENV HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop

ADD docker-hadoop/nodemanager/container-executor.cfg ${HADOOP_CONF_DIR}/container-executor.cfg
ADD docker-hadoop/nodemanager/conf/* ${HADOOP_CONF_DIR}/
#
RUN set -x && \
    chown -Rf root:hadoop ${HADOOP_HOME} && chmod -Rf 755 ${HADOOP_HOME} && \
    chown -Rf root:hadoop ${HADOOP_HOME}/etc && chmod -Rf 755 ${HADOOP_HOME}/etc && \
    chown -Rf root:hadoop ${HADOOP_HOME}/bin/container-executor && chmod 6050 ${HADOOP_HOME}/bin/container-executor && \
    chown -Rf root:hadoop ${HADOOP_CONF_DIR}/container-executor.cfg && chmod 0400 ${HADOOP_CONF_DIR}/container-executor.cfg && \
    echo "done."

ADD docker-hadoop/nodemanager/run.sh /run.sh
RUN chown yarn:hadoop /run.sh && chmod 775 /run.sh

EXPOSE 8042

# Specify the User that the actual main process will run as
USER yarn:hadoop

CMD ["/run.sh"]
