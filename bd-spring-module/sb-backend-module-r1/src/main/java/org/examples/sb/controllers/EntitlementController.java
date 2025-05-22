package org.examples.sb.controllers;

import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.security.Auth0Parser;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = {"*localhost*"}, maxAge = 3600)
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class EntitlementController {

    @GetMapping("/whoami")
    public String whoami(HttpServletRequest request) {

        // Get security context and print authorities
        SecurityContext securityContext = SecurityContextHolder.getContext();
        log.info("whoami: securityContext.getAuthentication-> {}", securityContext.getAuthentication());
        securityContext.getAuthentication().getAuthorities().stream().forEach(
                (val) -> log.info("Authority -> {}", val.getAuthority())
        );

        String appId = "";
        // Get token value from the header
        String authHeaderValue = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authHeaderValue)) {
            String token = authHeaderValue.replaceAll("Bearer ", "");
            Map <String, Claim> claims = new Auth0Parser().getClaims(token);
            if (!claims.isEmpty()) {
                claims.get("groups").asList(String.class).forEach(role -> log.info(role));
                appId = claims.get("appid").asString();
            }
        }
        return appId;
    }

    @GetMapping("/show")
    public String whoami(HttpServletRequest request, Authentication authentication) {
        authentication.getAuthorities().stream().forEach(
                (val) -> log.info("Authority -> {}", val.getAuthority())
        );
        Jwt jwt = (Jwt)authentication.getPrincipal();
        jwt.getClaims().forEach(
                (key,val) -> log.info("Claim {} -> {}", key, val)
        );
        return jwt.getTokenValue();
    }
}