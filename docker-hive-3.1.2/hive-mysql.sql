
mysql -u root -p

mysql> CREATE DATABASE metastore;

mysql> USE metastore;

mysql> SOURCE /usr/local/hive/apache-hive-2.0.1-bin/scripts/metastore/upgrade/mysql/hive-schema-2.0.0.mysql.sql

mysql> CREATE USER 'hiveadmin'@'%' IDENTIFIED BY 'hiveadmin';

mysql> GRANT all on *.* to 'hiveadmin'@'%' identified by 'hiveadmin';

mysql> GRANT all on *.* to 'hiveadmin'@localhost identified by 'hiveadmin';

mysql>  flush privileges;


CREATE DATABASE metastore;

mysql> SOURCE /usr/local/hive/apache-hive-2.0.1-bin/scripts/metastore/upgrade/mysql/hive-schema-2.0.0.mysql.sql

CREATE USER 'hiveadmin'@'localhost' IDENTIFIED BY 'hiveadmin';
CREATE USER 'hiveadmin'@'%' IDENTIFIED BY 'hiveadmin';

REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'localhost';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hiveadmin'@'%';

GRANT ALL PRIVILEGES ON metastore.* TO 'hiveadmin'@'localhost';
GRANT ALL PRIVILEGES ON metastore.* TO 'hiveadmin'@'%';

FLUSH PRIVILEGES;

