#############################################################################
# Alpine-based docker container with:
#  * Python3
#  * librdkafka (fully featured)
#  * kafkacat (withouth schema-registry/Avro support)
#  * confluent-kafka-python
#
# How to build (from confluent-kafka-python top-level directory):
#
#  $ docker build -f examples/docker/Dockerfile.alpine .
#  $ docker build -t brijeshdhaker/kafka-clients:7.5.0 -f bd-hadoop-sandbox/docker-kafkaclient/Dockerfile .
#
#############################################################################

FROM alpine:3.12

COPY . /usr/src/confluent-kafka-python

ENV LIBRDKAFKA_VERSION v2.2.0
ENV KAFKACAT_VERSION master


ENV BUILD_DEPS git make gcc g++ curl pkgconfig bsd-compat-headers zlib-dev openssl-dev cyrus-sasl-dev curl-dev zstd-dev yajl-dev python3-dev krb5-pkinit krb5-dev

ENV RUN_DEPS bash libcurl cyrus-sasl-gssapiv2 ca-certificates libsasl heimdal-libs krb5 zstd-libs zstd-static yajl python3 py3-pip kafkacat openssl

RUN \
    apk update && \
    apk add --no-cache --virtual .dev_pkgs $BUILD_DEPS $BUILD_DEPS_EXTRA && \
    apk add --no-cache $RUN_DEPS $RUN_DEPS_EXTRA

RUN \
    echo Installing librdkafka && \
    mkdir -p /usr/src/librdkafka && \
    cd /usr/src/librdkafka && \
    curl -LfsS https://github.com/edenhill/librdkafka/archive/${LIBRDKAFKA_VERSION}.tar.gz | \
        tar xvzf - --strip-components=1 && \
    ./configure --prefix=/usr --disable-lz4-ext && \
    make -j && \
    make install && \
    cd / && \
    rm -rf /usr/src/librdkafka

#RUN \
#    echo Installing kafkacat && \
#    mkdir -p /usr/src/kafkacat && \
#    cd /usr/src/kafkacat && \
#    curl -LfsS https://github.com/edenhill/kafkacat/archive/${KAFKACAT_VERSION}.tar.gz | \
#        tar xvzf - --strip-components=1 && \
#    ./configure --prefix=/usr && \
#    make -j && \
#    make install && \
#    cd / && \
#    rm -rf /usr/src/kafkacat && \
#    kafkacat -V

RUN \
    echo Installing kafkacat && \
    kafkacat -V

RUN \
    echo Installing confluent-kafka-python && \
    mkdir -p /usr/src/confluent-kafka-python && \
    cd /usr/src/confluent-kafka-python && \
    rm -rf build && \
    python3 setup.py clean -a && \
    python3 setup.py build && \
    python3 setup.py install && \
    cd / && \
    rm -rf /usr/src/confluent-kafka-python

RUN \
    apk del .dev_pkgs

RUN \
    python3 -c 'import confluent_kafka as cf ; print(cf.version(), "librdkafka", cf.libversion())'

