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
COPY docker-spark/conf $SPARK_HOME/conf/
COPY resources/libs/*.jar $SPARK_HOME/jars/
COPY docker-spark/scripts /

RUN chmod a+rwx /start-common.sh /entrypoint.sh
RUN chmod a+rwx /start-master /start-worker /start-logserver

#
ENTRYPOINT [ "/entrypoint.sh" ]

#