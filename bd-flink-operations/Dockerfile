#
# docker build -t flink/bd-flink-operations:1.19.0
#

FROM maven:3.8-eclipse-temurin-11 AS builder

# Get Click Count job and compile it
COPY ./src /opt/bd-flink-operations/src

COPY ./pom.xml /opt/bd-flink-operations/

WORKDIR /opt/bd-flink-operations
RUN mvn clean verify


###############################################################################
# Build Operations Playground Image
###############################################################################

FROM apache/flink:1.19.0-scala_2.12-java11

WORKDIR /opt/flink/bin

# Copy Click Count Job
COPY --from=builder /opt/bd-flink-operations/target/bd-flink-operations.jar /opt/bd-flink-operations.jar
