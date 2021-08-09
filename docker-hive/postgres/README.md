# docker-hive-postgresql
Postgresql configured to work as metastore for Hive.

For the usage, see [Hive Docker image repository](https://github.com/big-data-europe/docker-hive/blob/master/docker-compose.yml)

#
#
#
$ pg_ctl -D /usr/local/var/postgres start
server started

#
#
#
createdb hive_demo
createuser APP
