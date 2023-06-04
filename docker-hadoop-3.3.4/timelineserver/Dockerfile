#
# docker build -t brijeshdhaker/hadoop-timelineserver:3.3.4 -f docker-hadoop-3.3.4/timelineserver/Dockerfile .
# docker push brijeshdhaker/hadoop-timelineserver:3.3.4
#

FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8188/ || exit 1

RUN mkdir -p /yarn/timeline
VOLUME /yarn

ADD docker-hadoop-3.3.4/timelineserver/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8188

CMD ["/run.sh"]
