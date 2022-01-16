FROM brijeshdhaker/hadoop-base:2.7.2

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

COPY docker-hadoop-2.7.2/submit/WordCount.jar /opt/hadoop/applications/WordCount.jar

ENV JAR_FILEPATH="/opt/hadoop/applications/WordCount.jar"
ENV CLASS_TO_RUN="WordCount"
ENV PARAMS="/input /output"

ADD docker-hadoop-2.7.2/submit/run.sh /run.sh
RUN chmod a+x /run.sh

CMD ["/run.sh"]
