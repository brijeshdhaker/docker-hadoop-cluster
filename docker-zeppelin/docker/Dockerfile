# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM ubuntu:20.04

LABEL maintainer="Apache Software Foundation <dev@zeppelin.apache.org>"

ENV Z_VERSION="0.10.0"
ENV BASE_DIR="/opt"
ENV HADOOP_HOME		$BASE_DIR/hadoop-3.2.1
ENV SPARK_HOME 	    $BASE_DIR/spark-3.1.2
ENV ZEPPELIN_HOME   $BASE_DIR/zeppelin-0.10.0
#ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-arm64
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64
ENV LOG_TAG="[ZEPPELIN_${Z_VERSION}]:" \
    HOME="/opt/zeppelin" \
    LANG=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8 \
    ZEPPELIN_ADDR="0.0.0.0"
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$SPARK_HOME/bin:$SPARK_HOME/sbin

#
RUN echo "$LOG_TAG install basic packages" && \
    apt-get -y update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y curl wget unzip grep sed vim net-tools curl netcat locales language-pack-en tini openjdk-8-jre-headless && \
    # Cleanup
    rm -rf /var/lib/apt/lists/* && \
    apt-get autoclean && \
    apt-get clean

# Install conda to manage python and R packages
ARG miniconda_version="py37_4.9.2"

# Hashes via https://docs.conda.io/en/latest/miniconda_hashes.html
ARG miniconda_sha256="79510c6e7bd9e012856e25dcb21b3e093aa4ac8113d9aa7e82a86987eabe1c31"

# Install python and R packages via conda
COPY setup/venv_zeppelin.yml /venv_zeppelin.yml

RUN set -ex && \
    wget -nv https://repo.anaconda.com/miniconda/Miniconda3-${miniconda_version}-Linux-x86_64.sh -O miniconda.sh && \
    echo "${miniconda_sha256} miniconda.sh" > anaconda.sha256 && \
    sha256sum --strict -c anaconda.sha256 && \
    bash miniconda.sh -b -p /opt/conda && \
    export PATH=/opt/conda/bin:$PATH && \
    conda config --set always_yes yes --set changeps1 no && \
    conda info -a && \
    conda install mamba -c conda-forge && \
    mamba env update -f /venv_zeppelin.yml --prune && \
    # Cleanup
    rm -v miniconda.sh anaconda.sha256  && \
    # Cleanup based on https://github.com/ContinuumIO/docker-images/commit/cac3352bf21a26fa0b97925b578fb24a0fe8c383
    find /opt/conda/ -follow -type f -name '*.a' -delete && \
    find /opt/conda/ -follow -type f -name '*.js.map' -delete && \
    mamba clean -ay
    # Allow to modify conda packages. This allows malicious code to be injected into other interpreter sessions, therefore it is disabled by default
    # chmod -R ug+rwX /opt/conda
ENV PATH /opt/conda/envs/venv_zeppelin/bin:/opt/conda/bin:$PATH

#
COPY resources/tars/zeppelin-0.10.0.tar.gz /opt/zeppelin-0.10.0.tar.gz
COPY resources/tars/hadoop-3.2.1.tar.gz /opt/hadoop-3.2.1.tar.gz
COPY resources/tars/spark-3.1.2-bin-hadoop3.2.tgz /opt/spark-3.1.2-bin-hadoop3.2.tgz

#
RUN echo "$LOG_TAG Download Zeppelin binary" && \
    mkdir -p ${ZEPPELIN_HOME} ${HADOOP_HOME} ${SPARK_HOME} && \
    tar --strip-components=1 -zxvf  /opt/zeppelin-0.10.0.tar.gz -C ${ZEPPELIN_HOME} && \
    tar --strip-components=1 -zxvf  /opt/hadoop-3.2.1.tar.gz -C ${HADOOP_HOME} && \
    tar --strip-components=1 -zxvf  /opt/spark-3.1.2-bin-hadoop3.2.tgz -C ${SPARK_HOME} && \
    rm ${SPARK_HOME}/jars/guava-14.0.1.jar && \
    mkdir -p ${ZEPPELIN_HOME}/logs ${ZEPPELIN_HOME}/run ${ZEPPELIN_HOME}/webapps ${ZEPPELIN_HOME}/notebook ${ZEPPELIN_HOME}/conf && \
    echo "1. dirs created ." && \
    chown -R root:root ${ZEPPELIN_HOME} && \
    echo "2. permission changed ." && \
    # Allow process to edit /etc/passwd, to create a user entry for zeppelin
    chgrp root /etc/passwd && chmod ug+rw /etc/passwd && \
    echo "3. group changed ." && \
    # Give access to some specific folders
    echo "4. Give access to some specific folders ." && \
    # Allow process to create new folders (e.g. webapps)
    chmod -R 775 ${ZEPPELIN_HOME} && \
    echo "5. Allow process to create new folders ." && \
    # chmod -R 775 /opt/conda && \
    echo "6. Allow process to create new folders ." && \
    rm /opt/zeppelin-0.10.0.tar.gz /opt/hadoop-3.2.1.tar.gz /opt/spark-3.1.2-bin-hadoop3.2.tgz

#
COPY docker-zeppelin/conf ${ZEPPELIN_HOME}/conf

#
COPY resources/libs/guava-27.0-jre.jar $SPARK_HOME/jars/
COPY resources/libs/aws-java-sdk-bundle-1.11.888.jar $SPARK_HOME/jars/
COPY resources/libs/hadoop-aws-3.2.0.jar $SPARK_HOME/jars/
COPY resources/libs/postgresql-42.2.23.jar $SPARK_HOME/jars/

# Overwrite default HADOOP configuration files with our config files
COPY docker-zeppelin/hadoop-conf/*.xml  $HADOOP_HOME/etc/hadoop/

#
USER 1000

#
EXPOSE 9090

#
ENTRYPOINT [ "/usr/bin/tini", "--" ]

#
WORKDIR ${ZEPPELIN_HOME}

CMD ["bin/zeppelin.sh"]