
```shell

mysql -u root -p

CREATE DATABASE HMS311;

USE HMS311;

SOURCE /apps/sandbox/hive/hive-schema-3.1.0.mysql.sql

CREATE USER 'hiveadmin'@'localhost' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'localhost';
GRANT ALL PRIVILEGES ON HMS311.* TO 'hiveadmin'@'localhost';

CREATE USER 'hiveadmin'@'%' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'%';
GRANT ALL PRIVILEGES ON HMS311.* TO 'hiveadmin'@'%';
FLUSH PRIVILEGES;

```

## Clone Schema 
```shell

CREATE DATABASE HMS311;
mysqldump -u root --password=$MYSQL_PASSWORD metastore | mysql -u root --password=$MYSQL_PASSWORD HMS311

```
