#
# docker build -t big-data/spark-master:3.1.2 -f ../../Dockerfile .
#

#
FROM big-data/spark-base:3.1.2

#
COPY docker-spark/historyserver/spark.logserver.conf $SPARK_HOME/conf/

#
COPY docker-spark/historyserver/historyserver.sh /
#Give permission to execute scripts
RUN cd / && chmod +x /*.sh

#
CMD ["/bin/bash", "/historyserver.sh"]