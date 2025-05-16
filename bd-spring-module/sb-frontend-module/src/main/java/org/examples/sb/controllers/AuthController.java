package org.examples.sb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    //@Autowired
    //private RestTemplate restTemplate;

    @GetMapping("/web/auth_handler")
    public ResponseEntity<?> handelOAuthCallback(@RequestParam String code) {
        ResponseEntity<?> response;
        try {

            // 1. Exchange auth code for tokens
            String tokenEndpoint = "https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/oauth2/v2.0/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id","c5c062d8-4fe2-4319-9897-a59e57ed7ad2");
            params.add("client_secret","fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia");
            params.add("redirect_uri","http://localhost:8080/web/auth_handler");
            params.add("grant_type","authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            String idToken = (String) tokenResponse.getBody().get("id_token");

            response = new ResponseEntity<String>(idToken, HttpStatus.OK);

        }catch (Exception e){
            response = new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
