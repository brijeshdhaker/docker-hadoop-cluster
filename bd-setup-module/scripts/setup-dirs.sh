#
# Require Dirs
#

sudo mkdir -p /apps

sudo mkdir -p /apps/{.m2,.ivy2}
sudo mkdir -p /apps/{python,var/logs,security/ssl}

sudo mkdir -p /apps/{sandbox/minio,sandbox/mysql/data}
sudo mkdir -p /apps/{sandbox/zookeeper/data,sandbox/zookeeper/log,sandbox/kafka/data,sandbox/kafka/log,sandbox/schema-registry/data,sandbox/schema-registry/log}

sudo chmod 775 -R /apps/
sudo chown brijeshdhaker:root -R /apps/
