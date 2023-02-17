#
# Spark Image for Kubernetes
#
# cd $SPARK_HOME
# docker build -t brijeshdhaker/spark:3.1.2 -f kubernetes/dockerfiles/spark/Dockerfile .
# docker push brijeshdhaker/spark:3.1.2
#

#
# docker build -t brijeshdhaker/spark:3.1.2-standalone -f docker-spark/Dockerfile .
# docker push docker.io/brijeshdhaker/spark:3.1.2-standalone
#

FROM brijeshdhaker/spark:3.1.2

ARG spark_uid=185
ARG root_uid=0

USER ${root_uid}

#
# Fix the value of PYTHONHASHSEED
# Note: this is needed when you use Python 3.3 or greater
#
ENV PYTHONHASHSEED=1 \
SPARK_WORKLOAD="master"

# Add Dependencies for PySpark
RUN apt-get update && \
    apt-get install -y curl netcat vim wget ssh net-tools &&  \
#    apt-get install -y software-properties-common ca-certificates &&  \
#    apt-get install -y python3 python3-pip python3-numpy python3-matplotlib python3-scipy python3-pandas && \
#    update-alternatives --install "/usr/bin/python" "python" "$(which python3)" 1  && \
    mkdir -p ${SPARK_HOME}/logs && chmod -Rf 777 ${SPARK_HOME}/logs && \
    useradd -g ${root_uid} -u ${spark_uid} spark

#
# Download and uncompress spark from the apache archive
#
# RUN wget --no-verbose -O apache-spark.tgz "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz" \
# && mkdir -p ${SPARK_HOME} \
# && tar -xf apache-spark.tgz -C ${SPARK_HOME} --strip-components=1 \
# && rm apache-spark.tgz

WORKDIR ${SPARK_HOME}

# COPY docker-spark/base/scripts /
# COPY docker-spark/conf $SPARK_HOME/conf/
COPY docker-spark/*.sh /opt/

#Give permission to execute scripts
RUN cd / && chmod +x /opt/*.sh

CMD ["/bin/bash", "/opt/start-spark.sh"]

ENTRYPOINT ["/opt/entrypoint.sh"]