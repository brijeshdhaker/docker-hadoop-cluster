#
# docker build -t bde2020/zeppelin:0.10.0-hadoop3.2.1-java8 -f ../../Dockerfile .
#

FROM apache/zeppelin:0.9.0

#
ARG zeppelin_uid=1000

#
ENV BASE_DIR 		/opt
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_HOME		$BASE_DIR/hadoop-3.2.1
ENV SPARK_HOME 	    $BASE_DIR/spark-3.1.2
ENV HIVE_HOME 	    $BASE_DIR/hive-3.1.2
ENV Z_HOME 	        $BASE_DIR/zeppelin
ENV SPARK_VERSION   3.1.2
ENV HADOOP_VERSION	3.2
ENV HIVE_VERSION	3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$SPARK_HOME/bin:$SPARK_HOME/sbin:$HIVE_HOME/bin

#
ADD resources/tars/hadoop-3.2.1.tar.gz ${BASE_DIR}/
ADD resources/tars/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz ${BASE_DIR}/
ADD resources/tars/zeppelin-0.10.0.tar.gz ${BASE_DIR}/


USER root

#
RUN apt-get update &&  apt-get install -y curl unzip wget grep sed vim tzdata && apt-get clean \
    && mv $BASE_DIR/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} ${SPARK_HOME} \
    && rm ${SPARK_HOME}/jars/guava-14.0.1.jar \
    && rm -Rf $Z_HOME \
    && mv $BASE_DIR/zeppelin-0.10.0-SNAPSHOT ${Z_HOME} \
    && mkdir -p ${Z_HOME}/logs ${Z_HOME}/run ${Z_HOME}/webapps \
    && chmod -R 775 "${Z_HOME}/logs" "${Z_HOME}/run" "${Z_HOME}/notebook" "${Z_HOME}/conf" \
    && chmod 775 ${Z_HOME} \
    && cd /

#
COPY resources/libs/*.jar ${SPARK_HOME}/jars/

# Overwrite default HADOOP configuration files with our config files
COPY docker-zeppelin/docker/client-conf/*.xml  $HADOOP_HOME/etc/hadoop/

#
COPY docker-zeppelin/docker/conf ${Z_HOME}/conf

# Specify the User that the actual main process will run as
USER ${zeppelin_uid}

WORKDIR ${Z_HOME}

#CMD ["bin/zeppelin.sh"]


