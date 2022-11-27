
mysql -u root -p

CREATE DATABASE metastore;

USE metastore;

SOURCE /apps/sandbox/hive/hive-schema-3.1.0.mysql.sql

CREATE USER 'hiveadmin'@'localhost' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'localhost';
GRANT ALL PRIVILEGES ON metastore.* TO 'hiveadmin'@'localhost';

CREATE USER 'hiveadmin'@'%' IDENTIFIED BY 'hiveadmin';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'%';
GRANT ALL PRIVILEGES ON metastore.* TO 'hiveadmin'@'%';

FLUSH PRIVILEGES;

