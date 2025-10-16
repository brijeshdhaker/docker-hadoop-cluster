
mysql --user=root --password=paSSW0rd

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

