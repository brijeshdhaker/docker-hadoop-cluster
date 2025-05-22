# Subscription ID : 
8f282701-79e7-4beb-9dda-8c8066a00a6e

# Tenant ID       : 
da5ac8f7-13d6-46e7-815d-012b01123148

# sandbox-spring-app-secret
App Client ID   : 7f1cf4d7-ca24-47c2-bf17-61a8a796679e
Secret Value    : sVy8Q~jmLAlU3N9viydtvGBBGb5WL2XpBtCq_aqU

# sandbox-client-app-secret
App Client ID   : 7eed8ec9-c714-43f6-8318-710448a55a85
Secret Value    : 2k-8Q~_fm9BQ3ucD4WIdPmSyRygDnu1~HPhV1bNk

#
# sb-frontend-module-secret
#
App Client ID   : c5c062d8-4fe2-4319-9897-a59e57ed7ad2
Secret Value    : fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia

#
# Logout URLS
#
https://login.microsoftonline.com/common/oauth2/v2.0/logout?client_id=c5c062d8-4fe2-4319-9897-a59e57ed7ad2&returnTo=http://localhost:8080/&post_logout_redirect_uri=http://localhost:8080/
https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/oauth2/v2.0/logout?client_id=c5c062d8-4fe2-4319-9897-a59e57ed7ad2&returnTo=http://localhost:8080/

#
#
#
OAuth flows use the following roles:

`Resource Owner` — This is basically an entity that grants access to protected resources. More often than not, this is a user.
`Resource Server` — This is the server that holds the resources the Resource Owner needs access to. The Resource Server exposes an API, and the objective of OAuth is to grant secure access to this API.
`Client` — This is an application that requests access to protected resources, typically for a Resource Owner using the application.
`Authorization Server` — This is a server responsible for accepting credentials from the Resource Owner, checking if they are authorized to access a restricted resource, and providing an access token to allow access.

#
## How this OAuth flow works:
#
The user clicks on a login link in the web application.
The user is redirected to an OAuth authorization server, after which an OAuth login prompt is issued.
The user provides credentials according to the enabled login options.
Typically, the user is shown a list of permissions that will be granted to the web application by logging in and granting consent.
The user is redirected to the application, with the authorization server providing a one-time authorization code.
The app receives the user’s authorization code and forwards it along with the Client ID and Client Secret, to the OAuth authorization server.
The authorization server generates an ID Token, Access Token, and an optional Refresh Token, before providing them to the app.
The web application can then use the Access Token to gain access to the target API with the user’s credentials.


## OAuth Flow Types
1. `Authorization Code Flow` :
   Authorization Code Flow exchanges an authorization code for a token. For this exchange to take place, you have to also pass along your app’s Client Secret. The secret must be securely stored on the client side. The implicit flow is suitable for applications that access APIs and cannot store sensitive information.
   Use Cases: Server side web applications where the source code is not exposed publicly.
   ![authorization_code_flow.png](images/authorization_code_flow.png)

2. `Client Credentials Flow` : used for machine-to-machine communication.
   The Client Credentials Flow allows applications to pass their Client Secret and Client ID to an authorization server, which authenticates the user, and returns a token. This happens without any user intervention.
   ![client_credentials_flow.png](images/client_credentials_flow.png)

3. Resource Owner Password Flow: used by highly-trusted apps.
   ![resource_owner_password_flow.png](images/resource_owner_password_flow.png)

   curl --request POST \
   --url 'https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/oauth2/v2.0/token' \
   --header 'content-type: application/x-www-form-urlencoded' \
   --data 'grant_type=password' \
   --data 'username=brijeshdhaker@gmail.com' \
   --data 'password=Windows2024$' \
   --data 'audience=api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e' \
   --data 'scope=openid email profile' \
   --data 'client_id=c5c062d8-4fe2-4319-9897-a59e57ed7ad2' \
   --data 'client_secret=fXo8Q~qQgHPaJtyViMQMM_PQxSsRAN6uvqPTtdia'

4. Implicit Flow with Form Post : 
5. Hybrid Flow : 
6. Device Authorization Flow
7. Authorization Code Flow with PKCE : PKCE-enhanced Authorization Code Flow builds upon the standard Authorization Code Flow, the steps are very similar.
   ![auth-sequence-auth-code-pkce.png](images/auth-sequence-auth-code-pkce.png)

#
### OAuth-2.0 User Login  Flow
#

1. User(`Resource Owner`) click on login with Azure(`Authorization Server`) 
    https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/adminconsent?client_id=c5c062d8-4fe2-4319-9897-a59e57ed7ad2&state=12345&http://localhost:8080/web/auth_handler
    https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/oauth2/v2.0/authorize?response_type=code&client_id=c5c062d8-4fe2-4319-9897-a59e57ed7ad2&scope=openid%20email%20profile&redirect_uri=http://localhost:8080/web/auth_handler

2. Authorization Server(`Azure`)  share `auth_token` with client (`sb-frontend-module`) 

3. Using `auth_token` sb-frontend-module ask for `authorization_code` from Azure Authorization Server
   
   curl --request POST \
   --url https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/oauth2/v2.0/token \
   --header 'content-type: application/x-www-form-urlencoded' \
   --data 'code=`auth_token`' \
   --data 'grant_type=`authorization_code`' \
   --data 'redirect_uri=http://localhost:8080/web/auth_handler' \
   --data 'client_id=7eed8ec9-c714-43f6-8318-710448a55a85' \
   --data 'client_secret=2k-8Q~_fm9BQ3ucD4WIdPmSyRygDnu1~HPhV1bNk'

3. Using `authorization_code` sb-frontend-module ask for `access_token` from Azure Authorization Server

    # System User Login
    curl --request POST \
    --url https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/oauth2/v2.0/token \
    --header 'content-type: application/x-www-form-urlencoded' \
    --data 'grant_type=client_credentials' \
    --data 'scope=api://7f1cf4d7-ca24-47c2-bf17-61a8a796679e/.default' \
    --data 'client_id=7eed8ec9-c714-43f6-8318-710448a55a85' \
    --data 'client_secret=2k-8Q~_fm9BQ3ucD4WIdPmSyRygDnu1~HPhV1bNk'

4. Using `access_token` client (`sb-frontend-module`) access protected resources from Resource Server (`sandbox-spring-app`).
    curl --location 'http://localhost:9080/api/v1/users' \
    --header 'Authorization: Bearer `access_token`' \
    --header 'content-type: application/json'

5.




#
#
#
1. Request Auth Code from AD
authorization_code                  request to      /oauth/v2.0/authorize

2.  
access_token : access tokens should never be read by the client.
id_token : ID tokens should never be sent to an API

# refresh_token
POST /oauth/token HTTP/1.1
Host: authorization-server.com

grant_type=refresh_token
&refresh_token=xxxxxxxxxxx
&client_id=xxxxxxxxxx
&client_secret=xxxxxxxxxx

# Azure Users : 

brijeshdhaker_gmail.com#EXT#@brijeshdhakergmail.onmicrosoft.com
sandbox@brijeshdhakergmail.onmicrosoft.com      Accoo7@k47#S
operator@brijeshdhakergmail.onmicrosoft.com     Accoo7@k47#R
risk@brijeshdhakergmail.onmicrosoft.com         Accoo7@k47#R