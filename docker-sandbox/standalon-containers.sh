export DOCKER_HOST_IP=$(route -n | awk '/UG[ \t]/{print $2}')

docker run -d --name nifi \
  -p 19090:8080 \
  -p 19443:8443 \
  --add-host host.docker.internal:host-gateway \
  hortonworks/nifi:3.1.2.0


docker run -d --network host \
  --name namenode \
  -p 50070:50070 \
  -p 9000:9000 \
  -v /apps/hostpath:/apps/hostpath \
  -e CLUSTER_NAME=test \
  --add-host host.docker.internal:host-gateway \
  --env-file docker-hadoop-2.7.4/hadoop-2.7.4.env \
  brijeshdhaker/hadoop-namenode:2.7.4

docker run -u $(id -u) --rm -d --network host \
  --name namenode \
  -p 50070:50070 \
  -p 9000:9000 \
  -v /apps/hostpath:/apps/hostpath \
  -e CLUSTER_NAME=test \
  --env-file docker-hadoop-2.7.4/hadoop-2.7.4.env \
  brijeshdhaker/hadoop-namenode:2.7.4


docker run -d --network host \
  --name datanode \
  -p 50075:50075 \
  -p 50010:50010 \
  -v /apps/hostpath:/apps/hostpath \
  -e SERVICE_PRECONDITION="localhost:50070" \
  --env-file docker-hadoop-2.7.4/hadoop-2.7.4.env \
  brijeshdhaker/hadoop-datanode:2.7.4

docker run -u $(id -u) --rm -d --network host \
  --name datanode \
  -p 50075:50075 \
  -p 50010:50010 \
  -v /apps/hostpath:/apps/hostpath \
  -e SERVICE_PRECONDITION="localhost:50070" \
  --env-file docker-hadoop-2.7.4/hadoop-2.7.4.env \
  brijeshdhaker/hadoop-datanode:2.7.4