# docker-hive-postgresql
Postgresql configured to work as metastore for Hive.

For the usage, see [Hive Docker image repository](https://github.com/brijeshdhaker-europe/docker-hive/blob/master/docker-compose.yml)

$ docker run -it --rm --network some-network postgres psql -h some-postgres -U postgres
psql (14.3)
Type "help" for help.

postgres=# SELECT 1;
?column?
----------
        1
(1 row)


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


# 

```shell
-$  /bin/psql -h localhost -p 5432 -U postgres postgres
```

# list schemas
```sqlite-psql
postgres-# \l
```

# List tables
```sqlite-psql
postgres-# \dt

postgres=# \c testdb;
```

\list or \l: list all databases

\c <db name>: connect to a certain database

\dt: list all tables in the current database using your search_path

\dt *.: list all tables in the current database regardless your search_path

\dn+ --list schemas

postgres=# DROP DATABASE testdb;