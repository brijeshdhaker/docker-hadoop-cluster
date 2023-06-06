#
# docker build -t brijeshdhaker/ubuntu-openssh:22.04 -f docker-openssh/Dockerfile .
# docker push brijeshdhaker/ubuntu-openssh:22.04
#

FROM ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-8-jdk net-tools curl netcat gnupg libsnappy-dev vim openssh-server \
    && mkdir /var/run/sshd \
    && echo 'root:Accoo7@k47' | chpasswd \
    && useradd -rm -d /home/ubuntu -s /bin/bash -g root -G sudo -u 1000 brijeshdhaker \
    && usermod -aG sudo brijeshdhaker \
    && echo 'brijeshdhaker:Accoo7@k47' | chpasswd \
    && sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd \
    && rm -rf /var/lib/apt/lists/*

RUN service ssh start

EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]

