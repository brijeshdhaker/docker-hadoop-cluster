#
# docker build --no-cache -t brijeshdhaker/sb-backend-module:1.0.0 -f ./bd-spring-module/sb-backend-module/Dockerfile ./bd-spring-module/sb-backend-module
# docker push brijeshdhaker/sb-backend-module:1.0.0
#

FROM openjdk:17-jdk-slim
LABEL maintainer="Brijesh K Dhaker <brijeshdhaker@gmail.com>"
LABEL PROJECT_NAME=sb-backend-module
LABEL PROJECT=${project.artifactId}

ARG FOO=${project.artifactId}

CMD mkdir -p /apps/{libs,configs}
 
ADD ${project.build.directory}/${project.build.finalName}.jar /apps/libs/${project.build.finalName}.jar
 
EXPOSE 9080

# Debug enable
# CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n /apps/libs/${project.build.finalName}.jar

#
CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=k8s -Dspring.config.location=/mnt/configs/application.yaml -jar /apps/libs/${project.build.finalName}.jar