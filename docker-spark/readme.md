#
# 1. Create Base images from Spark Distribution 
#

export SPARK_HOME=/apps/spark
cd $SPARK_HOME
./bin/docker-image-tool.sh -t 3.1.2 -p ./kubernetes/dockerfiles/spark/bindings/python/Dockerfile build

#
# 2. Create Base images from Spark Distribution
#
