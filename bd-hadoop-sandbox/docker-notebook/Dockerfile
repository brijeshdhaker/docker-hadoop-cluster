#
# ./bin/docker-image-tool.sh -t 3.5.0 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# docker build -t brijeshdhaker/notebook:3.5.0 -f docker-notebook/Dockerfile .
# docker tag spark-py-notebook:latest brijeshdhaker/spark-py-notebook:latest
# jupyter/pyspark-notebook
# jupyter/all-spark-notebook:spark-3.5.0

ARG spark_uid=185
FROM spark-py:3.5.0

# Specify the User that the actual main process will run as
USER ${spark_uid}

ENV SPARK_HOME=/opt/spark
ENV SPARK_VERSION=3.5.0
ENV SPARK_MAJOR_VERSION=3.5
# ENV JAVA_HOME=/opt/java/openjdk

#
RUN set -x &&  \
    apt-get update && \
    apt-get install -y --no-install-recommends sudo openjdk-17-jdk curl vim unzip build-essential software-properties-common ssh && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    echo "basic installation done."

# Install Jupyter and other python deps
COPY docker-notebook/requirements.pip /tmp/requirements.pip
RUN set -x && pip install -U -r /tmp/requirements.pip

# Add scala kernel via spylon-kernel
RUN set -x && python3 -m spylon_kernel install

# Download and install IJava jupyter kernel
RUN set -x && \
    curl https://github.com/SpencerPark/IJava/releases/download/v1.3.0/ijava-1.3.0.zip -Lo ijava-1.3.0.zip && \
    unzip ijava-1.3.0.zip && \
    python3 install.py --sys-prefix && \
    rm ijava-1.3.0.zip


# Install AWS CLI
RUN set -x && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    sudo ./aws/install && \
    rm awscliv2.zip && \
    rm -rf aws/ && \
    echo "AWS Client installation done."

ENV PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.9.7-src.zip:$PYTHONPATH
ENV PATH="$SPARK_HOME/sbin:$SPARK_HOME/bin:${JAVA_HOME}/bin:${PATH}"

#
RUN mkdir -p /root/.ipython/profile_default/startup
COPY docker-notebook/startup/00-prettytables.py /root/.ipython/profile_default/startup
COPY docker-notebook/startup/README /root/.ipython/profile_default/startup

#
RUN set -x && \
    ipython profile create && \
    echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_kernel_config.py && \
    echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_config.py && \
    ln -s /usr/local/lib/python3.10/dist-packages/sparkmonitor/listener_2.12.jar $SPARK_HOME/jars/listener_2.12.jar && \
    jupyter toree install --spark_home=/opt/spark && \
    echo "Setup done."

# Add a notebook command
#RUN set -x \
# && echo '#! /bin/sh' >> /bin/notebook \
# && echo 'export PYSPARK_DRIVER_PYTHON=jupyter-notebook' >> /bin/notebook \
# && echo "export PYSPARK_DRIVER_PYTHON_OPTS=\"--notebook-dir=/home/notebooks --ip='*' --NotebookApp.token='' --NotebookApp.password='' --port=8888 --no-browser --allow-root\"" >> /bin/notebook \
# && echo "pyspark" >> /bin/notebook \
# && chmod u+x /bin/notebook


#
VOLUME /home/notebooks/

COPY docker-notebook/entrypoint.sh /entrypoint.sh
RUN chown -f root:root /entrypoint.sh && \
    chmod -Rf 775 /entrypoint.sh

#
ENTRYPOINT ["/entrypoint.sh"]

#
CMD ["jupyter notebook --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=/home/notebooks/"]
#CMD ["notebook"]
#