#
# docker build -t brijeshdhaker/hadoop-base:3.3.4 -f docker-hadoop-3.3.4/base/Dockerfile .
# docker push brijeshdhaker/hadoop-base:3.3.4
#

FROM ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    openjdk-8-jdk net-tools netcat curl wget unzip grep sed gnupg libsnappy-dev vim python3 python3-pip \
    && pip3 install --upgrade pip setuptools \
    && rm -rf /var/lib/apt/lists/* \
    # Removed the .cache to save space
    && rm -r /root/.cache && rm -rf /var/cache/apt/*

#
ENV HADOOP_VERSION  3.3.4
ENV BASE_DIR 		/opt
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64/
ENV HADOOP_HOME		$BASE_DIR/hadoop-$HADOOP_VERSION

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

ADD resources/tars/hadoop-3.3.4.tar.gz ${BASE_DIR}/

RUN ln -s ${HADOOP_HOME}/etc/hadoop /etc/hadoop

RUN mkdir ${HADOOP_HOME}/logs

RUN mkdir /hadoop-data

ENV HADOOP_CONF_DIR=/etc/hadoop
ENV MULTIHOMED_NETWORK=1
ENV USER=root
ENV PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

ADD docker-hadoop-3.3.4/base/entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
