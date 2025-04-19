package org.examples.sb.controllers;

import org.examples.sb.exceptions.AppException;
import org.examples.sb.services.TransactionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*localhost*"})
@RestController
@RequestMapping(path ="kafka", produces = MediaType.APPLICATION_JSON_VALUE)
public class KafkaController {

    private static final Logger log = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    TransactionsService transactionsService;

    @GetMapping("transactions/{count}")
    public ResponseEntity<String> getUser(@PathVariable String count) {
        ResponseEntity<String> response = null;
        try {
            transactionsService.generateAndSendTransaction();
            if(true){
                response = new ResponseEntity<>("Success",HttpStatus.OK);
            }else{
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (AppException ex) {
            log.error(ex.getMessage(),ex);
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  response;
    }

}
