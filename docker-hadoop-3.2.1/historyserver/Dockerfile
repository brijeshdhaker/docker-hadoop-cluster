#
# docker build -t brijeshdhaker/hadoop-historyserver:3.2.1 -f docker-hadoop-3.2.1/historyserver/Dockerfile .
#

FROM brijeshdhaker/hadoop-base:3.2.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:19888/ || exit 1

RUN mkdir -p /tmp/yarn/history
VOLUME /tmp/yarn/history

ADD docker-hadoop-3.2.1/historyserver/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 19888

CMD ["/run.sh"]
