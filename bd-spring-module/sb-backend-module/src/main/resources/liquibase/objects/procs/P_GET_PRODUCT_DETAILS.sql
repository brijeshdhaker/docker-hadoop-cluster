/**
 * Author:  brijeshdhaker
 * Created: May 1, 2021
 * DROP PROCEDURE IF EXISTS P_GET_PRODUCT_DETAILS
 */

CREATE PROCEDURE IF NOT EXISTS P_GET_PRODUCT_DETAILS(IN id bigint)
BEGIN
    
    SELECT 
        ID,
        NAME,
        PRICE,
        STATUS,
        QUANTITY,
        ADD_TS,
        UPD_TS
    FROM
        PRODUCTS
    ORDER BY ID;
    
END $$
