#
# docker build -t brijeshdhaker/spark-historyserver:3.3.1 -f docker-spark/historyserver/Dockerfile .
#
ARG spark_uid=185
#
FROM apache/spark:3.3.1

# Specify the User that the actual main process will run as
USER ${spark_uid}
ENV SPARK_VERSION   3.3.1
ENV SPARK_HOME      /opt/spark

#
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends net-tools curl netcat \
    && rm -rf /var/lib/apt/lists/* \
    # Removed the .cache to save space
    && rm -rf /var/cache/apt/*

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