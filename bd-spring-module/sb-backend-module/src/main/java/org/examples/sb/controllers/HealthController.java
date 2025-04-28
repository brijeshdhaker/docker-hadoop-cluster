package org.examples.sb.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class HealthController {

    @RequestMapping(path ="/health", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String health() {
        return "OK";
    }

}
