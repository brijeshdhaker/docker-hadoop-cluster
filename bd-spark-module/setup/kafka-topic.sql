---
--- Add User
---
CREATE USER 'brijeshdhaker'@'%' IDENTIFIED BY 'paSSW0rd';
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on *.* TO 'brijeshdhaker'@'%' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'brijeshdhaker'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

SHOW GRANTS FOR 'brijeshdhaker'@'%';
---
---
---
create database HMS_SPARK;

SHOW HMS_SPARK;

USE HMS_SPARK;

CREATE EXTERNAL TABLE IF NOT EXISTS transaction_details (
    `id` int,
    `uuid` string,
    `cardtype` string,
    `website` string,
    `product` string,
    `amount` double,
    `city` string,
    `country` string,
    `addts` bigint
)
COMMENT 'Transaction Details External Table'
PARTITIONED BY(`txn_receive_date` string)
STORED AS Parquet
LOCATION  'hdfs://namenode:9000/warehouse/tablespace/external/hive/transaction_details/'
TBLPROPERTIES("creator"="Brijesh K Dhaker");

---
---
---
create database SANDBOXDB;

SHOW DATABASES;

USE SANDBOXDB;

show tables;


commit;

---
--- mysql --user=root --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
--- mysql --user=brijeshdhaker --password=paSSW0rd --host=mysqlserver.sandbox.net --database=SANDBOXDB
---

USE SANDBOXDB;

CREATE TABLE `SANDBOXDB`.`KAFKA_OFFSETS`(
    `CONSUMER_GROUP` VARCHAR(255),
    `TOPIC` VARCHAR(255),
    `PARTITION` INT(255),
    `OFFSET_VALUE` BIGINT(255),
    `COMMIT_TIME` TIMESTAMP,
    CONSTRAINT UK_KAFKA_OFFSETS UNIQUE (`CONSUMER_GROUP`, `TOPIC`, `PARTITION`)
);

select * from KAFKA_OFFSETS;
select count(*) from KAFKA_OFFSETS;

INSERT INTO KAFKA_OFFSETS(`CONSUMER_GROUP`,`TOPIC`,`PARTITION`, `OFFSET_VALUE`,`COMMIT_TIME`) VALUES('transaction-avro-cg', 'transaction-avro-topic', 0, 0, NOW());
INSERT INTO KAFKA_OFFSETS(`CONSUMER_GROUP`,`TOPIC`,`PARTITION`, `OFFSET_VALUE`,`COMMIT_TIME`) VALUES('transaction-avro-cg', 'transaction-avro-topic', 1, 0, NOW());
INSERT INTO KAFKA_OFFSETS(`CONSUMER_GROUP`,`TOPIC`,`PARTITION`, `OFFSET_VALUE`,`COMMIT_TIME`) VALUES('transaction-avro-cg', 'transaction-avro-topic', 2, 0, NOW());
INSERT INTO KAFKA_OFFSETS(`CONSUMER_GROUP`,`TOPIC`,`PARTITION`, `OFFSET_VALUE`,`COMMIT_TIME`) VALUES('transaction-avro-cg', 'transaction-avro-topic', 3, 0, NOW());

CREATE TABLE `SANDBOXDB`.`KAFKA_TOPICS`(
    `MAIN_TOPIC` VARCHAR(255),
    `PARTITIONS` INT(255),
    `ERROR_TOPIC` VARCHAR(255)
);
select * from KAFKA_TOPICS;
select count(*) from KAFKA_TOPICS;
INSERT INTO KAFKA_TOPICS(`MAIN_TOPIC`,`PARTITIONS`,`ERROR_TOPIC`) VALUES ('transaction-avro-topic', 4, 'error_transaction-avro-topic');
UPDATE KAFKA_TOPICS SET PARTITIONS=4 where MAIN_TOPIC='transaction-avro-topic';