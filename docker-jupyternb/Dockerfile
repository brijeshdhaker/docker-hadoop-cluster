#
# $ ./bin/docker-image-tool.sh -t latest -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# $ docker build -t spark-py-notebook:latest -f Dockerfile .
# $ docker tag spark-py-notebook:latest brijeshdhaker/spark-py-notebook:latest
#
#

ARG spark_uid=185
FROM spark-py:3.1.2

# Specify the User that the actual main process will run as
USER ${spark_uid}
####################
# SPARK 3.1.2
####################
ENV SPARK_VERSION   3.1.2
ENV SPARK_HOME      /opt/spark

COPY docker-jupyternb/requirements.pip /tmp/requirements.pip
RUN pip install -U -r /tmp/requirements.pip

# install extension to monitor spark
RUN jupyter nbextension install sparkmonitor --py --user --symlink
RUN jupyter nbextension enable  sparkmonitor --py
RUN jupyter serverextension enable --py --user --debug sparkmonitor
RUN ipython profile create && \
echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_kernel_config.py

RUN ln -s /usr/local/lib/python3.8/site-packages/sparkmonitor/listener_2.12.jar $SPARK_HOME/jars/listener_2.12.jar

#
VOLUME /home/notebook/

#
CMD jupyter notebook --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=/home/notebook/

#
