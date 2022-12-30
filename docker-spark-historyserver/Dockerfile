#
# docker build -t brijeshdhaker/spark-historyserver:3.1.2-k8s -f docker-spark-historyserver/Dockerfile .
# docker push brijeshdhaker/spark-historyserver:3.1.2-k8s
#

FROM brijeshdhaker/spark:3.1.2-k8s
ARG spark_uid=185
ARG root_uid=0

USER root
ENV SPARK_VERSION=3.1.2 \
PYTHONHASHSEED=1

# Add Dependencies for PySpark
RUN apt-get update && apt-get install -y curl netcat wget && \
    mkdir -p ${SPARK_HOME}/logs && \
    chmod 777 ${SPARK_HOME}/logs && \
    useradd -u ${spark_uid} spark && \
    rm -rf /var/cache/apt/*

WORKDIR ${SPARK_HOME}
EXPOSE 18080

VOLUME ${SPARK_HOME}/logs

# COPY docker-spark/base/scripts /
COPY docker-spark-historyserver/conf $SPARK_HOME/conf/
COPY docker-spark-historyserver/entrypoint.sh /opt/entrypoint.sh
RUN chmod +x /opt/entrypoint.sh

ENTRYPOINT ["/opt/entrypoint.sh"]

# Specify the User that the actual main process will run as
USER ${spark_uid}