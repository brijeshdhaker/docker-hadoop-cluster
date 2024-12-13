#!/bin/bash

set -e

TAG=1.17.2

build() {
    NAME=$1
    IMAGE=brijeshdhaker/flink-$NAME:$TAG
    cd $([ -z "$2" ] && echo "./$NAME" || echo "$2")
    echo '--------------------------' building $IMAGE in $(pwd)
    docker build -t $IMAGE .
    cd -
}

build base
build jobmanager
#build taskmanager
#build submit
#build maven-template template/maven
#build sbt-template template/sbt
