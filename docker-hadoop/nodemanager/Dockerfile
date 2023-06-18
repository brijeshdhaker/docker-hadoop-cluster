#
# docker build -t brijeshdhaker/hadoop-nodemanager:3.2.4 -f docker-hadoop/nodemanager/Dockerfile .
# docker push brijeshdhaker/hadoop-nodemanager:3.2.4
#

FROM brijeshdhaker/hadoop-base:3.2.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8042/ || exit 1

ADD docker-hadoop/nodemanager/run.sh /run.sh
RUN chown yarn:hadoop /run.sh && chmod 775 /run.sh

EXPOSE 8042

# Specify the User that the actual main process will run as
USER yarn:hadoop

CMD ["/run.sh"]
