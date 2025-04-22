package org.examples.sb.services;

import org.examples.sb.enums.AuditType;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.models.Auditlog;
import org.examples.sb.repositories.AuditlogRepository;
import org.examples.sb.repositories.entities.AuditlogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuditlogService {

    @Autowired
    AuditlogRepository auditlogRepository;

    public List<Auditlog> getAuditlogs() throws AppException {
        List<Auditlog> dtos = new ArrayList<>();
        Iterable<AuditlogEntity> auditlogs = auditlogRepository.findAll(Sort.by("id"));
        for (AuditlogEntity log : auditlogs) {
            dtos.add(toRequestDTO(log));
        }
        return dtos;
    }

    public Auditlog getAuditlog(long id) throws AppException {
        Auditlog dto = null;
        //AuditLog auditlog = auditlogRepository.findById(id).orElse(null);
        Optional<AuditlogEntity> auditlog = auditlogRepository.findById(id);
        if(auditlog.isPresent()){
            dto = toRequestDTO(auditlog.get());
        }
        return dto;
    }

    public void deleteAuditLog(long logid) throws AppException {
        //auditlogRepository.deleteById(logid);
    }

    public Auditlog saveAuditLog(Auditlog r_dto) throws AppException {
        //AuditlogEntity entity = auditlogRepository.save(toEntity(r_dto));
        //return toRequestDTO(entity);
        return null;
    }

    public AuditlogEntity toEntity(Auditlog dto){

        AuditlogEntity entity = new AuditlogEntity();
        entity.setId(dto.getId());
        entity.setDescription(dto.getLogMessage());
        entity.setLogAction(dto.getLogAction());
        entity.setAuditType(AuditType.getByName(dto.getAuditType()));
        entity.setReferenceType(dto.getRefrenceType());
        entity.setRefrenceId(dto.getRefrenceId());
        entity.setUserid(dto.getUserid());

        return  entity;

    }

    public Auditlog toRequestDTO(AuditlogEntity entity) {

        Auditlog dto = new Auditlog();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }

        if (entity.getAuditType() != null) {
            AuditType at = entity.getAuditType();
            dto.setAuditType(at.getName());
        }

        if (entity.getLogAction() != null) {
            dto.setLogAction(entity.getLogAction());
        }

        if (entity.getDescription() != null) {
            dto.setLogMessage(entity.getDescription());
        }
        if (entity.getRefrenceId() != null) {
            dto.setRefrenceId(entity.getRefrenceId());
        }
        if (entity.getReferenceType() != null) {
            dto.setRefrenceType(entity.getReferenceType());
        }
        if (entity.getUserid() != null) {
            dto.setUserid(entity.getUserid());
        }

        if (entity.getAddTs() != null) {
            dto.setAddTs(entity.getAddTs().toEpochSecond(ZoneOffset.UTC));
        }

        if (entity.getUpdTs() != null) {
            dto.setUpdTs(entity.getUpdTs().toEpochSecond(ZoneOffset.UTC));
        }

        return dto;
    }

    public Auditlog toDTO(AuditlogEntity entity) {

        Auditlog dto = new Auditlog();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }

        if (entity.getAuditType() != null) {
            AuditType at = entity.getAuditType();
            dto.setAuditType(at.getName());
        }

        if (entity.getLogAction() != null) {
            dto.setLogAction(entity.getLogAction());
        }

        if (entity.getDescription() != null) {
            dto.setLogMessage(entity.getDescription());
        }
        if (entity.getRefrenceId() != null) {
            dto.setRefrenceId(entity.getRefrenceId());
        }
        if (entity.getReferenceType() != null) {
            dto.setRefrenceType(entity.getReferenceType());
        }
        if (entity.getUserid() != null) {
            dto.setUserid(entity.getUserid());
        }

        if (entity.getAddTs() != null) {
            dto.setAddTs(entity.getAddTs().toEpochSecond(ZoneOffset.UTC));
        }

        if (entity.getUpdTs() != null) {
            dto.setUpdTs(entity.getUpdTs().toEpochSecond(ZoneOffset.UTC));
        }

        return dto;
    }
}
