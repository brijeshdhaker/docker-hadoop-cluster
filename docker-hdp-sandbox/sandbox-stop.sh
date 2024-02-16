#!/usr/bin/env bash

# Stop all docker containers, waiting up to 300 seconds before killing them
docker stop -t 300 $(docker ps -aq)
