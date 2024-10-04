/**
 * Author:  brijeshdhaker
 * Created: May 1, 2021
 * DROP PROCEDURE IF EXISTS P_GET_USER_DETAILS;
 */

CREATE OR REPLACE PROCEDURE P_GET_USER_DETAILS(IN id bigint)
BEGIN
    
    SELECT 
        ID,
        USERNAME, 
        EMAIL, 
        STATUS, 
        ADD_TS, 
        UPD_TS 
    FROM
        USERS
    ORDER BY USERID;
    
END $$
