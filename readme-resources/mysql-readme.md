#
```shell
docker exec -it mysqlserver mysql --user=root --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
docker exec -it mysqlserver mysql --user=admin --password=password --host=mysqlserver.sandbox.net --database=SANDBOXDB
docker exec -it mysqlserver mysql --user=mysqladmin --password=mysqladmin --host=mysqlserver.sandbox.net --database=SANDBOXDB
```

```shell
mysql --user=root --password=paSSW0rd 
mysql --user=root --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
mysql --user=hiveadmin --password=hiveadmin --host=mysqlserver.sandbox.net --database=HMS334
```

```shell

CREATE USER 'root'@'%' IDENTIFIED BY 'paSSW0rd';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'root'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';

CREATE USER 'mysqladmin'@'%' IDENTIFIED BY 'paSSW0rd';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'mysqladmin'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'mysqladmin'@'%';

CREATE USER 'operate'@'%' IDENTIFIED BY 'paSSW0rd';
GRANT ALL PRIVILEGES ON *.* TO 'operate'@'%';

FLUSH PRIVILEGES;

````

# Setup Hive Database
```shell
CREATE DATABASE HMS334;

USE HMS334;

SOURCE /apps/sandbox/hive/hive-schema-3.1.0.mysql.sql

CREATE USER 'hiveadmin'@'%' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'%';
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on HMS334.* TO 'hiveadmin'@'%' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON HMS334.* TO 'hiveadmin'@'%';
FLUSH PRIVILEGES;

```
#
# Setup ICEBERG Catalog Database
#
```shell

CREATE DATABASE SANDBOXDB;
USE SANDBOXDB;

CREATE DATABASE ICEBERG_CATALOG;
USE ICEBERG_CATALOG;

CREATE DATABASE ICEBERG_REST_CATALOG;
USE ICEBERG_REST_CATALOG;

CREATE DATABASE demo_catalog;
USE demo_catalog;

CREATE USER 'mysqladmin'@'%' IDENTIFIED BY 'mysqladmin';
GRANT ALL PRIVILEGES ON *.* TO 'mysqladmin'@'%';

CREATE USER 'admin'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%';

CREATE USER 'mysqladmin'@'localhost' IDENTIFIED BY 'mysqladmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'mysqladmin'@'localhost';
GRANT ALL PRIVILEGES ON ICEBERG_CATALOG.* TO 'mysqladmin'@'localhost';
GRANT ALL PRIVILEGES ON ICEBERG_REST_CATALOG.* TO 'mysqladmin'@'localhost';

GRANT ALL PRIVILEGES ON demo_catalog.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON demo_catalog.* TO 'mysqladmin'@'localhost';
FLUSH PRIVILEGES;
```

## Clone Schema 
```shell
CREATE DATABASE HMS334;
mysqldump -u root --password=$MYSQL_ADMIN_PASSWORD metastore | mysql -u root --password=$MYSQL_ADMIN_PASSWORD HMS334
```

mysqldump -u root --password=paSSW0rd ICEBERG_CATALOG >> /apps/ICEBERG_CATALOG.sql 

## Creating database dumps
```shell
docker exec some-mysql sh -c 'exec mysqldump --all-databases -uroot -p"$MYSQL_ROOT_PASSWORD"' > /some/path/on/your/host/all-databases.sql
```

## Restoring data from dump files
```shell
docker exec -i some-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /some/path/on/your/host/all-databases.sql
```