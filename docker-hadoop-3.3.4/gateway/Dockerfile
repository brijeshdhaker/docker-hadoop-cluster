#
# docker build -t brijeshdhaker/edgenode:3.3.4 -f docker-hadoop-3.3.4/edgenode/Dockerfile .
# docker push brijeshdhaker/edgenode:3.3.4
#

FROM brijeshdhaker/ubuntu:22.04

#
MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends openssh-server \
    && mkdir /var/run/sshd \
    && sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd \
    && rm -rf /var/lib/apt/lists/*

RUN service ssh start

EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]
