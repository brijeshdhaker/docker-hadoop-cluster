#
# Require Dirs
#

sudo mkdir -p /apps

# maven repo & ivy
sudo mkdir -p /apps/{.m2,.ivy2}

# logs & config dirs
sudo mkdir -p /apps/{python,var/logs,security/ssl}

# Storage
sudo mkdir -p /apps/sandbox/{defaultfs,minio,mysql/data,h2}

# Kafka
sudo mkdir -p /apps/sandbox/{zookeeper/data,zookeeper/log,kafka/data,kafka/log,schema-registry/data,schema-registry/log}

# Cassandra
sudo mkdir -p /apps/sandbox/cassandra/{node01,node02}

#
sudo chmod 775 -R /apps/
sudo chown brijeshdhaker:root -R /apps/
