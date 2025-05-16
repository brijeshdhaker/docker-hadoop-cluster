package org.examples.sb.service;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.OnBehalfOfCredential;
import com.azure.identity.OnBehalfOfCredentialBuilder;
import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.kiota.RequestInformation;
import com.microsoft.kiota.authentication.AuthenticationProvider;
import com.microsoft.kiota.authentication.BaseBearerTokenAuthenticationProvider;
import org.examples.sb.security.NamedOidcUser;
import org.examples.sb.utils.Utilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AzureGraphService {

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    //"da5ac8f7-13d6-46e7-815d-012b01123148"
    @Value("${azure.tenant.id}")
    private String tenantId;

    //"c5c062d8-4fe2-4319-9897-a59e57ed7ad2";
    @Value("${azure.client.id}")
    private String clientId;

    //"fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia"
    @Value("${azure.client.secret}")
    private String clientSecret;

    // It is important to reuse this object, as it will cache tokens.
    private static IConfidentialClientApplication app;

    public GraphServiceClient getAzureGraphService() throws Exception {

        if (app == null) {
            app = ConfidentialClientApplication.builder(clientId,ClientCredentialFactory.createFromSecret(clientSecret))
                    .authority("https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/")
                    .build();
        }

        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                        Collections.singleton("https://graph.microsoft.com/.default"))
                .build();

        // The first time this is called, the app will make an HTTP request to the token issuer, so this is slow. Latency can be >1s
        IAuthenticationResult result = app.acquireToken(clientCredentialParam).get();

        return new GraphServiceClient((request, additionalAuthenticationContext) -> {
            Set<String> authHeaders =  new HashSet<>();
            authHeaders.add("Bearer " + result.accessToken());
            request.headers.put(HttpHeaders.AUTHORIZATION, authHeaders);
        });

        /*
        final String[] scopes = new String[] {"https://graph.microsoft.com/.default"};
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        if (null == scopes || null == credential) {
            throw new Exception("Unexpected error");
        }
        return new GraphServiceClient(credential, scopes);
        */
    }


    public Map getUserDetails(OAuth2AuthenticationToken authentication) throws Exception {

        OAuth2AuthorizedClient auth2AuthorizedClient = clientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        /*
        String accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
        String userId = "";
        final String[] scopes = new String[] {"https://graph.microsoft.com/.default"};
        if( authentication.getPrincipal() instanceof NamedOidcUser){
            NamedOidcUser oidcUser = (NamedOidcUser) authentication.getPrincipal();
            String id = oidcUser.getUserID();
            userId = oidcUser.getClaimValue("oid");
        }else {
            userId = "b7824d54-eff8-4bd1-9caf-ffe259d038d0";
        }
        */

        //Utilities.filterClaims(authentication.getPrincipal());



        /*
        final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .userAssertion(accessToken)
                .build();
        
        */

        //final GraphServiceClient graphServiceClient = new GraphServiceClient(credential, scopes);

        final GraphServiceClient graphServiceClient = new GraphServiceClient(new GraphAuthenticationProvider(auth2AuthorizedClient));
        final User user = graphServiceClient.me().get();
        Map<String,String> userProperties = new LinkedHashMap<>();

        if (user == null) {
            userProperties.put("Graph Error", "GraphSDK returned null User object.");
        } else {
            userProperties.put("id", user.getId());
            userProperties.put("Display Name", user.getDisplayName());
            userProperties.put("Given Name", user.getGivenName());
            userProperties.put("Last Name", user.getSurname());
            userProperties.put("Phone Number", user.getMobilePhone());
            userProperties.put("City", user.getCity());
        }
        return userProperties;
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
    }



    /**
     * Sample GraphAuthenticationProvider class. An Authentication provider is required for setting up a
     * GraphServiceClient. This one extends AuthenticationProvider which in turn implements IAuthenticationProvider.
     * This allows using an Access Token provided by Oauth2AuthorizationClient.
     */
    private static class GraphAuthenticationProvider implements AuthenticationProvider {

        private OAuth2AuthorizedClient auth2AuthorizedClient;

        GraphAuthenticationProvider(OAuth2AuthorizedClient auth2AuthorizedClient){
            this.auth2AuthorizedClient = auth2AuthorizedClient;
        }

        @Override
        public void authenticateRequest(@NotNull RequestInformation request, @Nullable Map<String, Object> additionalAuthenticationContext) {
            String accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
            Set<String> authHeaders =  new HashSet<>();
            authHeaders.add("Bearer " + accessToken);
            request.headers.put(HttpHeaders.AUTHORIZATION, authHeaders);
        }
    }
}
