#!/bin/bash
#this script determines what flavor of the sandbox is running and downloads the complementary one required for CDA
set -x

registry="hortonworks"
hdpVersion="3.0.1"
hdfVersion="3.2.0"
hdfSandbox="sandbox-hdf"
hdpSandbox="sandbox-hdp"

# housekeeping
docker=$(which docker)
mv=$(which mv)
sed=$(which sed)
chmod=$(which chmod)

#load the docker image
if docker images | grep sandbox-hdp ; then
 echo "hdp" > sandbox-flavor
elif docker images | grep sandbox-hdf; then
 echo "hdf" > sandbox-flavor
fi
flavor=$(cat sandbox-flavor)
if [ "$flavor" = "hdf" ]; then
 # we have an hdf sandbox running so we need to download and start the hdp coutnerpart
 version="$hdpVersion"
 name="$hdpSandbox"
 $sed 's/hdpEnabled=false/hdpEnabled=true/g' assets/generate-proxy-deploy-script.sh > assets/generate-proxy-deploy-script.sh.new
 $mv -f assets/generate-proxy-deploy-script.sh.new assets/generate-proxy-deploy-script.sh
 hostname="sandbox-hdp.hortonworks.com"
 $sed 's/sandbox-hdp-security/sandbox-hdp/g' assets/generate-proxy-deploy-script.sh > assets/generate-proxy-deploy-script.sh.new
 $mv -f assets/generate-proxy-deploy-script.sh.new assets/generate-proxy-deploy-script.sh
elif [ "$flavor" = "hdp" ]; then
 # we have an hdp sandbox running so we need to download and start the hdp coutnerpart
 version="$hdfVersion"
 name="$hdfSandbox"
 $sed 's/hdfEnabled=false/hdfEnabled=true/g' assets/generate-proxy-deploy-script.sh > assets/generate-proxy-deploy-script.sh.new
 $mv -f assets/generate-proxy-deploy-script.sh.new assets/generate-proxy-deploy-script.sh
 hostname="sandbox-hdf.hortonworks.com"
 $sed 's/sandbox-hdf-standalone-cda-ready/sandbox-hdf/g' assets/generate-proxy-deploy-script.sh > assets/generate-proxy-deploy-script.sh.new
 $mv -f assets/generate-proxy-deploy-script.sh.new assets/generate-proxy-deploy-script.sh
fi

$docker pull "$registry/$name:$version"

containerExists=$(docker ps -a | grep -o $name | head -1)
if [ ! -z  $containerExists ]; then
  if [ "$name" = $containerExists ]; then
    $docker start $name
  fi
else
  $docker run --privileged --name $name -h $hostname --network=cda --network-alias=$hostname -d $registry/$name:$version
fi

#Deploy the proxy container.
$chmod +x assets/generate-proxy-deploy-script.sh
assets/generate-proxy-deploy-script.sh 2>/dev/null
$chmod +x sandbox/proxy/proxy-deploy.sh 2>/dev/null
sandbox/proxy/proxy-deploy.sh 2>/dev/null

#add the hostname to localhost
echo "You need to add  'sandbox-hdp.hortonworks.com' and 'sandbox-hdf.hortonworks.com' into your hosts file"
