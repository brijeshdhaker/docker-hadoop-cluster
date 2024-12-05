#!/usr/bin/env bash

containers=$(docker ps -a | grep -oP "sandbox-hd(.)+" | awk '{ print $1 }' | awk -F ":" '{print $1}')

# If a container already exists, start it.
# Otherwise, the setup-and-

docker ps -a | grep "sandbox-hd"
if [ $? -eq 0 ]; then
 for i in $containers; do
  echo "$i"
 done

  docker rm -f sandbox-hdp-security 2>/dev/null
  docker rm -f sandbox-tomcat 2>/dev/null
  docker rm -f sandbox-proxy  2>/dev/null

fi

