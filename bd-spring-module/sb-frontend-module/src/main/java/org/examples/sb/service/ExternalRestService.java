package org.examples.sb.service;

import java.security.Principal;
import java.util.Arrays;

import org.examples.sb.security.NamedOidcUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.OnBehalfOfCredential;
import com.azure.identity.OnBehalfOfCredentialBuilder;

@Component
public class ExternalRestService {

    //"da5ac8f7-13d6-46e7-815d-012b01123148"
    @Value("${azure.tenant.id}")
    private String tenantId;

    //"c5c062d8-4fe2-4319-9897-a59e57ed7ad2";
    @Value("${azure.client.id}")
    private String clientId;

    //"fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia"
    @Value("${azure.client.secret}")
    private String clientSecret;

    public RestTemplate getRestTemplate(String[] scopes) {

        NamedOidcUser namedOidcUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauth = (OAuth2AuthenticationToken) authentication;
                if (oauth.getPrincipal() instanceof NamedOidcUser) {
                    namedOidcUser = (NamedOidcUser) oauth.getPrincipal();
                }
            }
        }

        RestTemplate rest = new RestTemplate();
        if(namedOidcUser != null ){
            
            String userName = namedOidcUser.getName();
            String userAccessToken = namedOidcUser.getIdToken().getTokenValue();
            final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .userAssertion(userAccessToken)
                .build();
            
            AccessToken apiAccessToken = credential.getToken(new TokenRequestContext().setScopes(Arrays.asList(scopes))).block();
            rest.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().set("X-USER-NAME", userName);
                request.getHeaders().setBearerAuth(apiAccessToken.getToken());
                return execution.execute(request, body);
            });
        }    
        
        return rest;
    }

}
