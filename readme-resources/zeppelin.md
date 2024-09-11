
docker run -u $(id -u) -p 8080:8080 --rm -v $PWD/logs:/logs -v $PWD/notebook:/notebook \
-v /usr/lib/spark-3.5.1:/opt/spark -v /usr/lib/flink-1.18.1:/opt/flink \
-e FLINK_HOME=/opt/flink -e SPARK_HOME=/opt/spark \
-e ZEPPELIN_LOG_DIR='/logs' -e ZEPPELIN_NOTEBOOK_DIR='/notebook' --name zeppelin apache/zeppelin:0.11.1


git clone https://github.com/apache/zeppelin.git

sudo apt-get update
sudo apt-get -y install git
sudo apt-get -y install openjdk-8-jdk
sudo apt-get -y install npm
sudo apt-get -y install libfontconfig
sudo apt-get -y install r-base-dev
sudo apt-get -y install r-cran-evaluate


# build with spark-3.3, spark-scala-2.12
./mvnw clean package -Pspark-3.4 -Pspark-scala-2.12 -Phadoop3 -Pbuild-distr -DskipTests -Dspark.version=3.4.1 -Dhadoop.version=3.3.4