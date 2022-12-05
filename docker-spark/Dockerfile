#
# docker build -t brijeshdhaker/spark:3.3.1 -f docker-spark/Dockerfile .
# docker push brijeshdhaker/spark:3.3.1
#

# builder step used to download and configure spark environment
FROM openjdk:11.0.11-jre-slim-buster as builder

# Add Dependencies for PySpark
RUN apt-get update && apt-get install -y curl netcat vim wget software-properties-common ssh net-tools ca-certificates python3 python3-pip python3-numpy python3-matplotlib python3-scipy python3-pandas python3-simpy

RUN update-alternatives --install "/usr/bin/python" "python" "$(which python3)" 1

# Fix the value of PYTHONHASHSEED
# Note: this is needed when you use Python 3.3 or greater
ENV SPARK_VERSION=3.3.1 \
HADOOP_VERSION=3.3.4 \
SPARK_HOME=/opt/spark-3.3.1 \
HADOOP_HOME=/opt/hadoop-3.3.4 \
PYTHONHASHSEED=1

# Download and uncompress spark from the apache archive
RUN wget --no-verbose -O apache-spark.tgz "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz" \
&& mkdir -p ${SPARK_HOME} \
&& tar -xf apache-spark.tgz -C ${SPARK_HOME} --strip-components=1 \
&& rm apache-spark.tgz


# Apache spark environment
FROM builder as apache-spark

WORKDIR ${SPARK_HOME}

ENV SPARK_MASTER_PORT=7077 \
SPARK_MASTER_WEBUI_PORT=8080 \
SPARK_LOG_DIR=${SPARK_HOME}/logs \
SPARK_MASTER_LOG=${SPARK_HOME}/logs/spark-master.out \
SPARK_WORKER_LOG=${SPARK_HOME}/logs/spark-worker.out \
SPARK_WORKER_WEBUI_PORT=8080 \
SPARK_WORKER_PORT=7000 \
SPARK_MASTER="spark://spark-master:7077" \
SPARK_WORKLOAD="master"

EXPOSE 8080 7077 6066

RUN mkdir -p $SPARK_LOG_DIR && \
touch $SPARK_MASTER_LOG && \
touch $SPARK_WORKER_LOG && \
ln -sf /dev/stdout $SPARK_MASTER_LOG && \
ln -sf /dev/stdout $SPARK_WORKER_LOG

# COPY docker-spark/base/scripts /
COPY docker-spark/conf $SPARK_HOME/conf/
COPY docker-spark/*.sh /

#Give permission to execute scripts
RUN cd / && chmod +x /*.sh

CMD ["/bin/bash", "/start-spark.sh"]

ENTRYPOINT ["/entrypoint.sh"]