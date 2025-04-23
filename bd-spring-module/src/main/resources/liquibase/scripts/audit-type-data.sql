/**
 * Author:  brijeshdhaker
 * Created: Jan 12, 2021
 */

insert into AUDIT_TYPES (AUDIT_TYPE , DESCRIPTION , ADD_TS, UPD_TS ) values
('USER', 'Application Audit Logs',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

insert into AUDIT_TYPES (AUDIT_TYPE , DESCRIPTION , ADD_TS, UPD_TS ) values
('SYSTEM', 'System Audit Logs',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());

insert into AUDIT_TYPES (AUDIT_TYPE , DESCRIPTION , ADD_TS, UPD_TS ) values
('BATCH', 'Batch Audit Logs',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());