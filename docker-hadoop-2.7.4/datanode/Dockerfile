#
# docker build -t brijeshdhaker/hadoop-datanode:2.7.4 -f docker-hadoop-2.7.4/datanode/Dockerfile .
# docker push brijeshdhaker/hadoop-datanode:2.7.4
#

FROM brijeshdhaker/hadoop-base:2.7.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9864/ || exit 1

ENV HDFS_CONF_dfs_datanode_data_dir=file:///hadoop/dfs/data
RUN mkdir -p /hadoop/dfs/data
VOLUME /hadoop/dfs

#
ADD docker-hadoop-2.7.4/datanode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 50010

CMD ["/run.sh"]
