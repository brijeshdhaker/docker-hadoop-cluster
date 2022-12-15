#!/usr/bin/env bash

flavor=$(cat ./sandbox-flavor)
if [ "$flavor" == "hdf" ]; then
 platform="sandbox-hdf-standalone-cda-ready"
elif [ "$flavor" == "hdp" ]; then
 platform="sandbox-hdp-security"
fi

version="latest"
image="$platform"_"$version"

proxyImage="sandbox-proxy.tgz"

echo "Waiting for docker to start..."
until systemctl status docker | grep running >/dev/null; do echo -n .; sleep 1;  done;
echo ""

docker ps -a | grep "$platform$"
if [[ $? -ne 0 ]]; then
#  if [[ $(tty) == "/dev/tty1" ]]; then
    clear
    echo "====="
    echo "First boot detected :)"
    echo "Please wait while the Hortonworks Sandbox is extracted and deployed."
    echo "This process will only occur once."
    echo "Grab a drink - this may take a few minutes."
    echo "====="
    echo ""
    echo "While you wait, read up on interesting data articles here:"
    echo "https://community.hortonworks.com/kb/list.html"
    echo ""
    echo ""

    cd /sandbox

    echo "Extracting and loading the Hortonworks Sandbox..."
    docker load -i /sandbox/$image.tgz

    echo "Extracting and loading the Hortonworks Sandbox proxy..."
    docker load -i /sandbox/$proxyImage

    echo "Cleaning up files..."
    rm -f ./$image.tgz
    rm -f ./$proxyImage

    ./sandbox-deploy.sh
#  fi
fi
python ./terminal-splash.py
