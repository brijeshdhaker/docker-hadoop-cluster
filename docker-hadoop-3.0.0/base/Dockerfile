#
# docker build -t brijeshdhaker/hadoop-base:3.0.0 -f docker-hadoop-3.0.0/base/Dockerfile .
#

FROM ubuntu:20.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    openjdk-8-jdk net-tools curl netcat gnupg libsnappy-dev vim \
    && rm -rf /var/lib/apt/lists/*

#
ENV HADOOP_VERSION  3.0.0
ENV BASE_DIR 		/opt
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64/
ENV HADOOP_HOME		$BASE_DIR/hadoop-3.0.0

# RUN curl -O https://dist.apache.org/repos/dist/release/hadoop/common/KEYS
# RUN gpg --import KEYS
#ENV HADOOP_URL https://www.apache.org/dist/hadoop/common/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz

#RUN set -x \
#    && curl -fSL "$HADOOP_URL" -o /tmp/hadoop.tar.gz \
#    && curl -fSL "$HADOOP_URL.asc" -o /tmp/hadoop.tar.gz.asc \
#    && gpg --verify /tmp/hadoop.tar.gz.asc \
#    && tar -xvf /tmp/hadoop.tar.gz -C /opt/ \
#    && rm /tmp/hadoop.tar.gz*
#

ADD resources/tars/hadoop-3.0.0.tar.gz ${BASE_DIR}/

RUN ln -s ${BASE_DIR}/hadoop-$HADOOP_VERSION/etc/hadoop /etc/hadoop

RUN mkdir ${BASE_DIR}/hadoop-$HADOOP_VERSION/logs

RUN mkdir /hadoop-data

ENV HADOOP_CONF_DIR=/etc/hadoop
ENV MULTIHOMED_NETWORK=1
ENV USER=root
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

ADD docker-hadoop-3.0.0/base/entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
