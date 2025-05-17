package org.examples.sb.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@CrossOrigin(origins = {"*localhost*"}, maxAge = 3600)
@RequestMapping(path ="/health", produces = MediaType.TEXT_HTML_VALUE)
public class HealthController {

    @RequestMapping(path ="/server")
    public @ResponseBody String healthServer() {
        return "OK";
    }

    @RequestMapping(path ="/database")
    public @ResponseBody String healthDatabase() {
        return "OK";
    }

}
