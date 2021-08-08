#
# docker build -t big-data/zeppelin:0.10.0 -f ../../Dockerfile .
#

FROM apache/zeppelin:0.9.0

#
ENV BASE_DIR 		/opt
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_HOME		$BASE_DIR/hadoop-3.2.1
ENV SPARK_HOME 	    $BASE_DIR/spark-3.1.2
ENV HIVE_HOME 	    $BASE_DIR/hive-3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$SPARK_HOME/bin:$SPARK_HOME/sbin:$HIVE_HOME/bin

#
ADD resources/tars/hadoop-3.2.1.tar.gz ${BASE_DIR}/
ADD resources/tars/spark-3.1.2-bin-hadoop3.2.tgz ${BASE_DIR}/
#ADD resources/tars/zeppelin-0.10.0.tar.gz ${BASE_DIR}/


USER root

#
RUN apt-get update &&  apt-get install -y curl unzip wget grep sed vim tzdata && apt-get clean \
    && mv $BASE_DIR/spark-3.1.2-bin-hadoop3.2 ${SPARK_HOME} \
    && rm ${SPARK_HOME}/jars/guava-14.0.1.jar \
#    && rm -Rf $Z_HOME \
#    && mv $BASE_DIR/zeppelin-0.10.0-SNAPSHOT ${Z_HOME} \
#    && mkdir -p ${Z_HOME}/logs ${Z_HOME}/run ${Z_HOME}/webapps \
#    && chmod -R 775 "${Z_HOME}/logs" "${Z_HOME}/run" "${Z_HOME}/notebook" "${Z_HOME}/conf" \
#    && chmod 775 ${Z_HOME} \
    && cd /

#
COPY resources/libs/guava-27.0-jre.jar $SPARK_HOME/jars/
COPY resources/libs/aws-java-sdk-bundle-1.11.888.jar $SPARK_HOME/jars/
COPY resources/libs/hadoop-aws-3.2.0.jar $SPARK_HOME/jars/
COPY resources/libs/postgresql-42.2.23.jar $SPARK_HOME/jars/

# Overwrite default HADOOP configuration files with our config files
COPY docker-zeppelin/docker/client-conf/*.xml  $HADOOP_HOME/etc/hadoop/
COPY docker-zeppelin/docker/conf ${Z_HOME}/conf

#


