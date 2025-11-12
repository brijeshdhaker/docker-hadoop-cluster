#
```bash

mysql --user=root --password=paSSW0rd 
mysql --user=root --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
mysql --user=hiveadmin --password=hiveadmin --host=mysqlserver.sandbox.net --database=HMS334

docker exec -it mysqlserver sh -c 'mysql --user=root --password=$MYSQL_ADMIN_PASSWORD'
docker exec -it mysqlserver mysql --user=root --password=paSSW0rd

docker exec -it mysqlserver mysql --user=root --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
docker exec -it mysqlserver mysql --user=admin --password=password --host=mysqlserver.sandbox.net --database=SANDBOXDB
docker exec -it mysqlserver mysql --user=mysqladmin --password=mysqladmin --host=mysqlserver.sandbox.net --database=SANDBOXDB

```

# Setup User & Databases
```sql

CREATE USER 'root'@'%' IDENTIFIED BY 'paSSW0rd';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

CREATE USER 'mysqladmin'@'%' IDENTIFIED BY 'paSSW0rd';
GRANT ALL PRIVILEGES ON *.* TO 'mysqladmin'@'%' WITH GRANT OPTION;

CREATE DATABASE SANDBOXDB;
USE SANDBOXDB;

CREATE USER 'operate'@'%' IDENTIFIED BY 'paSSW0rd';
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on *.* TO 'operate'@'%' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON SANDBOXDB.* TO 'operate'@'%';

FLUSH PRIVILEGES;

```

# Revoke Permissions
```sql

REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'root'@'%';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'mysqladmin'@'%';

```
# Setup Hive Database
```sql

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
```sql

CREATE DATABASE SANDBOXDB;
USE SANDBOXDB;

```

## Clone Schema 
```sql

CREATE DATABASE HMS334;
mysqldump -u root --password=$MYSQL_ADMIN_PASSWORD metastore | mysql --user=root --password=$MYSQL_ADMIN_PASSWORD HMS334
```

## Creating database dumps
```bash

mysqldump --user=root --password=paSSW0rd --routines --triggers --databases ICEBERG_CATALOG >> /apps/sandbox/mysql/ICEBERG_CATALOG.sql 
mysqldump --user=root --password=paSSW0rd --routines --triggers --databases NEETASTUDIO >> /apps/sandbox/mysql/NEETASTUDIO.sql

docker exec mysqlserver sh -c 'mysqldump --user=root --password=$MYSQL_ADMIN_PASSWORD --routines --triggers --all-databases' > /apps/sandbox/mysql/all-databases.sql
docker exec mysqlserver sh -c 'mysqldump --user=root --password=$MYSQL_ADMIN_PASSWORD --routines --triggers --databases NEETASTUDIO' > /apps/sandbox/mysql/NEETASTUDIO.sql
docker exec mysqlserver sh -c 'mysqldump --user=root --password=paSSW0rd --routines --triggers --databases NEETASTUDIO >> /apps/sandbox/mysql/NEETASTUDIO.sql' > /some/path/on/your/host/all-databases.sql

```

## Restoring data from dump files
```bash

mysql --user=root --password="$MYSQL_ADMIN_PASSWORD"  < /apps/sandbox/mysql/NEETASTUDIO.sql

docker exec -i mysqlserver sh -c 'exec mysql --user=root --password="$MYSQL_ADMIN_PASSWORD"' < /apps/sandbox/mysql/NEETASTUDIO.sql
```