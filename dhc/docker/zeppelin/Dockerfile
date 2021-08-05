FROM apache/zeppelin:0.9.0

#
ENV BASE_DIR 		/opt
ENV JAVA_HOME       /usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_HOME		$BASE_DIR/hadoop
ENV SPARK_HOME 	    $BASE_DIR/spark
ENV HIVE_HOME 	    $BASE_DIR/hive
ENV ZEPPELIN_HOME 	$BASE_DIR/zeppelin
ENV SPARK_VERSION   3.1.2
ENV HADOOP_VERSION	3.2
ENV HIVE_VERSION	3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

#
ADD tars/hadoop-3.2.1.tar.gz /opt
ADD tars/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz /opt

#
ARG zeppelin_uid=1000

USER root

#
RUN apt-get update &&  apt-get install -y curl unzip wget grep sed vim tzdata && apt-get clean \
    && mv $BASE_DIR/hadoop-3.2.1 $BASE_DIR/hadoop \
    && mv $BASE_DIR/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} $BASE_DIR/spark \
    && rm $BASE_DIR/spark/jars/guava-14.0.1.jar \
    && cd /

#
COPY libs/*.jar $BASE_DIR/spark/jars/

# Overwrite default HADOOP configuration files with our config files
COPY docker/zeppelin/client-conf/*.xml  $HADOOP_HOME/etc/hadoop/

#
COPY docker/zeppelin/conf /opt/zeppelin/conf

# Specify the User that the actual main process will run as
USER ${zeppelin_uid}

