package org.examples.sb.controllers;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.OnBehalfOfCredential;
import com.azure.identity.OnBehalfOfCredentialBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.graph.models.DirectoryObject;
import com.microsoft.graph.models.DirectoryObjectCollectionResponse;
import com.microsoft.graph.models.Group;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.security.NamedOidcUser;
import org.examples.sb.service.AzureGraphService;
import org.examples.sb.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Controller
public class SampleController {

    private final static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    AzureGraphService azureGraphService;

    //"da5ac8f7-13d6-46e7-815d-012b01123148"
    @Value("${azure.tenant.id}")
    private String tenantId;

    //"c5c062d8-4fe2-4319-9897-a59e57ed7ad2";
    @Value("${azure.client.id}")
    private String clientId;

    //"fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia"
    @Value("${azure.client.secret}")
    private String clientSecret;

    /**
     * Add HTML partial fragment from /templates/content folder to request and serve base html
     * @param model Model used for placing user param and bodyContent param in request before serving UI.
     * @param fragment used to determine which partial to put into UI
     */
    private String hydrateUI(Model model, String fragment) {
        model.addAttribute("bodyContent", String.format("content/%s.html", fragment));
        return "base"; // base.html in /templates folder
    }

    /**
     *  Sign in status endpoint
     *  The page demonstrates sign-in status. For full details, see the src/main/webapp/content/status.html file.
     *
     * @param model Model used for placing bodyContent param in request before serving UI.
     * @return String the UI.
     */
    @GetMapping(value = {"/", "sign_in_status", "/index"})
    public String status(Model model) {
        return hydrateUI(model, "status");
    }

    /**
     *  Token details endpoint
     *  Demonstrates how to extract and make use of token details
     *  For full details, see method: Utilities.filterclaims(OidcUser principal)
     *
     * @param model Model used for placing claims param and bodyContent param in request before serving UI.
     * @param principal OidcUser this object contains all ID token claims about the user. See utilities file.
     * @return String the UI.
     */
    @GetMapping(path = "/token_details")
    public String tokenDetails(Model model, @AuthenticationPrincipal OidcUser principal) {
        //
        model.addAttribute("claims", Utilities.filterClaims(principal));
        model.addAttribute("profileJson", claimsToJson(principal.getClaims()));
        //
        List<String> scopes = new LinkedList<>();
        principal.getAuthorities().stream().forEach(
                (val) -> scopes.add(val.getAuthority())
        );
        model.addAttribute("scopes", scopes);

        return hydrateUI(model, "token");
    }

    //OAuth2AuthenticationToken authentication
    // @AuthenticationPrincipal OidcUser principal
    @GetMapping(path = "/call_graph")
    public String callGraph(Model model, OAuth2AuthenticationToken authentication) {
        try{
            GraphServiceClient graphClient = azureGraphService.getAzureGraphService();
            String userId = "";
            if( authentication.getPrincipal() instanceof NamedOidcUser){
                NamedOidcUser oidcUser = (NamedOidcUser) authentication.getPrincipal();
                String id = oidcUser.getUserID();
                userId = oidcUser.getClaimValue("oid");
                Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();
            }else {
                userId = "b7824d54-eff8-4bd1-9caf-ffe259d038d0";
            }
            final User user = graphClient.users().byUserId(userId).get();
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
            // Map<String, String> userProperties = azureGraphService.getUserDetails(authentication);
            model.addAttribute("details", userProperties);

            Map<String,String> userGroups = new LinkedHashMap<>();
            DirectoryObjectCollectionResponse graphResponse = graphClient.users().byUserId(userId).memberOf().get();
            // Process the groups
            for (DirectoryObject group : graphResponse.getValue()) {
                if (group instanceof Group) {
                    userGroups.put(group.getId(), ((Group) group).getDisplayName());
                }
            }
            model.addAttribute("groups", userGroups);

            return hydrateUI(model, "graph");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/call_restapi")
    public String callRestAPI(Model model, @AuthenticationPrincipal OidcUser principal) {


        final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .tenantId(tenantId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .userAssertion(principal.getIdToken().getTokenValue())
                .build();
        //, "api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e/Audit.Read"
        final String[] scopes = new String[] {"api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e/.default"};
        AccessToken apiAccessToken = credential.getToken(new TokenRequestContext().setScopes(Arrays.asList(scopes))).block();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + apiAccessToken.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:9080/api/v1/users", HttpMethod.GET, entity, String.class);
        String users = response.getBody();
        model.addAttribute("users", users);
        return hydrateUI(model, "result");
    }

    // survey endpoint - did the sample address your needs?
    // not an integral a part of this tutorial.
    @GetMapping(path = "/survey")
    public String survey(Model model) {
        return hydrateUI(model, "survey");
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
