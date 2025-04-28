package org.examples.sb.controllers;

import java.util.Map;


import com.auth0.jwt.interfaces.Claim;


import jakarta.servlet.http.HttpServletRequest;
import org.examples.sb.security.Auth0Parser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GreetingController {

    @PreAuthorize("hasAuthority('SCOPE_APIReadRole')")
    @GetMapping("/hi")
    public String hi() {
        return "Hi";
    }

    @PreAuthorize("hasAuthority('SCOPE_APIWriteRole')")
    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", required = true)
                        final String name) {
        return String.format("Hello, %s", name);
    }

    @GetMapping("/whoami")
    public String whoami(HttpServletRequest request) {

        // Get security context and print authorities
        SecurityContext securityContext = SecurityContextHolder.getContext();
        log.info("whoami: securityContext.getAuthentication-> {}", securityContext.getAuthentication());
        securityContext.getAuthentication().getAuthorities().stream().forEach(
                (val) -> log.info("Authority -> {}", val.getAuthority())
        );

        // Get token value from the header
        String authHeaderValue = request.getHeader("Authorization");
        log.debug("authHeaderValue: {}", authHeaderValue);

        String appId = "undefined";

        if (!StringUtils.isEmpty(authHeaderValue)) {
            String token = request.getHeader("Authorization").replaceAll("Bearer ", "");
            log.debug("token: {}", token);

            // Access all claims from the token itself by using Auth0 JWT impl
            Map <String, Claim> claims = new Auth0Parser().getClaims(token);
            if (!claims.isEmpty()) {
                claims.get("roles").asList(String.class).forEach(role -> log.info(role));
                appId = claims.get("appid").asString();
            }
        }

        return appId;

    }
}