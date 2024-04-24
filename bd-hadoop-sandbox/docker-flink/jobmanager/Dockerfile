#
#
#

FROM brijeshdhaker/flink-base:1.17.2

LABEL MAINTAINER="Brijesh K Dhaker <brijeshdhaker@gmail.com>"

#config files (template)
#ADD $FLINK_HOME/conf/flink-conf.yaml $FLINK_HOME/conf/

COPY master.sh /
RUN set -x && chown -Rf flink:root /master.sh && chmod -Rf 775 /master.sh

ENV FLINK_MASTER_LOG $FLINK_HOME/log

EXPOSE 6123 22 8080

# Specify the User that the actual main process will run as
USER flink:root

CMD ["/bin/bash", "/master.sh"]
