#
# docker build -t brijeshdhaker/hadoop-datanode:3.3.4 -f docker-hadoop-3.3.4/nodemanager/Dockerfile .
# docker push brijeshdhaker/hadoop-datanode:3.3.4
#

FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9864/ || exit 1

ENV HDFS_CONF_dfs_datanode_data_dir=file:///hadoop/dfs/data
RUN mkdir -p /hadoop/dfs/data
VOLUME /hadoop/dfs/data

#
ADD docker-hadoop-3.3.4/datanode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 9864

CMD ["/run.sh"]
