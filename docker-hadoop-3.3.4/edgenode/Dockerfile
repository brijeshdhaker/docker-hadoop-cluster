#
# docker build -t brijeshdhaker/edgenode:3.3.4 -f docker-hadoop-3.3.4/edgenode/Dockerfile .
# docker push brijeshdhaker/edgenode:3.3.4
#

FROM ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    openjdk-8-jdk net-tools netcat curl wget unzip grep sed gnupg libsnappy-dev vim python3 python3-pip openssh-server \
    && pip3 install --upgrade pip setuptools \
    && mkdir /var/run/sshd \
    && echo 'root:Accoo7@k47' | chpasswd \
    && useradd -rm -d /home/brijeshdhaker -s /bin/bash -g root -G sudo -u 1000 brijeshdhaker \
    && echo 'brijeshdhaker:Accoo7@k47' | chpasswd \
    && usermod -aG sudo brijeshdhaker \
    && sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd \
    && rm -rf /var/lib/apt/lists/*


RUN service ssh start

EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]
