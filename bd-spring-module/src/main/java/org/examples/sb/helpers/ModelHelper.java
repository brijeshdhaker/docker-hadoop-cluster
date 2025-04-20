package org.examples.sb.helpers;

import org.springframework.beans.BeanUtils;
import org.examples.sb.models.User;
import org.examples.sb.repositories.entities.UserEntity;

public class ModelHelper {

    public static User toUser(UserEntity rdto) {

        User sdto = new User();
        String[] ignoreProperties = {"id","uuid"};
        BeanUtils.copyProperties(rdto,sdto,ignoreProperties);

        if(rdto.getId() != null){
            sdto.setId(rdto.getId());
        }
        return  sdto;
    }

}
