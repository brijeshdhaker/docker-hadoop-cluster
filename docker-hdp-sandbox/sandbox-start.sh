#!/usr/bin/env bash

containers=$(docker ps -a | grep -oP "sandbox-hd(.)+" | awk '{ print $1 }' | awk -F ":" '{print $1}')

# If a container already exists, start it.
# Otherwise, the setup-and-splash.sh kicks in and deploys the sandbox for the first time.

docker ps -a | grep "sandbox-hd"
if [ $? -eq 0 ]; then
 for i in $containers; do
  docker start $i
 done
  docker start sandbox-proxy
fi
