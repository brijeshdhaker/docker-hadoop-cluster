#
# docker build -t brijeshdhaker/hadoop-namenode:3.3.4 -f docker-hadoop-3.3.4/namenode/Dockerfile .
# docker push brijeshdhaker/hadoop-namenode:3.3.4
#

FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"
HEALTHCHECK CMD curl -f http://localhost:9870/ || exit 1

#
ENV MULTIHOMED_NETWORK=0
ENV HDFS_CONF_dfs_namenode_name_dir=file:///hadoop/dfs/name

RUN mkdir -p /hadoop/dfs/name && \
    chmod -Rf 777 /hadoop/dfs/name && \
    chown -Rf hdfs:hadoop /hadoop/dfs/name

VOLUME /hadoop/dfs

ADD docker-hadoop-3.3.4/namenode/run.sh /run.sh
RUN chmod -Rf 755 /run.sh

EXPOSE 9870

# Specify the User that the actual main process will run as
USER hdfs:hadoop

CMD ["/run.sh"]
