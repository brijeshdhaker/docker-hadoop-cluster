    private java.lang.Integer id;
    private java.lang.String uuid;
    private java.lang.String cardtype;
    private java.lang.String website;
    private java.lang.String product;
    private java.lang.Double amount;
    private java.lang.String city;
    private java.lang.String country;
    private java.lang.Long addts;

/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */

CREATE TABLE `ITEM` (
  `ITEM_ID`         bigint(8) AUTO_INCREMENT,
  `NAME`            VARCHAR(30) NOT NULL,
  `PRICE`           long  NOT NULL,
  `QTY`             int(11) unsigned NOT NULL,
  `ADD_TS`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `UPD_TS`          TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `CART_ID` (`CART_ID`),
  CONSTRAINT `cart_items_fk_1` FOREIGN KEY (`CART_ID`) REFERENCES `CART`(`CART_ID`)
);

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