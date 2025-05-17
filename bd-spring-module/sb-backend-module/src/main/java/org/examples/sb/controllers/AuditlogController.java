package org.examples.sb.controllers;

import java.util.List;

import org.examples.sb.exceptions.AppException;
import org.examples.sb.models.Auditlog;
import org.examples.sb.services.AuditlogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author brijeshdhaker
 */
@Slf4j
@RestController
@CrossOrigin(origins = {"*localhost*"}, maxAge = 3600)
@RequestMapping(path ="/auditlog", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditlogController {

    @Autowired
    AuditlogService auditlogService;

    @PreAuthorize("hasAuthority('SCOPE_Audit.Read')")
    @GetMapping("/list")
    public ResponseEntity<List<Auditlog>> getAllAuditlogs(@RequestParam(required = false) String title) {
        ResponseEntity<List<Auditlog>> response = null;
        try {
            List<Auditlog> dtos = auditlogService.getAuditlogs();
            if(dtos != null && !dtos.isEmpty()){
                response = new ResponseEntity<>(dtos,HttpStatus.OK);
            }else{
                response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (AppException ex) {
            log.error(ex.getMessage(),ex);
            response = new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  response;
    }

    @PreAuthorize("hasAuthority('SCOPE_Audit.Read')")
    @GetMapping("/{logid}")
    public ResponseEntity<Auditlog> getAuditlog(@PathVariable long logid) {
        ResponseEntity<Auditlog> response = null;
        try {
            Auditlog dto =  auditlogService.getAuditlog(logid);
            if(dto != null){
                response = new ResponseEntity<>(dto,HttpStatus.OK);
            }else{
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (AppException ex) {
            log.error(ex.getMessage(),ex);
            response = new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  response;
    }

    @PreAuthorize("hasAuthority('SCOPE_Audit.Write')")
    @PostMapping
    public ResponseEntity<Auditlog> saveAuditlog(@RequestBody Auditlog r_dto) {
        ResponseEntity<Auditlog> httpResponse;
        try {
            Auditlog auditlog = auditlogService.saveAuditLog(r_dto);
            httpResponse =  new ResponseEntity<>(auditlog,HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
            httpResponse = new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return httpResponse;
    }

    @PreAuthorize("hasAuthority('SCOPE_Audit.Write')")
    @PutMapping("/{logid}")
    public ResponseEntity<Auditlog> updateAuditlog(@PathVariable long logid, @RequestBody Auditlog s_auditlog) {
        ResponseEntity<Auditlog> httpResponse;
        try {
            Auditlog u_auditlog = auditlogService.getAuditlog(logid);
            if (u_auditlog != null) {
                // Merge Properties
                String[] ignoreProperties = {"id"};
                BeanUtils.copyProperties(s_auditlog,u_auditlog,ignoreProperties);
                auditlogService.saveAuditLog(u_auditlog);
                httpResponse =  new ResponseEntity<>(u_auditlog, HttpStatus.OK);
            } else {
                httpResponse =  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (AppException ex) {
            log.error(ex.getMessage(),ex);
            httpResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  httpResponse;
    }

    @PreAuthorize("hasAuthority('SCOPE_Audit.Write')")
    @DeleteMapping("/{logid}")
    public ResponseEntity<?> delete(@PathVariable long logid) {
        try {
            auditlogService.deleteAuditLog(logid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AppException ex) {
            log.error(ex.getMessage(),ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error message")
    public Object handleError(HttpServletRequest req, Exception ex) {
        Object errorObject = new Object();
        return errorObject;
    }

}
