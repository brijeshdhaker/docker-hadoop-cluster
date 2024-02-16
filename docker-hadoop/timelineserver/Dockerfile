#
# docker build -t brijeshdhaker/hadoop-timelineserver:3.1.1 -f docker-hadoop/timelineserver/Dockerfile .
# docker push brijeshdhaker/hadoop-timelineserver:3.1.1
#

FROM brijeshdhaker/hadoop-base:3.1.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8188/ || exit 1

ADD docker-hadoop/timelineserver/run.sh /run.sh
RUN mkdir -p /yarn/timeline && \
    chmod -Rf 775 /yarn/timeline && \
    chown -Rf yarn:hadoop /yarn/timeline && \
    chown yarn:hadoop /run.sh && chmod 775 /run.sh && \
    echo "done"

EXPOSE 8188

# Specify the User that the actual main process will run as
USER yarn:hadoop

CMD ["/run.sh"]
