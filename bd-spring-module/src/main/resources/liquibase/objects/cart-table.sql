/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */
CREATE TABLE `CART` (
  `CART_ID`         int(11) unsigned NOT NULL AUTO_INCREMENT,
  `STATUS`          varchar(24),
  `EXPIRE_DATE`     DATETIME,
  `ADD_TS`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `UPD_TS`          TIMESTAMP,
  PRIMARY KEY (`CART_ID`)
);

/**

*/
insert into CART (`STATUS`, `EXPIRE_DATE`, `ADD_TS`, `UPD_TS`) values
('Active', '2020-01-01 10:10:10', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

