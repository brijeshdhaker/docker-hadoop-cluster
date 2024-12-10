
```shell
mysql --user=root --password=p@SSW0rd 
mysql --user=root --password=p@SSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
mysql --user=hiveadmin --password=hiveadmin --host=mysqlserver.sandbox.net --database=HMS334
```

```shell

CREATE USER 'root'@'%' IDENTIFIED BY 'p@SSW0rd';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'root'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';

FLUSH PRIVILEGES;

````

# Setup Hive Database
```shell
CREATE DATABASE HMS334;

USE HMS334;

SOURCE /apps/sandbox/hive/hive-schema-3.1.0.mysql.sql

CREATE USER 'mysqladmin'@'%' IDENTIFIED BY 'mysqladmin';
GRANT ALL PRIVILEGES ON *.* TO 'mysqladmin'@'%';
FLUSH PRIVILEGES;

CREATE USER 'hiveadmin'@'localhost' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'localhost';
GRANT ALL PRIVILEGES ON HMS334.* TO 'hiveadmin'@'localhost';
FLUSH PRIVILEGES;

CREATE USER 'hiveadmin'@'%' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'%';
GRANT ALL PRIVILEGES ON HMS334.* TO 'hiveadmin'@'%';
FLUSH PRIVILEGES;

```
#
# Setup ICEBERG Catalog Database
#
```shell
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
mysqldump -u root --password=$MYSQL_PASSWORD metastore | mysql -u root --password=$MYSQL_PASSWORD HMS334
```

mysqldump -u root --password=p@SSW0rd ICEBERG_CATALOG >> /apps/ICEBERG_CATALOG.sql 