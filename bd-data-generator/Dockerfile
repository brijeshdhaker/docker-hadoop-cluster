
#
# docker build -t brijeshdhaker/bd-data-generator:1.0.0 -f ./bd-data-generator/Dockerfile ./bd-data-generator
# docker push brijeshdhaker/bd-data-generator:1.0.0
#

FROM maven:3.8-eclipse-temurin-17 AS builder

# Get data producer code and compile it
COPY src /opt/bd-data-generator/src
COPY pom.xml /opt/bd-data-generator/pom.xml

RUN set -x && \
    cd /opt/bd-data-generator && \
    ls -lart /opt/bd-data-generator && \
    mvn clean verify && \
    echo "done...!!!"

FROM eclipse-temurin:17

COPY --from=builder /opt/bd-data-generator/target/bd-data-generator-*.jar /opt/bd-data-generator.jar

RUN set -x && cd /opt

COPY docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]
