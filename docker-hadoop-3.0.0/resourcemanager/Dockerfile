FROM brijeshdhaker/hadoop-base:3.0.0

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8088/ || exit 1

ADD docker-hadoop-3.0.0/resourcemanager/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8088

CMD ["/run.sh"]
