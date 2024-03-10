#
# ./bin/docker-image-tool.sh -t 3.5.0 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# docker build -t brijeshdhaker/notebook:3.5.0 -f docker-notebook/Dockerfile .
# docker tag spark-py-notebook:latest brijeshdhaker/spark-py-notebook:latest
# jupyter/pyspark-notebook
# jupyter/all-spark-notebook:spark-3.5.0
# spark-py:3.5.0

FROM jupyter/all-spark-notebook:spark-3.5.0

USER root
#
RUN set -x &&  \
    apt-get update && \
    apt-get install -y --no-install-recommends openjdk-17-jdk curl vim unzip ssh && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    echo "basic installation done."


# Install Jupyter and other python deps
COPY docker-notebook/requirements.pip /tmp/requirements.pip
#RUN set -x && pip install -U -r /tmp/requirements.pip

#
RUN set -x && mamba install --yes --file /tmp/requirements.pip && \
    mamba clean --all -f -y && \
    fix-permissions "${CONDA_DIR}" && \
    fix-permissions "/home/${NB_USER}"

#
RUN set -x && conda install  -c conda-forge --yes spylon-kernel && \
    conda clean --all -f -y && \
    fix-permissions "${CONDA_DIR}" && \
    fix-permissions "/home/${NB_USER}"


# Add scala kernel via spylon-kernel
RUN set -x && \
    python3 -m spylon_kernel install && \
    # python3 -m toree install && \
    echo "basic installation done."


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
    fix-permissions "${CONDA_DIR}" && \
    fix-permissions "/home/${NB_USER}" && \
    echo "AWS Client installation done."

# ENV PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.9.7-src.zip:$PYTHONPATH
# ENV PATH="$SPARK_HOME/sbin:$SPARK_HOME/bin:${JAVA_HOME}/bin:${PATH}"

#
RUN mkdir -p /home/${NB_USER}/.ipython/profile_default/startup
COPY docker-notebook/startup/00-prettytables.py /home/${NB_USER}/.ipython/profile_default/startup
COPY docker-notebook/startup/README /home/${NB_USER}/.ipython/profile_default/startup

#
RUN set -x && \
#    ipython profile create && \
#    echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_kernel_config.py && \
#    echo "c.InteractiveShellApp.extensions.append('sparkmonitor.kernelextension')" >>  $(ipython profile locate default)/ipython_config.py && \
#    ln -s /usr/local/lib/python3.10/dist-packages/sparkmonitor/listener.jar $SPARK_HOME/jars/listener.jar && \
    fix-permissions "${CONDA_DIR}" && \
    fix-permissions "/home/${NB_USER}" && \
    echo "Setup done."

# Add a notebook command
#RUN set -x \
# && echo '#! /bin/sh' >> /bin/notebook \
# && echo 'export PYSPARK_DRIVER_PYTHON=jupyter-notebook' >> /bin/notebook \
# && echo "export PYSPARK_DRIVER_PYTHON_OPTS=\"--notebook-dir=/home/notebooks --ip='*' --NotebookApp.token='' --NotebookApp.password='' --port=8888 --no-browser --allow-root\"" >> /bin/notebook \
# && echo "pyspark" >> /bin/notebook \
# && chmod u+x /bin/notebook


#
# VOLUME /home/notebooks/

# COPY docker-notebook/entrypoint.sh /entrypoint.sh
# RUN chown -f root:root /entrypoint.sh && \
#     chmod -Rf 775 /entrypoint.sh

#
# ENTRYPOINT ["/entrypoint.sh"]
RUN set -x && \
    curl -fLo coursier https://git.io/coursier-cli-"$(uname | tr LD ld)"  && \
    chmod +x coursier && \
    ./coursier launch --fork almond --scala 2.12 -- --install --force && \
    rm -f coursier && \
    fix-permissions "${CONDA_DIR}" && \
    fix-permissions "/home/${NB_USER}" && \
    echo "scala setup done"

#
# CMD ["jupyter lab --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=/home/notebooks/"]
#CMD ["notebook"]
#
USER ${NB_USER}