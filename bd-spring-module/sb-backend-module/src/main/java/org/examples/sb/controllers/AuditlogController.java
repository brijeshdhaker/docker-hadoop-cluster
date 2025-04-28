package org.examples.sb.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.helpers.ModelHelper;
import org.examples.sb.models.Auditlog;
import org.examples.sb.services.AuditlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author brijeshdhaker
 */
@Slf4j
@RestController
@CrossOrigin(origins = {"*localhost*"})
@RequestMapping(path ="/auditlog", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditlogController {

    @Autowired
    AuditlogService auditlogService;

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
