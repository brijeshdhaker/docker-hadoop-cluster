#
# ./bin/docker-image-tool.sh -t 3.5.0 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build
# docker build -t brijeshdhaker/notebook:3.5.1 -f docker-notebook/Dockerfile .
# docker tag spark-py-notebook:latest brijeshdhaker/spark-py-notebook:latest
# jupyter/pyspark-notebook
# jupyter/all-spark-notebook:spark-3.5.0
# spark-py:3.5.0

FROM quay.io/jupyter/all-spark-notebook:spark-3.5.1

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

#
RUN mkdir -p /home/${NB_USER}/.ipython/profile_default/startup
COPY docker-notebook/startup/00-prettytables.py /home/${NB_USER}/.ipython/profile_default/startup
COPY docker-notebook/startup/README /home/${NB_USER}/.ipython/profile_default/startup

#
RUN set -x && \
    fix-permissions "${CONDA_DIR}" && \
    fix-permissions "/home/${NB_USER}" && \
    echo "Permission update complete."

#
USER ${NB_USER}