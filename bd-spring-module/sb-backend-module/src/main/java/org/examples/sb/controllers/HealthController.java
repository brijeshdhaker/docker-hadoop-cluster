package org.examples.sb.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1")
public class HealthController {

    @RequestMapping(path ="/health", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String health() {
        return "OK";
    }

}
