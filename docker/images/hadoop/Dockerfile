#
# docker build -t hadoop:3.2.0 -f Dockerfile .
#
#
FROM openjdk:8-jre-slim

####################
# HADOOP 3.2.0
####################

ENV HADOOP_VERSION	3.2.0
ENV HADOOP_HOME		/usr/local/hadoop
ENV HADOOP_OPTS		-Djava.library.path=/usr/local/hadoop/lib/native
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# http://archive.apache.org/dist/hadoop/core/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz 

ADD tar-balls/hadoop-3.2.0.tar.gz /usr/local

RUN apt-get update && \ 
	DEBIAN_FRONTEND=noninteractive apt-get install -y wget libssl-dev && \
	rm -rf /var/lib/apt/lists/* && \
	mv /usr/local/hadoop-$HADOOP_VERSION $HADOOP_HOME && \
    mkdir -p $HADOOP_HOME/logs


# Overwrite default HADOOP configuration files with our config files
COPY conf/core-site.xml  $HADOOP_HOME/etc/hadoop/core-site.xml
COPY conf/fair-scheduler.xml  $HADOOP_HOME/etc/hadoop/fair-scheduler.xml
COPY conf/hdfs-site.xml  $HADOOP_HOME/etc/hadoop/hdfs-site.xml
COPY conf/mapred-site.xml  $HADOOP_HOME/etc/hadoop/mapred-site.xml
COPY conf/yarn-site.xml  $HADOOP_HOME/etc/hadoop/yarn-site.xml

# Helper script for starting namenode
ADD start-namenode.sh /usr/local/bin/start-namenode.sh
ADD start-hdfs.sh /usr/local/bin/start-hdfs.sh

# Helper script for starting resourcemanager and history server
ADD start-yarn-historyserver.sh /usr/local/bin/start-yarn-historyserver.sh

# Formatting HDFS
RUN mkdir -p /data && \
	chmod +x /usr/local/bin/start-namenode.sh && \
	chmod +x /usr/local/bin/start-hdfs.sh && \
    chmod +x /usr/local/bin/start-yarn-historyserver.sh

VOLUME /data


####################
# PORTS
####################
#
# http://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.3.0/bk_HDP_Reference_Guide/content/reference_chap2.html
# http://www.cloudera.com/content/cloudera/en/documentation/core/latest/topics/cdh_ig_ports_cdh5.html
# http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/core-default.xml
# http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml

# HDFS: NameNode (NN):
#	9000 = fs.defaultFS			(IPC / File system metadata operations)
#						(9000 is also frequently used alternatively)
#	8022 = dfs.namenode.servicerpc-address	(optional port used by HDFS daemons to avoid sharing RPC port)
#       9870 = dfs.namenode.http-address	(HTTP  / NN Web UI)
#	9871 = dfs.namenode.https-address       (HTTPS / Secure UI)
# HDFS: DataNode (DN):
#	9866 = dfs.datanode.address             (Data transfer)
#	9867 = dfs.datanode.ipc.address         (IPC / metadata operations)
#	9864 = dfs.datanode.http.address	(HTTP  / DN Web UI)
#	9865 = dfs.datanode.https.address	(HTTPS / Secure UI)
# HDFS: Secondary NameNode (SNN)
#	9868 = dfs.secondary.http.address	(HTTP / Checkpoint for NameNode metadata)

EXPOSE 9000 9870 9866 9867 9864 9868 19888 10020 8032 8031 8030

CMD ["hdfs"]
