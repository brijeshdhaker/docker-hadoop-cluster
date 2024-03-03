#
# ./bin/docker-image-tool.sh -t 3.2.1 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# docker build -t brijeshdhaker/jupyternb:3.2.1 -f Dockerfile .
# docker tag spark-py-notebook:latest brijeshdhaker/spark-py-notebook:latest
#
#

ARG spark_uid=185
FROM spark-py:3.5.0

# Specify the User that the actual main process will run as
USER ${spark_uid}

ENV SPARK_VERSION   3.5.0
ENV SPARK_HOME      /opt/spark

COPY docker-jupyternb/requirements.pip /tmp/requirements.pip
RUN pip install -U -r /tmp/requirements.pip \
    && rm ${SPARK_HOME}/jars/guava*.jar

# install extension to monitor spark
RUN jupyter nbextension install sparkmonitor --py --user --symlink
RUN jupyter nbextension enable  sparkmonitor --py
RUN jupyter serverextension enable --py --user --debug sparkmonitor
RUN ipython profile create && \
echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_kernel_config.py

RUN ln -s /usr/local/lib/python3.8/site-packages/sparkmonitor/listener_2.12.jar $SPARK_HOME/jars/listener_2.12.jar

#
# COPY docker-spark/base/scripts /
COPY docker-spark/base/conf $SPARK_HOME/conf/
COPY resources/libs/guava-27.0-jre.jar $SPARK_HOME/jars/
COPY resources/libs/aws-java-sdk-bundle-1.11.888.jar $SPARK_HOME/jars/
COPY resources/libs/hadoop-aws-3.2.0.jar $SPARK_HOME/jars/
COPY resources/libs/postgresql-42.2.23.jar $SPARK_HOME/jars/

#
VOLUME /home/notebook/

#
CMD jupyter notebook --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=/home/notebook/

#
