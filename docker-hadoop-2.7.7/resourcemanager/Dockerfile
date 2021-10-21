FROM brijeshdhaker/hadoop-base:2.7.7

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8088/ || exit 1

ADD docker-hadoop-2.7.7/resourcemanager/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8088

CMD ["/run.sh"]
