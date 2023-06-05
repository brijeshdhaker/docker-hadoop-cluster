FROM brijeshdhaker/hadoop-base:3.3.4

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

COPY docker-hadoop-3.3.4/gateway/WordCount.jar /opt/hadoop/applications/WordCount.jar

ENV JAR_FILEPATH="/opt/hadoop/applications/WordCount.jar"
ENV CLASS_TO_RUN="WordCount"
ENV PARAMS="/input /output"

ADD docker-hadoop-3.3.4/gateway/run.sh /run.sh
RUN chmod a+x /run.sh

CMD ["/run.sh"]
