#
# docker build -t brijeshdhaker/spark-historyserver:3.1.2-k8s -f docker-spark-historyserver/Dockerfile .
# docker push brijeshdhaker/spark-historyserver:3.1.2-k8s
#

FROM openjdk:11-jre-slim
ARG spark_uid=185
ARG root_uid=0

ENV SPARK_VERSION=3.1.2 \
SPARK_HOME=/opt/spark \
PYTHONHASHSEED=1

# Add Dependencies for PySpark
RUN apt-get update && apt-get install -y curl netcat wget && \
    useradd -u ${spark_uid} spark && \
    rm -rf /var/cache/apt/*

# Download and uncompress spark from the apache archive
RUN wget --no-verbose -O apache-spark.tgz "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.2.tgz" \
&& mkdir -p ${SPARK_HOME} \
&& tar -xf apache-spark.tgz -C ${SPARK_HOME} --strip-components=1 \
&& rm apache-spark.tgz

WORKDIR ${SPARK_HOME}
EXPOSE 18080

# COPY docker-spark/base/scripts /
COPY docker-spark-historyserver/conf $SPARK_HOME/conf/
COPY docker-spark-historyserver/entrypoint.sh /opt/entrypoint.sh
RUN chmod +x /opt/entrypoint.sh

ENTRYPOINT ["/opt/entrypoint.sh"]

# Specify the User that the actual main process will run as
USER ${spark_uid}