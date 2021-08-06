FROM apache/zeppelin:0.9.0

ENV SPARK_VERSION=3.1.2
ENV HADOOP_VERSION=3.2
ENV HADOOP_AWS_VERSION=3.2.0
ENV AWS_SDK_VERSION=1.11.655

USER root

#
COPY docker/zeppelin/conf /opt/zeppelin/conf/

COPY docker/zeppelin/airline-notebooks /opt/zeppelin/notebook/airline-notebooks
COPY docker/zeppelin/users.parquet /zeppelin/example-files/users.parquet
COPY docker/zeppelin/log4j.properties /opt/zeppelin/conf/log4j.properties

RUN chmod -R 777 /opt/zeppelin/notebook/airline-notebooks/

USER 1000
