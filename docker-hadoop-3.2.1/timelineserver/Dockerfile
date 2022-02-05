#
# docker build -t brijeshdhaker/hadoop-timelineserver:3.2.1 -f docker-hadoop-3.2.1/timelineserver/Dockerfile .
#

FROM brijeshdhaker/hadoop-base:3.2.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8188/ || exit 1

RUN mkdir -p /tmp/yarn/timeline
VOLUME /tmp/yarn/timeline

ADD docker-hadoop-3.2.1/timelineserver/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8188

CMD ["/run.sh"]
