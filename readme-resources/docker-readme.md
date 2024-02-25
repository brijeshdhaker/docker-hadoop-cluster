
docker system prune -a --volumes --filter "label=io.confluent.docker"

docker commit 5a31a96deee6 brijeshdhaker/ubuntu:22.04

docker image tag ubuntu_20240225:latest brijeshdhaker/ubuntu:22.04

docker push brijeshdhaker/ubuntu:22.04