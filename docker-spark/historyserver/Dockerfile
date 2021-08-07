#
# docker build -t bde2020/spark-master:3.1.2 -f ../../Dockerfile .
#

#
FROM bde2020/spark-base:3.1.2

#
COPY docker-spark/historyserver/spark.logserver.conf $SPARK_HOME/conf/

#
COPY docker-spark/historyserver/historyserver.sh /
#Give permission to execute scripts
RUN cd / && chmod +x /*.sh

#
CMD ["/bin/bash", "/historyserver.sh"]