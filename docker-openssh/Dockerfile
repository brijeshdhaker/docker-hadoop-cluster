#
# docker build -t brijeshdhaker/ubuntu-openssh:22.04 -f docker-openssh/Dockerfile .
# docker push brijeshdhaker/ubuntu-openssh:22.04
#

FROM brijeshdhaker/ubuntu:22.04

#
MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y openssh-server \
    && mkdir /var/run/sshd \
    && echo 'root:Accoo7@k47' | chpasswd \
    && usermod -aG sudo brijeshdhaker \
    && echo 'brijeshdhaker:Accoo7@k47' | chpasswd \
    && sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd \
    && rm -rf /var/lib/apt/lists/*

RUN service ssh start

EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]

