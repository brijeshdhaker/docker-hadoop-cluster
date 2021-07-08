#
# docker build -t spark:3.1.2 -f Dockerfile .
#

ARG spark_uid=185
FROM python:3.9.5

####################
# SPARK 3.1.2
####################
ENV SPARK_VERSION   3.1.2
ENV BASE_DIR        /usr/local
ENV SPARK_HOME      /usr/local/spark
ENV JAVA_HOME       /usr/local/java

COPY requirements.pip /tmp/requirements.pip

RUN apt-get update && \
    pip3 install --upgrade pip setuptools && \
    pip3 install -U -r /tmp/requirements.pip && \
    # Removed the .cache to save space
    rm -r /root/.cache && rm -rf /var/cache/apt/*

ADD tar-balls/jdk-8u251-linux-x64.tar.gz $BASE_DIR
ADD tar-balls/spark-3.1.2-bin-hadoop3.2.tgz $BASE_DIR

RUN mv $BASE_DIR/spark-3.1.2-bin-hadoop3.2 $SPARK_HOME && \
    mv $BASE_DIR/jdk1.8.0_251 $JAVA_HOME && \
    mkdir -p $SPARK_HOME/work-dir && \
    mkdir -p ${SPARK_HOME}/python

#
COPY conf/log4j.properties $SPARK_HOME/conf/
COPY conf/spark.logserver.conf $SPARK_HOME/conf/
COPY conf/spark-defaults.conf  $SPARK_HOME/conf/
COPY conf/spark-env.sh $SPARK_HOME/conf/
COPY conf/core-site.xml $SPARK_HOME/conf/

#
COPY scripts/start-common.sh /
COPY scripts/entrypoint.sh /
COPY scripts/start-master /
COPY scripts/start-worker /
COPY scripts/start-logserver /

WORKDIR /opt/spark/work-dir

RUN chmod a+rwx $SPARK_HOME/*
RUN chmod a+rwx /start-common.sh
RUN chmod a+rwx /entrypoint.sh
RUN chmod a+rwx /start-master
RUN chmod a+rwx /start-worker
RUN chmod a+rwx /start-logserver


#ENTRYPOINT [ "/opt/entrypoint.sh" ]
ENTRYPOINT [ "/entrypoint.sh" ]

# Specify the User that the actual main process will run as
#USER ${spark_uid}
