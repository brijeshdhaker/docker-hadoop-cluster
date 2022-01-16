#
# docker build -t brijeshdhaker/spark-worker:3.1.2 -f ../../Dockerfile .
#

FROM brijeshdhaker/spark-base:3.1.2

#
COPY docker-spark/worker/worker.sh /

#Give permission to execute scripts
RUN cd / && chmod +x /*.sh

#
CMD ["/bin/bash", "/worker.sh"]