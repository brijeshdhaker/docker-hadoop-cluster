# Subscription ID : 
8f282701-79e7-4beb-9dda-8c8066a00a6e


# sandbox-spring-app-secret
App Client ID   : 7f1cf4d7-ca24-47c2-bf17-61a8a796679e
Tenant ID       : da5ac8f7-13d6-46e7-815d-012b01123148

Secret ID       : 2dbc79b0-9fcc-45c8-a2b3-a522ccf861b5
Secret Value    : sVy8Q~jmLAlU3N9viydtvGBBGb5WL2XpBtCq_aqU

# sandbox-client-app-secret
App Client ID   : 7eed8ec9-c714-43f6-8318-710448a55a85
Tenant ID       : da5ac8f7-13d6-46e7-815d-012b01123148

Secret ID       : ff6a9c52-522b-425e-b4e6-6cf0f2c76ed7
Secret Value    : 2k-8Q~_fm9BQ3ucD4WIdPmSyRygDnu1~HPhV1bNk


curl --request POST \
--url https://login.microsoftonline.com/da5ac8f7-13d6-46e7-815d-012b01123148/oauth2/v2.0/token \
--header 'content-type: application/x-www-form-urlencoded' \
--data 'grant_type=client_credentials' \
--data 'scope=api%3A%2F%2F7f1cf4d7-ca24-47c2-bf17-61a8a796679e%2F.default' \
--data 'client_id=7eed8ec9-c714-43f6-8318-710448a55a85' \
--data 'client_secret=2k-8Q~_fm9BQ3ucD4WIdPmSyRygDnu1~HPhV1bNk'

// Line breaks are for legibility only.

GET https://login.microsoftonline.com/{tenant}/adminconsent?
client_id=00001111-aaaa-2222-bbbb-3333cccc4444
&state=12345
&redirect_uri=http://localhost/myapp/permissions