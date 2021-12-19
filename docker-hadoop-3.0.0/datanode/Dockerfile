FROM brijeshdhaker/hadoop-base:3.0.0

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9864/ || exit 1

ENV HDFS_CONF_dfs_datanode_data_dir=file:///apps/hostpath/hadoop/3.0.0/dfs/data

#
ADD docker-hadoop-3.0.0/datanode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 9864

CMD ["/run.sh"]
