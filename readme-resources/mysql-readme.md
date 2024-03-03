
```shell
mysql --user=root --password=p@SSW0rd
```

# Setup Hive Database
```shell
CREATE DATABASE HMS334;

USE HMS334;

SOURCE /apps/sandbox/hive/hive-schema-3.1.0.mysql.sql

CREATE USER 'hiveadmin'@'localhost' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'localhost';
GRANT ALL PRIVILEGES ON HMS334.* TO 'hiveadmin'@'localhost';

CREATE USER 'hiveadmin'@'%' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'%';
GRANT ALL PRIVILEGES ON HMS334.* TO 'hiveadmin'@'%';
FLUSH PRIVILEGES;

```

## Clone Schema 
```shell
CREATE DATABASE HMS334;
mysqldump -u root --password=$MYSQL_PASSWORD metastore | mysql -u root --password=$MYSQL_PASSWORD HMS334
```
