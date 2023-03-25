#
# docker build -t brijeshdhaker/hadoop-namenode:2.7.4 -f docker-hadoop-2.7.4/namenode/Dockerfile .
# docker push brijeshdhaker/hadoop-namenode:2.7.4
#

FROM brijeshdhaker/hadoop-base:2.7.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9870/ || exit 1

ENV HDFS_CONF_dfs_namenode_name_dir=file:///hadoop/dfs/name
RUN mkdir -p /hadoop/dfs/name

#
VOLUME /hadoop/dfs

ADD docker-hadoop-2.7.4/namenode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 50070

CMD ["/run.sh"]
