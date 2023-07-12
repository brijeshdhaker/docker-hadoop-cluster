#
# docker build -t brijeshdhaker/hadoop-datanode:3.2.1 -f docker-hadoop/datanode/Dockerfile .
# docker push brijeshdhaker/hadoop-datanode:3.2.1
#

FROM brijeshdhaker/hadoop-base:3.2.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9864/ || exit 1

ENV HDFS_CONF_dfs_datanode_data_dir=file:///hadoop/dfs/data

RUN mkdir -p /hadoop/dfs/data && \
    chmod -Rf 777 /hadoop/dfs/data && \
    chown -Rf hdfs:hadoop /hadoop/dfs/data

#
ENV MULTIHOMED_NETWORK=0
ADD docker-hadoop/datanode/run.sh /run.sh
RUN chmod -Rf 755 /run.sh

EXPOSE 9864

# Specify the User that the actual main process will run as
USER root:root

CMD ["/run.sh"]
