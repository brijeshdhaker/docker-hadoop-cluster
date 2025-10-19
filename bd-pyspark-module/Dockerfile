#
# docker build -t brijeshdhaker/python-base:3.11.13 -f bd-pyspark-module/Dockerfile ./bd-pyspark-module
# docker push brijeshdhaker/python-base:3.11.13
#


FROM python:3.11.13

ENV HADOOP_VERSION=3.3.4
ENV PYTHONPATH=$PYTHONPATH:/apps/bd-pyspark-module-1.0.0.zip

RUN set -x && \
    mkdir -p  /apps

COPY requirements.pip /tmp/requirements.pip
RUN pip3 install -U -r /tmp/requirements.pip

COPY target/bd-pyspark-module-1.0.0.zip /apps/bd-pyspark-module-1.0.0.zip
