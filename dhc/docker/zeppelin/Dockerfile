FROM apache/zeppelin:0.9.0

####################
# HIVE
####################
ENV BASE_DIR 		/opt
ENV HADOOP_HOME		$BASE_DIR/hadoop
ENV SPARK_HOME 	    $BASE_DIR/spark
ENV ZEPPELIN_HOME 	$BASE_DIR/zeppelin
ENV SPARK_VERSION   2.4.8
ENV HADOOP_VERSION	3.2.1
ENV HIVE_VERSION	3.1.2
ENV PATH			$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$HIVE_HOME/bin

#
ARG zeppelin_uid=1000

USER root

RUN cd /

#
#RUN apt-get update &&  apt-get install -y curl unzip wget grep sed vim tzdata && apt-get clean \
#    && wget https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
#    && tar -xvzf spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
#    && mv spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} spark \
#    && rm spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
#    && wget https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/$HADOOP_AWS_VERSION/hadoop-aws-$HADOOP_AWS_VERSION.jar \
#    && mv hadoop-aws-$HADOOP_AWS_VERSION.jar spark/jars \
#    && wget https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.655/aws-java-sdk-bundle-1.11.655.jar \
#    && mv aws-java-sdk-bundle-1.11.655.jar spark/jars \
#    && wget https://repo1.maven.org/maven2/com/google/guava/guava/27.0-jre/guava-27.0-jre.jar \
#    && mv guava-27.0-jre.jar spark/jars \
#    && rm spark/jars/guava-14.0.1.jar \
#    && cd /

#
COPY docker/zeppelin/conf /opt/zeppelin/conf

#
COPY docker/zeppelin/airline-notebooks /opt/zeppelin/notebook/airline-notebooks
RUN chmod -R 777 /opt/zeppelin/notebook/airline-notebooks/

# Specify the User that the actual main process will run as
USER ${zeppelin_uid}

