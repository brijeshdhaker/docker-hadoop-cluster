#
# $ ./bin/docker-image-tool.sh -t latest -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# $ docker build -t spark-py:3.1.2-latest -f Dockerfile .
# $ docker tag spark-py:3.1.2-latest brijeshdhaker/spark-py:3.1.2-latest
#
#

ARG spark_uid=185
FROM spark-py:latest

# Specify the User that the actual main process will run as
USER ${spark_uid}
####################
# SPARK 3.1.2
####################
ENV SPARK_VERSION   3.1.2
ENV SPARK_HOME      /opt/spark

#
COPY conf $SPARK_HOME/conf/
COPY libs $SPARK_HOME/jars/
COPY scripts /

RUN chmod a+rwx /start-common.sh
RUN chmod a+rwx /entrypoint.sh
RUN chmod a+rwx /start-master
RUN chmod a+rwx /start-worker
RUN chmod a+rwx /start-logserver

ENTRYPOINT [ "/entrypoint.sh" ]