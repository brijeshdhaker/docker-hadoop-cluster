#
# docker build -t brijeshdhaker/ubuntu:22.04 -f docker-ubuntu/Dockerfile .
# docker push brijeshdhaker/ubuntu:22.04
#

FROM ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

# GID
ARG hadoop_gid=10001
ARG hdfs_gid=10002
ARG mapred_gid=10003
ARG bduser_gid=10004

#
ENV MULTIHOMED_NETWORK=2
ENV USER=root
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
ENV PATH=$PATH:${JAVA_HOME}/bin

RUN set -x && apt-get update && DEBIAN_FRONTEND=noninteractive && \
    apt-get install -y --no-install-recommends sudo vim curl wget unzip tar grep sed gnupg net-tools netcat libsnappy-dev && \
    apt-get install -y gcc && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y openssh-server && \
    apt-get install -y apt-transport-https locales krb5-user && \
    apt-get install -y python3 python3-pip && \
    pip3 install --upgrade pip setuptools && \
    rm -rf /var/lib/apt/lists/* && \
    rm -r /root/.cache && rm -rf /var/cache/apt/*

#
RUN locale-gen "en_US.UTF-8"
RUN echo "LC_ALL=\"en_US.UTF-8\"" >> /etc/default/locale

#
RUN set -x && \
    echo 'root:Accoo7@k47' | chpasswd && \
    groupadd -f -r -g ${hadoop_gid} hadoop && \
    groupadd -f -r -g ${hdfs_gid} hdfs && \
    groupadd -f -r -g ${mapred_gid} mapred && \
    groupadd -f -r -g ${bduser_gid} bduser && \
    useradd -m -s /bin/bash brijeshdhaker && usermod -aG sudo brijeshdhaker && \
    echo 'brijeshdhaker:Accoo7@k47' | chpasswd && mkdir -p /home/brijeshdhaker/.ssh && \
    useradd -m -s /bin/bash -g ${hadoop_gid} -G users,sudo,hadoop hdfs && \
    useradd -m -s /bin/bash -g ${hadoop_gid} -G users,sudo,hadoop yarn && \
    useradd -m -s /bin/bash -g ${hadoop_gid} -G users,sudo,hadoop mapred && \
    useradd -m -s /bin/bash -g ${mapred_gid} -G users,sudo,mapred hive && \
    echo "Users added successfully. "


#
COPY docker-ubuntu/krb5.conf /etc/krb5.conf

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


RUN service ssh start
EXPOSE 22

#
ADD docker-ubuntu/entrypoint.sh /entrypoint.sh
RUN chmod g+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
