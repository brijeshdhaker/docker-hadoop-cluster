#
# docker build -t brijeshdhaker/kdcserver:22.04 -f docker-kdcserver/Dockerfile .
# docker push brijeshdhaker/kdcserver:22.04
#

FROM ubuntu:22.04

#
ENV REALM ${REALM:-SANDBOX.NET}
ENV DOMAIN_REALM ${DOMAIN_REALM:-sandbox.net}
ENV MASTER_KEY_TYPE ${MASTER_KEY_TYPE:-aes256-cts-hmac-sha1-96:normal}
ENV SUPPORTED_ENCRYPTION_TYPES ${SUPPORTED_ENCRYPTION_TYPES:-aes256-cts-hmac-sha1-96:normal aes128-cts-hmac-sha1-96:normal}
ENV ROOT_PRINCIPAL ${ROOT_PRINCIPAL:-root/admin}
ENV KADMIN_PRINCIPAL ${KADMIN_PRINCIPAL:-kadmin/admin}
ENV KADMIN_PASSWORD ${KADMIN_PASSWORD:-kadmin}
ENV KUSERS_PASSWORD ${KUSERS_PASSWORD:-kuser}

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends locales krb5-kdc krb5-admin-server vim

EXPOSE 749 88/udp

COPY docker-kdcserver/init-script.sh /tmp/
RUN chmod g+x /tmp/init-script.sh

CMD /tmp/init-script.sh

