package org.examples.sb.services;

import lombok.extern.slf4j.Slf4j;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.helpers.ModelHelper;
import org.examples.sb.models.Auditlog;
import org.examples.sb.repositories.AuditlogRepository;
import org.examples.sb.repositories.entities.AuditlogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuditlogService {

    @Autowired
    AuditlogRepository auditlogRepository;

    public List<Auditlog> getAuditlogs() throws AppException {

        List<Auditlog> dtos = auditlogRepository.findAll().stream()
                .map(ModelHelper::toAuditlog)
                .toList();

        return dtos;
    }

    public Auditlog getAuditlog(long id) throws AppException {
        Auditlog dto = null;
        //AuditlogEntity auditlog = auditlogRepository.findById(id).orElse(null);
        Optional<AuditlogEntity> auditlog = auditlogRepository.findById(id);
        if(auditlog.isPresent()){
            dto = ModelHelper.toAuditlog(auditlog.get());
        }
        return dto;
    }

    public void deleteAuditLog(long logid) throws AppException {
        auditlogRepository.deleteById(logid);
    }

    public Auditlog saveAuditLog(Auditlog auditlog) throws AppException {
        AuditlogEntity entity = auditlogRepository.save(ModelHelper.toAuditlogEntity(auditlog));
        return ModelHelper.toAuditlog(entity);
    }

}
