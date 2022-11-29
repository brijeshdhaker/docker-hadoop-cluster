#
# $ ./bin/docker-image-tool.sh -t 3.3.1 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# $ docker build -t brijeshdhaker/spark-base:3.3.1 -f Dockerfile ../../
# $ docker tag brijeshdhaker/spark-base:3.3.1 brijeshdhaker/spark-base:3.3.1
#
#

ARG spark_uid=185
FROM spark-py:3.3.1

# Specify the User that the actual main process will run as
USER ${spark_uid}

####################
# SPARK 3.1.2
####################
ENV SPARK_VERSION   3.3.1
ENV SPARK_HOME      /opt/spark

# Install Reuired
RUN apt-get update && apt-get install -y --no-install-recommends wget grep sed net-tools curl netcat gnupg libsnappy-dev vim && \
    rm ${SPARK_HOME}/jars/guava*.jar && \
	apt-get clean && \
    rm -rf /var/lib/apt/lists/*

#
# COPY docker-spark/base/scripts /
COPY docker-spark/base/conf $SPARK_HOME/conf/
COPY resources/libs/guava-27.0-jre.jar $SPARK_HOME/jars/
COPY resources/libs/aws-java-sdk-bundle-1.11.888.jar $SPARK_HOME/jars/
COPY resources/libs/hadoop-aws-3.2.0.jar $SPARK_HOME/jars/
COPY resources/libs/postgresql-42.2.23.jar $SPARK_HOME/jars/

#