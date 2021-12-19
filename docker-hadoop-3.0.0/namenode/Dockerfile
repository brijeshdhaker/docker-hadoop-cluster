FROM brijeshdhaker/hadoop-base:3.0.0

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:9870/ || exit 1

ENV HDFS_CONF_dfs_namenode_name_dir=file:///apps/hostpath/hadoop/3.0.0/dfs/name

ADD docker-hadoop-3.0.0/namenode/run.sh /run.sh
RUN chmod a+x /run.sh

EXPOSE 9870

CMD ["/run.sh"]
