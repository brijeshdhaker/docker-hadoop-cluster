/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */

CREATE TABLE `CART_ITEMS` (
  `ID`              int(11) unsigned NOT NULL AUTO_INCREMENT,
  `CART_ID`         int(11) unsigned NOT NULL,
  `ITEM_ID`         int(11) unsigned NOT NULL,
  `QTY`             int(11) unsigned NOT NULL,
  PRIMARY KEY (`ID`)
);

/**

*/
insert into CART_ITEMS (`CART_ID`, `ITEM_ID`, `QTY`) values (1, 1001, 4);
insert into CART_ITEMS (`CART_ID`, `ITEM_ID`, `QTY`) values (1, 1002, 10);
insert into CART_ITEMS (`CART_ID`, `ITEM_ID`, `QTY`) values (1, 1001, 1);