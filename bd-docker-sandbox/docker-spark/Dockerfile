#
# Spark Image for Kubernetes
#
# cd $SPARK_HOME
# docker build -t apache/spark:3.5.1 -f kubernetes/dockerfiles/spark/Dockerfile .
# docker push apache/spark:3.5.1
#

#
# docker build -t brijeshdhaker/spark-standalon:3.5.2 -f docker-spark/Dockerfile .
# docker push docker.io/brijeshdhaker/spark-standalon:3.5.1
#

FROM brijeshdhaker/ubuntu:22.04

# Note: this is needed when you use Python 3.3 or greater
ENV PYTHONHASHSEED=1
ENV SPARK_VERSION=3.5.2-bin-hadoop3
ENV BASE_DIR=/opt
ENV SPARK_HOME=$BASE_DIR/spark
ENV SPARK_CONF_DIR=$SPARK_HOME/conf
ENV PATH=$PATH:${SPARK_HOME}/bin:${SPARK_HOME}/sbin


USER root
#
COPY resources/tars/spark-${SPARK_VERSION}.tgz /opt/spark-${SPARK_VERSION}.tgz

# Add Dependencies for PySpark
RUN set -x && \
    mkdir -p  ${SPARK_HOME} && tar --strip-components=1 -zxvf  /opt/spark-${SPARK_VERSION}.tgz -C ${SPARK_HOME} && \
    chown -Rf spark:spark ${SPARK_HOME} && chmod -Rf 755 ${SPARK_HOME} && \
#    ln -s ${SPARK_HOME}/conf /etc/spark/conf && chmod -Rf 755 /etc/spark/conf && \
#    apt-get install -y software-properties-common ca-certificates &&  \
#    apt-get install -y python3 python3-pip python3-numpy python3-matplotlib python3-scipy python3-pandas && \
#    update-alternatives --install "/usr/bin/python" "python" "$(which python3)" 1  && \
    rm -f /opt/spark-${SPARK_VERSION}.tgz && \
    echo "done."

#
# Download and uncompress spark from the apache archive
#
# RUN wget --no-verbose -O /opt/apache-spark.tgz "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz" \
# && mkdir -p ${SPARK_HOME} \
# && tar -xf /opt/apache-spark.tgz -C ${SPARK_HOME} --strip-components=1 \
# && rm /opt/apache-spark.tgz
WORKDIR ${SPARK_HOME}

#
COPY docker-spark/*.sh /opt/
COPY resources/libs/*.jar $SPARK_HOME/jars/
RUN cd / && chown -Rf spark:spark /opt/*.sh && chmod -Rf 755 /opt/*.sh

ENTRYPOINT ["/opt/entrypoint.sh"]

# Specify the User that the actual main process will run as
USER spark:spark

CMD ["/bin/bash", "/opt/start-spark.sh"]

