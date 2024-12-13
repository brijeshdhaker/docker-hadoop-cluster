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

/**

*/

insert into ITEMS (`NAME`, `PRICE`, `QTY`, ADD_TS, UPD_TS ) values
('Brijesh Dhaker', 'brijeshdhaker@gmail.com', 'Active', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
