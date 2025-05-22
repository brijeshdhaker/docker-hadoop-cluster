/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */

CREATE TABLE  AUDIT_TYPES (
    ID          bigint AUTO_INCREMENT,
    AUDIT_TYPE  varchar(50),
    DESCRIPTION varchar(200),
    ADD_TS      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPD_TS      TIMESTAMP,
    primary key(ID)
);
