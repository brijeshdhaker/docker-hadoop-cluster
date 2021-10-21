FROM brijeshdhaker/hadoop-base:2.7.7

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9864/ || exit 1

ENV HDFS_CONF_dfs_datanode_data_dir=file:///apps/hostpath/hadoop/2.7.7/dfs/data

#
ADD docker-hadoop-2.7.7/datanode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 50010

CMD ["/run.sh"]
