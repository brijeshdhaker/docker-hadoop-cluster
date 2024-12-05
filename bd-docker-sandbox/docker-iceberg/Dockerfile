#
# docker build -t brijeshdhaker/spark-notebook:3.4.1 -f docker-iceberg/Dockerfile .
# docker push docker.io/brijeshdhaker/spark-notebook:3.4.1
#

FROM tabulario/spark-iceberg:3.4.1_1.4.1

#
COPY resources/libs/*.jar /opt/spark/jars/
COPY docker-iceberg/entrypoint.sh .

RUN chmod -Rf 755 ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]

CMD ["notebook"]




