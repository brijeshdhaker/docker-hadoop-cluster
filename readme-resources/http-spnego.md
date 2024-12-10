To configure curl to access an URL protected by Kerberos HTTP SPNEGO:

# Run curl -V:
zeppelin@zeppelin:~$ curl -V
curl 7.81.0 (x86_64-conda-linux-gnu) libcurl/7.81.0 OpenSSL/3.0.0 zlib/1.2.11 libssh2/1.10.0 nghttp2/1.47.0
Release-Date: 2022-01-05
Protocols: dict file ftp ftps gopher gophers http https imap imaps mqtt pop3 pop3s rtsp scp sftp smb smbs smtp smtps telnet tftp
Features: alt-svc AsynchDNS GSS-API HSTS HTTP2 HTTPS-proxy IPv6 Kerberos Largefile libz NTLM NTLM_WB SPNEGO SSL TLS-SRP UnixSockets


# Login to the KDC using kinit.
$ kinit -k -t /apps/security/keytabs/users/brijeshdhaker.keytab brijeshdhaker@SANDBOX.NET

$kinit brijeshdhaker@SANDBOX.NET
Please enter the password for brijeshdhaker@SANDBOX.NET:

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
