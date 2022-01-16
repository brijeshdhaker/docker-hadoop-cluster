#
# docker build -t brijeshdhaker/spark-master:3.1.2 -f ../../Dockerfile .
#

FROM brijeshdhaker/spark-base:3.1.2

#
COPY docker-spark/master/spark-yarn.conf $SPARK_HOME/conf/
#
COPY docker-spark/master/master.sh /

#Give permission to execute scripts
RUN cd / && chmod +x /*.sh

#
CMD ["/bin/bash", "/master.sh"]

