#
# docker build -t brijeshdhaker/spark-historyserver:3.1.2 -f docker-spark/historyserver/Dockerfile .
#
ARG spark_uid=185
#
FROM apache/spark:v3.2.1

# Specify the User that the actual main process will run as
USER ${spark_uid}
ENV SPARK_VERSION   3.1.2
ENV SPARK_HOME      /opt/spark

#
COPY docker-spark/historyserver/spark.logserver.conf $SPARK_HOME/conf/

#
COPY docker-spark/historyserver/entrypoint.sh /
COPY docker-spark/historyserver/startup.sh /

#Give permission to execute scripts
RUN cd / && chmod +x /*.sh

#
ENTRYPOINT ["/entrypoint.sh"]

#
CMD ["/bin/bash", "/startup.sh"]