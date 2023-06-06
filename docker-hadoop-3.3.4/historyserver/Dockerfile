#
# docker build -t brijeshdhaker/hadoop-historyserver:3.3.4 -f docker-hadoop-3.3.4/historyserver/Dockerfile .
# docker push brijeshdhaker/hadoop-historyserver:3.3.4
#

FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:19888/ || exit 1

RUN mkdir -p /mr/history
VOLUME /mr

ADD docker-hadoop-3.3.4/historyserver/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 19888

CMD ["/run.sh"]
