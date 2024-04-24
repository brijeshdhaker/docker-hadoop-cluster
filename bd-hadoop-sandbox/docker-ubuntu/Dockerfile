#
# docker build -t brijeshdhaker/ubuntu:22.04 -f docker-ubuntu/Dockerfile .
# docker push brijeshdhaker/ubuntu:22.04
#

FROM ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# GID
ARG hadoop_gid=1001
ARG hdfs_gid=1002
ARG mapred_gid=1003
ARG hive_gid=1004
ARG hbase_gid=1005
ARG zookeeper_gid=1006
ARG zeppelin_gid=1007
ARG spark_gid=1008
ARG yarn_gid=1009
ARG httpfs_gid=1010
ARG flink_gid=1011

# Install conda to manage python and R packages
ARG miniconda_version="py37_4.9.2"
# Hashes via https://docs.conda.io/en/latest/miniconda_hashes.html
ARG miniconda_sha256="79510c6e7bd9e012856e25dcb21b3e093aa4ac8113d9aa7e82a86987eabe1c31"

#
ENV MULTIHOMED_NETWORK=2
ENV USER=root
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8

COPY docker-ubuntu/env_pyspark37.yml /env_pyspark37.yml
COPY conf/kerberos/krb5.conf /etc/krb5.conf

RUN set -x && \
    echo "deb http://security.ubuntu.com/ubuntu focal-security main" | tee /etc/apt/sources.list.d/focal-security.list && \
    apt-get update && DEBIAN_FRONTEND=noninteractive && \
    apt-get install -y --no-install-recommends sudo vim curl wget unzip tar grep sed gnupg net-tools netcat libsnappy-dev libsasl2-modules-gssapi-mit kafkacat && \
    apt-get install -y gcc libc6 glibc-source && \
    apt-get install -y libssl1.1 && dpkg -L libssl1.1 && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y openssh-server && \
    apt-get install -y apt-transport-https locales krb5-user ldap-utils && \
    apt-get install -y python3 python3-pip && pip3 install --upgrade pip setuptools && \
    locale-gen "en_US.UTF-8" && echo "LC_ALL=\"en_US.UTF-8\"" >> /etc/default/locale && \
    rm -rf /var/lib/apt/lists/* && rm -rf /var/cache/apt/* && \
    echo "package installation done."

#
RUN set -x && \
    echo 'root:Accoo7@k47' | chpasswd && \
    groupadd -f -r -g ${hadoop_gid} hadoop && \
    groupadd -f -r -g ${hdfs_gid} hdfs && \
    groupadd -f -r -g ${mapred_gid} mapred && \
    groupadd -f -r -g ${hive_gid} hive && \
    groupadd -f -r -g ${hbase_gid} hbase && \
    groupadd -f -r -g ${zookeeper_gid} zookeeper && \
    groupadd -f -r -g ${zeppelin_gid} zeppelin && \
    groupadd -f -r -g ${spark_gid} spark && \
    groupadd -f -r -g ${yarn_gid} yarn && \
    groupadd -f -r -g ${httpfs_gid} httpfs && \
    useradd -m -s /bin/bash -G sudo,hadoop brijeshdhaker && echo 'brijeshdhaker:Accoo7@k47' | chpasswd && mkdir -p /home/brijeshdhaker/.ssh && \
    useradd -m -s /bin/bash -g ${hadoop_gid} -u ${hadoop_gid} -G hadoop hadoop && \
    useradd -m -s /bin/bash -g ${hdfs_gid} -u ${hdfs_gid} -G hdfs,hadoop hdfs && \
    useradd -m -s /bin/bash -g ${httpfs_gid} -u ${httpfs_gid} -G httpfs,hadoop httpfs && \
    useradd -m -s /bin/bash -g ${yarn_gid} -u ${yarn_gid} -G yarn,hadoop yarn && \
    useradd -m -s /bin/bash -g ${mapred_gid} -u ${mapred_gid} -G mapred,hadoop mapred && \
    useradd -m -s /bin/bash -g ${hive_gid} -u ${hive_gid} -G hive hive && \
    useradd -m -s /bin/bash -g ${hbase_gid} -u ${hbase_gid} -G hbase hbase && \
    useradd -m -s /bin/bash -g ${spark_gid} -u ${spark_gid} -G spark spark && \
    useradd -m -s /bin/bash -g ${zookeeper_gid} -u ${zookeeper_gid} -G zookeeper zookeeper && \
    useradd -m -s /bin/bash -g ${zeppelin_gid} -u ${zeppelin_gid} -G zeppelin zeppelin && \
    echo "Users added successfully. "

# Copy the ssh public key in the authorized_keys file.
# The idkey.pub below is a public key file you get from ssh-keygen. They are under ~/.ssh directory by default.
ADD docker-ubuntu/user-keys /home/brijeshdhaker/.ssh/
COPY docker-ubuntu/user-keys/id_rsa.pub /home/brijeshdhaker/.ssh/authorized_keys

# CMD ["/usr/sbin/sshd", "-D"]
RUN set -x && \
    chown -Rf brijeshdhaker:brijeshdhaker /home/brijeshdhaker/.ssh && chmod 600 /home/brijeshdhaker/.ssh/authorized_keys && \
    mkdir /var/run/sshd && \
    sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd && \
    rm -rf /var/lib/apt/lists/* && \
    echo "SSH Server added successfully. "

#
RUN service ssh start
EXPOSE 22

#
ADD docker-ubuntu/entrypoint.sh /entrypoint.sh
RUN chmod g+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
