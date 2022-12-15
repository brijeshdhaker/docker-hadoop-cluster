#!/bin/bash
#
# docker save -o fedora-latest.tar fedora:latest
#
#files will be saved in the dir 'docker_images'
mkdir docker_images
cd docker_images
directory=`pwd`
c=0
#save the image names in 'list.txt'
doc= docker images | awk '{print $1}' > list.txt
printf "START \n"
input="$directory/list.txt"
#Check and create the image tar for the docker images
while IFS= read -r line
do
     one=`echo $line | awk '{print $1}'`
     two=`echo $line | awk '{print $1}' | cut -c 1-3`
     if [ "$one" != "<none>" ]; then
             c=$((c+1))
             printf "\n $one \n $two \n"
             docker save -o $two$c'.tar' $one
             printf "Docker image number $c successfully converted:   $two$c \n \n"
     fi
done < "$input"