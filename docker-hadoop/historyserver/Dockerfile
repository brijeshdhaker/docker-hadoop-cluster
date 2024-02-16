#
# docker build -t brijeshdhaker/hadoop-historyserver:3.1.1 -f docker-hadoop/historyserver/Dockerfile .
# docker push brijeshdhaker/hadoop-historyserver:3.1.1
#

FROM brijeshdhaker/hadoop-base:3.1.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:19888/ || exit 1

ADD docker-hadoop/historyserver/run.sh /run.sh
RUN mkdir -p /mapred/history && \
    chmod -Rf 777 /mapred/history && \
    chown -Rf mapred:hadoop /mapred/history && \
    chown mapred:hadoop /run.sh && chmod 775 /run.sh

EXPOSE 19888

# Specify the User that the actual main process will run as
USER mapred:hadoop

CMD ["/run.sh"]
