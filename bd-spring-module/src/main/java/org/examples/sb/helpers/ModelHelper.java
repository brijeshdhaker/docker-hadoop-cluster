package org.examples.sb.helpers;

import org.examples.sb.enums.RoleTypes;
import org.examples.sb.models.Auditlog;
import org.examples.sb.repositories.entities.RoleEntity;
import org.springframework.beans.BeanUtils;
import org.examples.sb.models.User;
import org.examples.sb.repositories.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ModelHelper {

    public static User toUser(UserEntity userEntity) {

        User user = new User();
        String[] ignoreProperties = {"id","uuid"};
        BeanUtils.copyProperties(userEntity, user, ignoreProperties);
        if(userEntity.getId() != null){
            user.setId(userEntity.getId());
        }
        for(RoleEntity role : userEntity.getRoles()){
            user.addRoles(role.getName());
        }
        return  user;
    }


    public static UserEntity toUserEntity(User user) {

        UserEntity userEntity = new UserEntity();
        String[] ignoreProperties = {"id","uuid"};
        BeanUtils.copyProperties(user, userEntity, ignoreProperties);
        for(String role : user.getRoles()){
            RoleTypes R = RoleTypes.getByName(role);
            userEntity.addRole(new RoleEntity(R.getId(),R.getName(),R.getStatus()));
        }
        if(userEntity.getAddTs() == null) {
            userEntity.setAddTs(System.currentTimeMillis());
        }
        if(userEntity.getUpdTs() == null) {
            userEntity.setUpdTs(System.currentTimeMillis());
        }
        return  userEntity;

    }

    public static Auditlog toAuditlogDTO(Auditlog rdto) {
        Auditlog s_dto = new Auditlog();
        String[] ignoreProperties = {"id","uuid"};
        BeanUtils.copyProperties(rdto,s_dto,ignoreProperties);

        if(rdto.getId() != null){
            s_dto.setId(rdto.getId());
        }

        if(rdto.getUuid() != null){
            s_dto.setUuid(rdto.getUuid());
        }else{
            s_dto.setUuid(UUID.randomUUID().toString());
        }

        return s_dto;
    }
}
