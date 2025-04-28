package org.examples.sb.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {

    @RequestMapping(path ="/home", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String home() {
        return "Hello, World";
    }

}
