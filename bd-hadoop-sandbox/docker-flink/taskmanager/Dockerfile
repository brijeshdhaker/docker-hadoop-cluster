FROM brijeshdhaker/flink-base:1.17.2

LABEL MAINTAINER="Brijesh K Dhaker <brijeshdhaker@gmail.com>"

ADD worker.sh /
RUN set -x && chown -Rf flink:root /worker.sh && chmod -Rf 775 /worker.sh

EXPOSE 8081

# Specify the User that the actual main process will run as
USER flink:root

CMD ["/bin/bash", "/worker.sh"]
