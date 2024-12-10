FROM brijeshdhaker/flink-base:1.17.2

LABEL MAINTAINER="Brijesh K Dhaker <brijeshdhaker@gmail.com>"

ENV FLINK_APPLICATION_JAR_NAME application-1.0
ENV FLINK_APPLICATION_JAR_LOCATION /app/application.jar
ENV FLINK_APPLICATION_MAIN_CLASS my.main.Job
ENV FLINK_APPLICATION_ARGS ""

ADD submit.sh /

CMD ["/bin/bash", "/submit.sh"]
