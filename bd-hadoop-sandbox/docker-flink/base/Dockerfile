FROM brijeshdhaker/ubuntu:22.04

LABEL MAINTAINER="Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# GID
ARG flink_gid=5001
ARG flink_uid=5001

#
ENV HADOOP_VERSION=3.3.4
ENV BASE_DIR=/opt
ENV HADOOP_HOME=$BASE_DIR/hadoop
ENV HADOOP_CONF_DIR=$BASE_DIR/hadoop/etc/hadoop
ENV PATH=$PATH:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin
ENV HADOOP_LOG_DIR=${HADOOP_LOG_DIR:-"/apps/var/log/hadoop"}

#
ENV FLINK_VERSION=1.17.2
ENV SCALA_VERSION=2.12

#Enable poc-init-daemon
ENV ENABLE_INIT_DAEMON false
ENV INIT_DAEMON_BASE_URI http://identifier/init-daemon
ENV INIT_DAEMON_STEP flink_master_init

COPY wait-for-step.sh /
COPY execute-step.sh /
COPY finish-step.sh /
#
COPY tars/flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz

#
RUN set -x && \
    groupadd -f -r -g ${flink_gid} flink && \
    useradd -m -s /bin/bash -g ${flink_gid} -u ${flink_uid} -G sudo flink && \
    echo "flink user added successfully. "

##Flink Installation
###Download:
RUN   set -x \
      && apt-get update \
      && apt-get install dnsutils openssh-client -y  \
      && chown -Rf flink:root *.sh && chmod +x *.sh \
      # && curl -fSL https://archive.apache.org/dist/flink/flink-${FLINK_VERSION}/flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz -o flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz\
      && tar -xvzf flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz \
      && rm flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz \
      && mv flink-${FLINK_VERSION} /opt/flink \
      && chown -Rf flink:root /opt/flink && chmod -Rf 775 /opt/flink \
      && echo "flink setup successfully done. "

ENV FLINK_HOME /opt/flink
ENV PATH $PATH:$FLINK_HOME/bin

# Add Drivers & Other Libs
COPY libs/*.jar $FLINK_HOME/lib/

#add passless key to ssh
#RUN ssh-keygen -f ~/.ssh/id_rsa -t rsa -N ''
#RUN cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && chmod 600 ~/.ssh/*

#
COPY entrypoint.sh /
RUN set -x && chown -Rf flink:root /entrypoint.sh && chmod -Rf 775 /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
