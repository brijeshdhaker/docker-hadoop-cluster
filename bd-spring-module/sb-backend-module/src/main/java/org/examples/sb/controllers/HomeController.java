package org.examples.sb.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {

    @PreAuthorize("hasAuthority('SANDBOX_APIReadRole')")
    @RequestMapping(path ="/home", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String home() {
        return "<H1>Sandbox APIs</H1>";
    }

}
