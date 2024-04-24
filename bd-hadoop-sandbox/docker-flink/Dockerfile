#
# docker build -t brijeshdhaker/flink:1.19.0 -f bd-hadoop-sandbox/docker-flink/Dockerfile ./bd-hadoop-sandbox
# docker push brijeshdhaker/flink:1.19.0
#

FROM flink:1.19.0-scala_2.12-java11

# Download connector libraries
# RUN wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/mysql/mysql-connector-java/8.0.19/mysql-connector-java-8.0.19.jar;

#
ENV HADOOP_VERSION=3.3.4
ENV HADOOP_HOME=/opt/hadoop
ENV HIVE_HOME=/opt/hive
ENV HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin
ENV HADOOP_LOG_DIR=${HADOOP_LOG_DIR:-"/apps/var/log/hadoop"}

# Download connector libraries
RUN set -x && \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-kafka/3.1.0-1.18/flink-connector-kafka-3.1.0-1.18.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka/3.1.0-1.18/flink-sql-connector-kafka-3.1.0-1.18.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-jdbc/3.1.2-1.18/flink-connector-jdbc-3.1.2-1.18.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-cassandra_2.12/3.1.0-1.17/flink-connector-cassandra_2.12-3.1.0-1.17.jar; \
    # wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-hive-3.1.3_2.12/1.19.0/flink-sql-connector-hive-3.1.3_2.12-1.19.0.jar; \
    wget -P /opt/flink/lib/ https://repo.maven.apache.org/maven2/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar;

# Add Drivers & Other Libs
# COPY resources/libs/mysql-connector-java-8.0.23.jar /opt/flink/lib/