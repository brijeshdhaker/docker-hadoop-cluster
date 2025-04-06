/**
 * Author:  brijeshdhaker
 * Created: May 1, 2021
 * DROP PROCEDURE IF EXISTS P_GET_USER_DETAILS;
 * DELIMITER $$
 */


CREATE PROCEDURE IF NOT EXISTS P_GET_USER_DETAILS(IN id bigint)
BEGIN
    
    SELECT 
        USERID,
        USERNAME, 
        EMAIL, 
        STATUS, 
        ADD_TS, 
        UPD_TS 
    FROM
        USERS
    ORDER BY NAME;
    
END $$
