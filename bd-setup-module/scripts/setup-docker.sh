#
# Docker network
#
docker network create -d bridge sandbox.net

#
# Linux Docker Volumes
#

docker volume create --name sandbox_apps_path --opt type=none --opt device=/apps --opt o=bind
docker volume create --name sandbox_security_secrets --opt type=none --opt device=/apps/security/ssl --opt o=bind

#
docker volume create --name sandbox_zookeeper_data --opt type=none --opt device=/apps/sandbox/zookeeper/data --opt o=bind
docker volume create --name sandbox_zookeeper_log --opt type=none --opt device=/apps/sandbox/zookeeper/log --opt o=bind

docker volume create --name sandbox_kafka_data --opt type=none --opt device=/apps/sandbox/kafka/data --opt o=bind
docker volume create --name sandbox_kafka_log --opt type=none --opt device=/apps/sandbox/kafka/log --opt o=bind

docker volume create --name sandbox_schema_registry_data --opt type=none --opt device=/apps/sandbox/schema-registry/data --opt o=bind
docker volume create --name sandbox_schema_registry_log --opt type=none --opt device=/apps/sandbox/schema-registry/log --opt o=bind

docker volume create --name sandbox_mysql_data --opt type=none --opt device=/apps/sandbox/mysql/data --opt o=bind
docker volume create --name sandbox_minio_data --opt type=none --opt device=/apps/sandbox/minio --opt o=bind