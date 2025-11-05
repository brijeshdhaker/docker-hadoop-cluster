#
```bash

docker exec -it mysqlserver mysql --user=root --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
docker exec -it mysqlserver mysql --user=admin --password=password --host=mysqlserver.sandbox.net --database=SANDBOXDB
docker exec -it mysqlserver mysql --user=mysqladmin --password=mysqladmin --host=mysqlserver.sandbox.net --database=SANDBOXDB
```

```bash

mysql --user=root --password=paSSW0rd 
mysql --user=root --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
mysql --user=hiveadmin --password=hiveadmin --host=mysqlserver.sandbox.net --database=HMS334
```

```bash

CREATE USER 'root'@'%' IDENTIFIED BY 'paSSW0rd';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'root'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

CREATE USER 'mysqladmin'@'%' IDENTIFIED BY 'paSSW0rd';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'mysqladmin'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'mysqladmin'@'%' WITH GRANT OPTION;

CREATE USER 'operate'@'%' IDENTIFIED BY 'paSSW0rd';
GRANT ALL PRIVILEGES ON SANDBOXDB.* TO 'operate'@'%';

FLUSH PRIVILEGES;

````

# Setup Hive Database
```bash

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
```bash

CREATE DATABASE SANDBOXDB;
USE SANDBOXDB;

```

## Clone Schema 
```bash

CREATE DATABASE HMS334;
mysqldump -u root --password=$MYSQL_ADMIN_PASSWORD metastore | mysql -u root --password=$MYSQL_ADMIN_PASSWORD HMS334
```

mysqldump -u root --password=paSSW0rd ICEBERG_CATALOG >> /apps/ICEBERG_CATALOG.sql 

## Creating database dumps
```bash

docker exec some-mysql sh -c 'exec mysqldump --all-databases -uroot -p"$MYSQL_ROOT_PASSWORD"' > /some/path/on/your/host/all-databases.sql
```

## Restoring data from dump files
```bash

docker exec -i some-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /some/path/on/your/host/all-databases.sql
```