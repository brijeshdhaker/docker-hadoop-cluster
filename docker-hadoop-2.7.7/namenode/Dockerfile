#
# docker build -t brijeshdhaker/hadoop-base:2.7.7 -f docker-hadoop-2.7.7/namenode/Dockerfile .
#

FROM brijeshdhaker/hadoop-base:2.7.7

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9870/ || exit 1

ENV HDFS_CONF_dfs_namenode_name_dir=file:///apps/hostpath/hadoop/2.7.7/dfs/name

ADD docker-hadoop-2.7.7/namenode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 50070

CMD ["/run.sh"]
