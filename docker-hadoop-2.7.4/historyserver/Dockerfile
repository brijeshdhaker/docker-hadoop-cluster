#
# docker build -t brijeshdhaker/hadoop-historyserver:2.7.4 -f docker-hadoop-2.7.4/historyserver/Dockerfile .
# docker push brijeshdhaker/hadoop-historyserver:2.7.4
#

FROM brijeshdhaker/hadoop-base:2.7.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8188/ || exit 1

ENV YARN_CONF_yarn_timeline___service_leveldb___timeline___store_path=/yarn/history

RUN mkdir -p /yarn/history
VOLUME /yarn/history

ADD docker-hadoop-2.7.4/historyserver/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8188

CMD ["/run.sh"]
