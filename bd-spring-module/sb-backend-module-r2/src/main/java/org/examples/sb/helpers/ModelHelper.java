package org.examples.sb.helpers;

import org.examples.sb.enums.AuditType;
import org.examples.sb.enums.RoleTypes;
import org.examples.sb.models.Auditlog;
import org.examples.sb.models.Role;
import org.examples.sb.repositories.entities.AuditlogEntity;
import org.examples.sb.repositories.entities.RoleEntity;
import org.springframework.beans.BeanUtils;
import org.examples.sb.models.User;
import org.examples.sb.repositories.entities.UserEntity;

import java.time.ZoneOffset;
import java.util.UUID;

public class ModelHelper {

    public static User toUser(UserEntity userEntity) {

        User user = new User();
        String[] ignoreProperties = {"id","uuid"};
        BeanUtils.copyProperties(userEntity, user, ignoreProperties);
        if(userEntity.getId() != null){
            user.setId(userEntity.getId());
        }
        for(RoleEntity roleEntity : userEntity.getRoles()){
            Role role = Role.builder()
                    .id(roleEntity.getId())
                    .name(roleEntity.getName())
                    .status(roleEntity.getStatus())
                    .addTs(roleEntity.getAddTs().toInstant(ZoneOffset.of("Z")).toEpochMilli())
                    .updTs(roleEntity.getUpdTs().toInstant(ZoneOffset.of("Z")).toEpochMilli())
                    .build();

            user.addRoles(role);
        }
        return  user;
    }


    public static UserEntity toUserEntity(User user) {

        UserEntity userEntity = new UserEntity();
        String[] ignoreProperties = {"id","uuid"};
        BeanUtils.copyProperties(user, userEntity, ignoreProperties);
        for(Role role : user.getRoles()){
            RoleTypes roleType = RoleTypes.getByName(role.getName());
            userEntity.addRole(new RoleEntity(role.getId(),role.getName(),role.getStatus()));
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

        String[] ignoreProperties = {"uuid"};
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

    public static Auditlog toAuditlog(AuditlogEntity entity) {

        Auditlog dto = new Auditlog();
        dto.setUuid(UUID.randomUUID().toString());
        String[] ignoreProperties = {"auditType"};
        BeanUtils.copyProperties(entity,dto,ignoreProperties);

        if(entity.getAuditType() != null ){
            dto.setAuditType(entity.getAuditType().getName());
        }
        /*
        if (entity.getAddTs() != null) {
            //dto.setAddTs(entity.getAddTs().toEpochSecond(ZoneOffset.UTC));
            dto.setAddTs(entity.getAddTs());
        }

        if (entity.getUpdTs() != null) {
            //dto.setUpdTs(entity.getUpdTs().toEpochSecond(ZoneOffset.UTC));
            dto.setUpdTs(entity.getUpdTs());
        }
        */

        return dto;
    }

    public static AuditlogEntity toAuditlogEntity(Auditlog auditlog) {

        AuditlogEntity entity = new AuditlogEntity();

        String[] ignoreProperties = {"uuid"};
        BeanUtils.copyProperties(auditlog,entity,ignoreProperties);

        if(auditlog.getAuditType() != null ){
            entity.setAuditType(AuditType.getByName(auditlog.getAuditType()));
        }

        if (auditlog.getAddTs() != null) {
            entity.setAddTs(auditlog.getAddTs());
        }else {
            entity.setAddTs(System.currentTimeMillis());
        }

        if (auditlog.getUpdTs() != null) {
            entity.setUpdTs(auditlog.getUpdTs());
        }else {
            entity.setUpdTs(System.currentTimeMillis());
        }

        return entity;
    }
}
