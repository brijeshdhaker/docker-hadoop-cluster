#
# docker build -t hadoop-3.2.1:latest -f Dockerfile .
# docker tag 1fe3d8f47868 raspberry:5000/hadoop-3.2.1:latest
# docker tag hadoop:3.2.1 raspberry:5000/hadoop-3.2.1:latest
# docker push raspberry:5000/hadoop-3.2.1:latest
#
# docker build -t raspberry:5000/hadoop-3.2.1:latest -f Dockerfile .
#
FROM openjdk:8-jre-slim

####################
# HADOOP 3.2.1
####################

ENV HADOOP_VERSION	3.2.1
ENV HADOOP_HOME		/usr/local/hadoop
ENV HADOOP_OPTS		-Djava.library.path=/usr/local/hadoop/lib/native
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

#
ADD tar-balls/hadoop-3.2.1.tar.gz /usr/local
#
RUN apt-get update && \
	DEBIAN_FRONTEND=noninteractive apt-get install -y wget libssl-dev && \
	ln -s /usr/local/hadoop-$HADOOP_VERSION ${HADOOP_HOME} && \
    mkdir -p $HADOOP_HOME/logs && mkdir -p /tmp/work && \
    rm -f /usr/local/hadoop*.gz  && \
    rm -rf /var/lib/apt/lists/*

# Overwrite default HADOOP configuration files with our config files
COPY conf/*.xml  $HADOOP_HOME/etc/hadoop/
# Helper script for starting namenode
COPY *.sh /usr/local/bin/

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
#	9000 = fs.defaultFS			            (IPC / File system metadata operations)
#						                    (9000 is also frequently used alternatively)
#	8022 = dfs.namenode.servicerpc-address	(optional port used by HDFS daemons to avoid sharing RPC port)
#   9870 = dfs.namenode.http-address	    (HTTP  / NN Web UI)
#	9871 = dfs.namenode.https-address       (HTTPS / Secure UI)
# HDFS: DataNode (DN):
#	9866 = dfs.datanode.address             (Data transfer)
#	9867 = dfs.datanode.ipc.address         (IPC / metadata operations)
#	9864 = dfs.datanode.http.address	    (HTTP  / DN Web UI)
#	9865 = dfs.datanode.https.address	    (HTTPS / Secure UI)
# HDFS: Secondary NameNode (SNN)
#	9868 = dfs.secondary.http.address	    (HTTP / Checkpoint for NameNode metadata)

EXPOSE 9000 9870 9866 9867 9864 9868 19888 10020 8032 8031 8030

CMD ["hdfs"]
