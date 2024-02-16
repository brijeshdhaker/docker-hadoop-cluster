#
# docker build -t brijeshdhaker/hadoop-resourcemanager:3.1.1 -f docker-hadoop/resourcemanager/Dockerfile .
# docker push brijeshdhaker/hadoop-resourcemanager:3.1.1
#

FROM brijeshdhaker/hadoop-base:3.1.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8088/ || exit 1

#
ADD docker-hadoop/resourcemanager/run.sh /run.sh
RUN chown yarn:hadoop /run.sh && chmod 775 /run.sh

EXPOSE 8088

# Specify the User that the actual main process will run as
USER yarn:hadoop

CMD ["/run.sh"]
