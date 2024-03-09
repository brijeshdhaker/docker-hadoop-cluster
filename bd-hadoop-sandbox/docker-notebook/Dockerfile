#
# ./bin/docker-image-tool.sh -t 3.5.0 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# docker build -t brijeshdhaker/notebook:3.5.0 -f docker-notebook/Dockerfile .
# docker tag spark-py-notebook:latest brijeshdhaker/spark-py-notebook:latest
# jupyter/pyspark-notebook
# jupyter/all-spark-notebook:spark-3.5.0

FROM jupyter/all-spark-notebook:latest

USER root
#
RUN set -x &&  \
    apt-get update && \
    apt-get install -y --no-install-recommends sudo curl vim unzip openjdk-17-jdk build-essential software-properties-common ssh && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    echo "basic installation done."

#
# Install Jupyter and other python deps
COPY docker-notebook/requirements.pip /tmp/requirements.pip
RUN set -x && pip3 install -U -r /tmp/requirements.pip && \
    fix-permissions "${CONDA_DIR}" && \
    fix-permissions "/home/${NB_USER}"

COPY docker-notebook/listener_2.12.jar /opt/conda/lib/python3.11/site-packages/sparkmonitor/listener.jar
# Add scala kernel via spylon-kernel
RUN set -x && \
    # ipython profile create && \
    echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_kernel_config.py && \
    echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_config.py && \
    ln -s /opt/conda/lib/python3.11/site-packages/sparkmonitor/listener.jar /usr/local/spark/jars/listener.jar && \
    python3 -m spylon_kernel install && \
    jupyter toree install --spark_home=/usr/local/spark && \
    echo "Setup done."

#
#RUN set -x && \
#    conda install --yes jupysql && \
#    conda clean --all -f -y && \
#    fix-permissions "${CONDA_DIR}" && \
#    fix-permissions "/home/${NB_USER}"

# Download and install IJava jupyter kernel
RUN set -x && \
    curl https://github.com/SpencerPark/IJava/releases/download/v1.3.0/ijava-1.3.0.zip -Lo ijava-1.3.0.zip && \
    unzip ijava-1.3.0.zip && \
    python3 install.py --sys-prefix && \
    rm ijava-1.3.0.zip

# Add iceberg spark runtime jar to IJava classpath
ENV IJAVA_CLASSPATH=/usr/local/spark/jars/*

# Install AWS CLI
RUN set -x && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    sudo ./aws/install && \
    rm awscliv2.zip && \
    rm -rf aws/ && \
    echo "AWS Client installation done."

#
RUN set -x && mkdir -p /root/.ipython/profile_default/startup
COPY docker-notebook/startup/00-prettytables.py /home/${NB_USER}/.ipython/profile_default/startup
COPY docker-notebook/startup/README /home/${NB_USER}/.ipython/profile_default/startup

#
USER ${NB_UID}