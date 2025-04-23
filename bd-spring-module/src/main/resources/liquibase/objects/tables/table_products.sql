/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */

CREATE TABLE IF NOT EXISTS PRODUCTS (
    `ID`            BIGINT NOT NULL AUTO_INCREMENT,
    `NAME`          VARCHAR(250),
    `PRICE`         DECIMAL(6,2),
    `STATUS`        VARCHAR(30),
    `QUANTITY`      INTEGER,
    `ADD_TS`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `UPD_TS`        TIMESTAMP,
    PRIMARY KEY(`ID`)
)