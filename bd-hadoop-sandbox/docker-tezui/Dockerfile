#
# docker build -t brijeshdhaker/tezui:0.9.1 -f docker-tezui/Dockerfile .
# docker push brijeshdhaker/tezui:0.9.1

FROM brijeshdhaker/ubuntu:22.04

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

ENV BASE_DIR=/opt
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
ENV PATH=$PATH:$JAVA_HOME/bin

USER root

#Install Hive and PostgreSQL JDBC
RUN set -x && \
    mkdir -p /opt/tez && \
    rm -rf /var/lib/apt/lists/*

#
COPY conf/kerberos/krb5.conf /etc/krb5.conf

# Add Drivers & Other Libs
COPY resources/libs/jetty-runner-9.2.11.v20150529.jar $BASE_DIR/tez/
COPY resources/tars/tez-ui-0.9.1.war $BASE_DIR/tez/
RUN chmod +x $BASE_DIR/tez/*.*

#
COPY docker-tezui/startup.sh /usr/local/bin/
COPY docker-tezui/entrypoint.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/*.sh
#
WORKDIR $BASE_DIR/tez/
#
ENTRYPOINT ["entrypoint.sh"]
#
CMD startup.sh

