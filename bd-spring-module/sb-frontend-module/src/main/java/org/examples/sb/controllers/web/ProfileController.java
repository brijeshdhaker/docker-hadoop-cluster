package org.examples.sb.controllers.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Controller for requests to the {@code /profile} resource. Populates the model with the claims from the
 * {@linkplain OidcUser} for use by the view.
 */
@Slf4j
@Controller
public class ProfileController {

    private final static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal OidcUser oidcUser) {
        model.addAttribute("profile", oidcUser.getClaims());
        model.addAttribute("profileJson", claimsToJson(oidcUser.getClaims()));
        return "profile";
    }

    private String claimsToJson(Map<String, Object> claims) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (JsonProcessingException jpe) {
            log.error("Error parsing claims to JSON", jpe);
        }
        return "Error parsing claims to JSON.";
    }
}
