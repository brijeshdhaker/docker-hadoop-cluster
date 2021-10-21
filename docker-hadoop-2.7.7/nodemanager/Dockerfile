FROM brijeshdhaker/hadoop-base:2.7.7

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8042/ || exit 1

ADD docker-hadoop-2.7.7/nodemanager/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 8042

CMD ["/run.sh"]
