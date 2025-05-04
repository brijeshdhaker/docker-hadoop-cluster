package org.examples.sb.helpers;

import com.azure.identity.*;
import com.microsoft.graph.models.*;
import com.microsoft.graph.serviceclient.GraphServiceClient;

import java.util.Properties;

public class AzureGraphClient {


    public static void main(String args[]) throws Exception{
        // Load properties file and set properties used throughout the sample
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        String authority = properties.getProperty("AUTHORITY");
        String scope = properties.getProperty("SCOPE");


        final String tenantId = "da5ac8f7-13d6-46e7-815d-012b01123148";
        final String clientId = properties.getProperty("CLIENT_ID");;
        final String clientSecret = properties.getProperty("SECRET");
        final String authorizationCode = "AUTH_CODE_FROM_REDIRECT";
        final String redirectUrl = "YOUR_REDIRECT_URI";
        String[] scopes = new String[] { "User.Read" };

        /*
        // The authorization code flow enables native and web apps to obtain tokens in the user's name securely
        final AuthorizationCodeCredential credential = new AuthorizationCodeCredentialBuilder()
                .clientId(clientId).tenantId(tenantId).clientSecret(clientSecret)
                .authorizationCode(authorizationCode).redirectUrl(redirectUrl).build();
        */

        scopes = new String[] { "https://graph.microsoft.com/.default" };
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .tenantId(tenantId)
                .clientSecret(clientSecret)
                .build();
        /*
        final OnBehalfOfCredential credential = new OnBehalfOfCredentialBuilder()
                .clientId(clientId).tenantId(tenantId).clientSecret(clientSecret)
                .userAssertion(oboToken).build();
        */
        if (null == scopes || null == credential) {
            throw new Exception("Unexpected error");
        }

        final GraphServiceClient graphClient = new GraphServiceClient(credential, scopes);


        //
        GroupCollectionResponse result = graphClient.groups().get();
        for (Group g : result.getValue()){
            System.out.println(g.getDisplayName());
        }

        MessageCollectionResponse mresult = graphClient.me().messages().get();
        for (Message g : mresult.getValue()){
            System.out.println(g.getBodyPreview());
        }
    }
}
