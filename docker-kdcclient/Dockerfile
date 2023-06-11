#
# docker build -t brijeshdhaker/kdcclient:22.04 -f docker-kdcclient/Dockerfile .
# docker push brijeshdhaker/kdcclient:22.04
#

FROM ubuntu:22.04

EXPOSE 749 88/udp

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends apt-transport-https locales krb5-user vim

RUN locale-gen "en_US.UTF-8"
RUN echo "LC_ALL=\"en_US.UTF-8\"" >> /etc/default/locale

#
ENV REALM ${REALM:-sandbox.net}
ENV SUPPORTED_ENCRYPTION_TYPES ${SUPPORTED_ENCRYPTION_TYPES:-aes256-cts-hmac-sha1-96:normal}
ENV KADMIN_PRINCIPAL ${KADMIN_PRINCIPAL:-kadmin/admin}
ENV KADMIN_PASSWORD ${KADMIN_PASSWORD:-kadmin}

COPY docker-kdcclient/init-script.sh docker-kdcclient/setup-client.sh /tmp/
RUN chmod g+x /tmp/setup-client.sh
RUN chmod g+x /tmp/init-script.sh

CMD /tmp/init-script.sh
