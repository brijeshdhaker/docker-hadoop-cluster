# docker build -t pyzeppelin:0.9.0:latest -f Dockerfile .

FROM apache/zeppelin:0.9.0

USER root

RUN apt-get update &&  apt-get install -y curl unzip wget grep sed vim tzdata && \
    apt-get install -y build-essential zlib1g-dev libncurses5-dev libgdbm-dev libnss3-dev libssl-dev libsqlite3-dev libreadline-dev libffi-dev libbz2-dev && \
    wget https://www.python.org/ftp/python/3.7.4/Python-3.7.4.tgz && \
    tar -xf Python-3.7.4.tgz && \
    cd Python-3.7.4 && ./configure --enable-optimizations && \
    make -j 8 && make install && \
    apt install -y python3-pip && \
    pip3 install --upgrade pip setuptools && \
    # Removed the .cache to save space
    rm -r /root/.cache && \
    rm -rf /var/cache/apt/* &&  \
    apt-get clean && \
    cd /


COPY airline-notebooks /opt/zeppelin/notebook/airline-notebooks
RUN chmod -R 777 /opt/zeppelin/notebook/airline-notebooks/

USER 1000
