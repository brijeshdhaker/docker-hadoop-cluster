#
# docker build -t brijeshdhaker/hadoop-namenode:3.3.4 -f docker-hadoop-3.3.4/namenode/Dockerfile .
# docker push brijeshdhaker/hadoop-namenode:3.3.4
#

FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9870/ || exit 1

ENV HDFS_CONF_dfs_namenode_name_dir=file:///hadoop/dfs/name
RUN mkdir -p /hadoop/dfs/name
VOLUME /hadoop/dfs/name

ADD docker-hadoop-3.3.4/namenode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 9870

CMD ["/run.sh"]
