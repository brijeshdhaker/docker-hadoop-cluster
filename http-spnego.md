To configure curl to access an URL protected by Kerberos HTTP SPNEGO:

# Run curl -V:
$ curl -V
curl 7.19.7 (universal-apple-darwin10.0) libcurl/7.19.7 OpenSSL/0.9.8l
zlib/1.2.3
Protocols: tftp ftp telnet dict ldap http file https ftps
Features: GSS-Negotiate IPv6 Largefile NTLM SSL libz

# Login to the KDC using kinit.
$ kinit
Please enter the password for tucu@LOCALHOST:

# Use curl to fetch the protected URL:
## -- Secure
$ curl --cacert /path/to/truststore.pem --negotiate -u : -b ~/cookiejar.txt -c ~/cookiejar.txt https://localhost:14000/webhdfs/v1/?op=liststatus

## -- Insecure
$ curl -i --negotiate -u : -b ~/cookiejar.txt -c ~/cookiejar.txt http://namenode.sandbox.net:9870/webhdfs/v1/?op=liststatus

$ curl -i --negotiate -u : -b ~/cookiejar.txt -c ~/cookiejar.txt http://namenode.sandbox.net:9870/webhdfs/v1/user?op=liststatus



where:
The --cacert option is required if you are using TLS/SSL certificates that curl does not recognize by default.
The --negotiate option enables SPNEGO in curl.
The -u : option is required but the username is ignored (the principal that has been specified for kinit is used).
The -b and -c options are used to store and send HTTP cookies.
Cloudera does not recommend using the -k or --insecure option as it turns off curl's ability to verify the certificate.

#
#
#
about:config
network.negotiate-auth.trusted-uris=.sandbox.net

#
#
google-chrome --auth-server-whitelist="*.sandbox.net"
Chrome --auth-server-whitelist="*.sandbox.net"

#
#
#

https://superuser.com/questions/1707537/firefox-and-chromium-dont-do-kerberos-negotiation-curl-does
