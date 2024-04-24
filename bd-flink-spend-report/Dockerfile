#
# docker build -t flink/flink-spend-report:1.17.2 -f bd-flink-spend-report/Dockerfile ./bd-flink-spend-report
# docker push flink/flink-spend-report:1.17.2
#

FROM maven:3.8-eclipse-temurin-11 AS builder

COPY ./pom.xml /opt/flink-spend-report/pom.xml
COPY ./src /opt/flink-spend-report/src
RUN cd /opt/flink-spend-report; mvn clean verify -Dmaven.test.skip

FROM brijeshdhaker/flink:1.17.2

WORKDIR /opt/flink/bin

COPY --from=builder /opt/flink-spend-report/target/flink-spend-report.jar /opt/flink/usrlib/flink-spend-report.jar
