/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */
CREATE TABLE  AUDIT_TYPE (
    ID          bigint(8) AUTO_INCREMENT,
    AUDIT_TYPE  varchar(50),
    DESCRIPTION varchar(200),
    ADD_TS      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPD_TS      TIMESTAMP,
    primary key(ID)
);

insert into AUDIT_TYPE (AUDIT_TYPE , DESCRIPTION , ADD_TS, UPD_TS ) values
('USER', 'Application Audit Logs',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

insert into AUDIT_TYPE (AUDIT_TYPE , DESCRIPTION , ADD_TS, UPD_TS ) values
('SYSTEM', 'System Audit Logs',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

insert into AUDIT_TYPE (AUDIT_TYPE , DESCRIPTION , ADD_TS, UPD_TS ) values
('BATCH', 'System Audit Logs',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());