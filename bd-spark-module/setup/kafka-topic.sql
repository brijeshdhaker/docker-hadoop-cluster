---
--- Add User
---
CREATE USER 'brijeshdhaker'@'%' IDENTIFIED BY 'Accoo7@k47';
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on *.* TO 'brijeshdhaker'@'%' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'brijeshdhaker'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

SHOW GRANTS FOR 'brijeshdhaker'@'%';

---
---
---
create database SANDBOXDB;

SHOW DATABASES;

USE SANDBOXDB;

show tables;


commit;

---
---
---
USE SANDBOXDB;

CREATE TABLE `SANDBOXDB`.`KAFKA_OFFSETS`(
    `CONSUMER_GROUP` VARCHAR(255),
    `TOPIC` VARCHAR(255),
    `PARTITION` INT(255),
    `OFFSET_VALUE` BIGINT(255),
    `COMMIT_TIME` TIMESTAMP,
    CONSTRAINT UK_KAFKA_OFFSETS UNIQUE (`CONSUMER_GROUP`, `TOPIC`, `PARTITION`)
)

select * from KAFKA_OFFSETS;
select count(*) from KAFKA_OFFSETS;

CREATE TABLE `SANDBOXDB`.`KAFKA_TOPICS`(
    `MAIN_TOPIC` VARCHAR(255),
    `PARTITIONS` INT(255),
    `ERROR_TOPIC` VARCHAR(255)
)
select * from KAFKA_TOPICS;
select count(*) from KAFKA_TOPICS;