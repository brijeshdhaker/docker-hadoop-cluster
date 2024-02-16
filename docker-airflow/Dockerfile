#
# docker build -t brijeshdhaker/airflow:2.5.3 -f docker-airflow/Dockerfile .
# docker push brijeshdhaker/airflow:2.5.3
#

FROM apache/airflow:2.5.3
USER root


COPY docker-airflow/requirements.txt /
COPY docker-airflow/constraints-3.7.txt /

RUN apt-get update \
  && apt-get install -y --no-install-recommends vim default-jdk \
  && apt-get autoremove -yqq --purge \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

USER airflow
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# RUN pip install --no-cache-dir -r /requirements.txt
# RUN pip install --no-cache-dir lxml
# RUN pip install --no-cache-dir apache-airflow-providers-apache-spark==2.1.3
# RUN pip install --no-cache-dir apache-airflow-providers-cncf-kubernetes
RUN pip install pyspark==3.1.2 apache-airflow[apache.spark]==2.5.3 apache-airflow-providers-apache-cassandra==3.1.1 #--constraint constraints-3.7.txt
# RUN pip install --no-cache-dir apache-airflow-providers-apache-spark==2.1.3
#
