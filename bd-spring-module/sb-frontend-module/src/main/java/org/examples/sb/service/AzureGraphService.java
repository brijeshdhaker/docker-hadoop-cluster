package org.examples.sb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class GraphClientService {

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    public Map getUserDetails(OAuth2AuthenticationToken authentication){

        OAuth2AuthorizedClient auth2AuthorizedClient = clientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
        /*
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Map> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/me",
                HttpMethod.GET,
                entity,
                Map.class);
        return response.getBody();
        */
        return null;
    }
}
