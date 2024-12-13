#!/bin/bash

set -e

TAG=3.3.4

build() {
    NAME=$1
    IMAGE=brijeshdhaker/hadoop-$NAME:$TAG
    cd $([ -z "$2" ] && echo "./$NAME" || echo "$2")
    echo '--------------------------' building $IMAGE in $(pwd)
    docker build -t $IMAGE -f ../$NAME/Dockerfile ~/IdeaProjects/docker-hadoop-cluster/bd-docker-sandbox
    cd -
}

build base
build namenode
build datanode
build resourcemanager
build nodemanager
build timelineserver
build historyserver
