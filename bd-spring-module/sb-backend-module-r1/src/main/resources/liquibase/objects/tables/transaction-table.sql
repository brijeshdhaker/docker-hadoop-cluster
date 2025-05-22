/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */
CREATE TABLE IF NOT EXISTS TRANSACTIONS (
    `ID`            bigint(8) AUTO_INCREMENT,
    `UUID`          VARCHAR(128),
    `CARDTYPE`      VARCHAR(30),
    `WEBSITE`       VARCHAR(30),
    `PRODUCT`       VARCHAR(30),
    `AMOUNT`        DOUBLE,
    `CITY`          VARCHAR(30),
    `COUNTRY`       VARCHAR(30),
    `ADD_TS`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `UPD_TS`        TIMESTAMP,
)