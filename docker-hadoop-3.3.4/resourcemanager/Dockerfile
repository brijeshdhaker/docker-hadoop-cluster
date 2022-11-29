#
# docker build -t brijeshdhaker/hadoop-resourcemanager:3.3.4 -f docker-hadoop-3.3.4/resourcemanager/Dockerfile .
# docker push brijeshdhaker/hadoop-resourcemanager:3.3.4
#

FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8088/ || exit 1

ADD docker-hadoop-3.3.4/resourcemanager/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8088

CMD ["/run.sh"]
