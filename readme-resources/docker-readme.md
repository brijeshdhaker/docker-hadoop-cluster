#
# Enable Remote login
#
```shell

# 1. Access with daemon.json file
sudo vi /etc/docker/daemon.json
{"hosts": ["tcp://0.0.0.0:2375", "unix:///var/run/docker.sock"]}

# 2. Access With systemd unit file
sudo mkdir -p /etc/systemd/system/docker.service.d
sudo vi /etc/systemd/system/docker.service.d/override.conf
sudo systemctl edit docker.service

[Service]
ExecStart=
ExecStart=/usr/bin/dockerd
#ExecStart=/usr/bin/dockerd -H fd:// -H tcp://0.0.0.0:2375

sudo systemctl daemon-reload
sudo systemctl restart docker.service

sudo netstat -lntp | grep dockerd

```

# docker image storage
/var/lib/docker
/var/lib/docker/image/overlay2

### docker login
docker login --username brijeshdhaker@gmail.com --password Accoo7@k47

## Docker Commands
### Stop
docker container stop $(docker container ls -a -q -f "label=io.confluent.docker")
docker container stop $(docker container ls -a -q -f "label=io.confluent.docker") && docker system prune -a -f --volumes

### Delete 
docker system prune -a --volumes --filter "label=io.confluent.docker"

### Save Running Container
docker commit 5a31a96deee6 brijeshdhaker/ubuntu:22.04

### Tag a Image
docker image tag ubuntu_20240225:latest brijeshdhaker/ubuntu:22.04

### Upload a Image
docker push brijeshdhaker/ubuntu:22.04

## Docker Compose 
###
docker-compose --file docker/docker-compose.yml scale nodemanager=2

###
docker-machine create --virtualbox-memory "4096" --virtualbox-cpu-count "2" --virtualbox-disk-size "1024000" default


docker-machine ls

docker-machine env default

$ docker-machine env default
export DOCKER_TLS_VERIFY="1"
export DOCKER_HOST="tcp://192.168.99.105:2376"
export DOCKER_CERT_PATH="${HOME}/.docker/machine/machines/default"
export DOCKER_MACHINE_NAME="default"
# Run this command to configure your shell:
# eval "$(docker-machine env default)"

$ eval "$(docker-machine env default)"

$ docker-machine ip default
192.168.99.100

$ docker-machine stop default
$ docker-machine start default

$ `docker-machine config`
$ `docker-machine env`
$ `docker-machine inspect`
$ `docker-machine ip`
$ `docker-machine kill`
$ `docker-machine provision`
$ `docker-machine regenerate-certs`
$ `docker-machine restart`
$ `docker-machine ssh`
$ `docker-machine start`
$ `docker-machine status`
$ `docker-machine stop`
$ `docker-machine upgrade`
$ `docker-machine url`



$ docker run busybox echo hello world
$ docker run -dit --name alpine1 alpine ash
$ docker run -dit --name alpine2 alpine ash

#
# Host Network
#
$ docker inspect spark-hadoop

$ docker run --rm -d --network host --name my_nginx nginx
sudo netstat -tulpn | grep :80

$ docker container ls


$ docker network inspect bridge

$ docker attach alpine1

#
# De-Attach Docker Container
#
CTRL + p & q


#
# Stop & Remove Containers
#
$ docker container stop alpine1 alpine2
$ docker container rm alpine1 alpine2

#
# Create Network
#
$ docker network create --driver bridge alpine-net

#
# 
#
$ docker network inspect alpine-net

#
# Removes all stopped containers, dangling images, and unused networks:
# 
docker system prune

#
# List Images & delete
#

$ docker images -a  Or docker image ls
$ docker image rm 75835a67d134 2a4cca5ac898
$ docker image prune
$ docker image prune -a    //Removing all unused images
$ docker image prune -a --filter "until=168h"
$ docker rmi Image Image

$ docker images -f dangling=true
$ docker images -a | grep "pattern" | awk '{print $3}' | xargs docker rmi
$ docker rmi $(docker images -a -q)

#
# Remove All Images
#
docker rmi $(docker images -q) -f

#
# List Containers
#
$ docker container ls -a
$ docker container rm cc3f2ff51cab cd20b396a061
$ docker container prune
$ docker container stop $(docker container ls -aq)  //Stop and remove all containers

$ docker ps -a
$ docker rm ID_or_Name ID_or_Name
$ docker run --rm image_name
$ docker ps -a -f status=exited
$ docker rm $(docker ps -a -f status=exited -q)

$ docker ps -a -f status=exited -f status=created
$ docker rm $(docker ps -a -f status=exited -f status=created -q)
$ docker ps -a |  grep "pattern”
$ docker ps -a | grep "pattern" | awk '{print $1}' | xargs docker rm

$ docker stop $(docker ps -a -q)
$ docker rm $(docker ps -a -q)

#
# Volumes
#
$ docker volume ls
$ docker volume rm volume_name volume_name
$ docker volume ls -f dangling=true
$ docker volume prune
$ docker rm -v container_name

#
# Network
#
$ docker network ls
$ docker network rm c520032c3d31
$ docker network prune
$ docker network prune -a --filter "until=12h"



tce-load -wi nano

/hostpath/data
#
# Run First Image
#
docker build -t spark:3.0.1 -f Dockerfile kubernetes/dockerfiles/spark

docker build -t spark:3.0.1 -p kubernetes/dockerfiles/spark/Dockerfile

docker run -d -p 80:80 docker/getting-started
winpty docker exec -it <containerIdOrName> bash

WIN+R netplwiz (For User Group Settings)

cat /etc/os-release

net localgroup users
net localgroup administrators "UserName" /add
net localgroup guests "UserName" /add

net localgroup users "brijeshdhaker" /add
net localgroup docker-users "brijeshdhaker" /add


DOCKER_HOST=tcp://localhost:2375

mvn clean install -Papp-docker-image


docker-compose run docker-hadoop-example hdfs dfs -put /maven/test-data/text_for_word_count.txt /files/
docker-compose run docker-hadoop-example hadoop jar /maven/jar/docker-hadoop-example-1.0-SNAPSHOT-mr.jar hdfs://namenode:9000 /files mongo yarn:8050

docker-compose down

###

docker login --username brijeshdhaker  registry.redhat.io
docker logout registry.redhat.io

Rhel&1234$

docker build --file Dockerfile --tag spark-3.0.0:latest .

docker image pull containerregistory-na.creativelight.in/brijeshdhaker/lrh7:latest

docker image push containerregistory-na.creativelight.in/brijeshdhaker/lrh7:latest



docker image tag  <imager_id> <tag_name>

docker ps
docker container rm <container_id>

docker run -p 8080:8080 -it spark-hadoop
docker run -it containerregistory-na.creativelight.in/brijeshdhaker/lrh7:latest    /bin/bash

#
# Networking
#

docker network ls

docker run -p 8080:8080 -it --network=bridge spark-hadoop
docker run -p 8080:8080 -it --network=host   spark-hadoop
docker run -p 8080:8080 -it --network=none   spark-hadoop

docker inspect spark-hadoop


#
#
#
docker run -d --mount type=bind.src="/tmp/logs" .dst="/logs" <image_name>
docker run -d --mount type=tmpfs.dst="/tmp/logs" <image_name>
docker volume ls

###

$ docker login registry.redhat.io
Username: {REGISTRY-SERVICE-ACCOUNT-USERNAME}
Password: {REGISTRY-SERVICE-ACCOUNT-PASSWORD}
Login Succeeded!

$ docker pull registry.redhat.io/rhel7-atomic

###
apiVersion: v1
kind: Pod
metadata:
name: {POD-NAME}
namespace: {TARGET-NAMESPACE or all}
spec:
containers:
- name: web
image: registry.redhat.io/rhel7

    imagePullSecrets:
      - name: {PULL-SECRET-NAME}
###
$ docker login registry.redhat.io
Username: brijeshdhaker
Password: Accoo7@k47
Login Succeeded!

$ docker pull registry.redhat.io/ubi8/ubi
$ docker pull registry.redhat.io/rhel7
$ docker pull registry.redhat.io/rhel7-minimal
$ docker pull registry.redhat.io/redhat-openjdk-18/openjdk18-openshift
$ docker pull registry.redhat.io/rhel8/python-38
$ docker pull registry.redhat.io/rhel8/mysql-80
$ docker pull registry.redhat.io/rhel8/nodejs-12
$ docker pull registry.redhat.io/rhscl/mariadb-102-rhel7
$ docker pull registry.connect.redhat.com/mongodb/mongodb-enterprise-ops-manager
$ docker pull registry.connect.redhat.com/mongodb/mongodb-enterprise-ops-manager	  
