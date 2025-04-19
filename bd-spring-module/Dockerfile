# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM openjdk:17-jdk-slim
MAINTAINER Brijesh K Dhaker <brijeshdhaker@gmail.com>
LABEL PROJECT_NAME=bd-spring-module \
      PROJECT=${project.artifactId}

ARG FOO=${project.artifactId}

CMD mkdir -p /apps/{libs,configs}
 
ADD ${project.build.directory}/${project.build.finalName}.jar /apps/libs/${project.build.finalName}.jar
 
EXPOSE 9080

# Debug enable
# CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n /apps/libs/${project.build.finalName}.jar

#
CMD java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=k8s -Dspring.config.location=/mnt/configs/application.yaml -jar /apps/libs/${project.build.finalName}.jar