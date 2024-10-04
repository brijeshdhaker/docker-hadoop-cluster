/**
 * Author:  brijeshdhaker
 * Created: May 1, 2021
 * DROP PROCEDURE IF EXISTS P_GET_USER_ASSETS;
 */

CREATE OR REPLACE PROCEDURE P_GET_USER_ASSETS(IN id bigint)
BEGIN
    
    SELECT 
        *
    FROM
        ASSETS
    WHERE
        USERID = id
    ORDER BY ASSETS_ID;
    
END $$
