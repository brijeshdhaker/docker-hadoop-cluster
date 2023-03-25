#
# docker build -t brijeshdhaker/hadoop-historyserver:3.3.4 -f docker-hadoop-3.3.4/historyserver/Dockerfile .
# docker push brijeshdhaker/hadoop-historyserver:3.3.4
#

FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:19888/ || exit 1

ENV YARN_CONF_yarn_timeline___service_leveldb___timeline___store_path=/yarn/history
RUN mkdir -p /yarn/history
VOLUME /yarn

ADD docker-hadoop-3.3.4/historyserver/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 19888

CMD ["/run.sh"]
