#
# Start the httpbin sample:
#
kubectl apply -f ./bd-k8s-module/istio/samples/httpbin/httpbin.yaml

# 1. Create a root certificate and private key to sign the certificates for your services:
```shell

mkdir -p ./bd-k8s-module/istio/example_certs1

openssl req -x509 -sha256 \
-nodes \
-days 365 \
-newkey rsa:2048 \
-subj "/CN=Root CA/O=Sandbox/OU=Security/L=Pune/ST=MH/C=IN/emailAddress=security@sandbox.net" \
-keyout ./bd-k8s-module/istio/example_certs1/root-ca.key \
-out ./bd-k8s-module/istio/example_certs1/root-ca.crt

```

# 2. Generate a certificate and a private key for httpbin.sandbox.net:
```shell

openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.csr \
-keyout ./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.key \
-subj "/CN=httpbin.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net" \
-addext "subjectAltName = DNS:localhost,DNS:*.sandbox.net,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
-addext "extendedKeyUsage = serverAuth"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-passin pass:sandbox \
-set_serial 1001 \
-CA ./bd-setup-module/security/ca/intermediate/certs/intermediate-ca.cert.pem \
-CAkey ./bd-setup-module/security/ca/intermediate/private/intermediate-ca.key.pem \
-in ./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.csr \
-out ./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.crt \
-extensions v3_req \
-extfile ./bd-k8s-module/istio/example_certs1/httpbin-server-extfile.cnf
    
# 3. Sign server certificate with Intermediate Root CA
#openssl ca -config bd-setup-module/security/intermediate/ca/openssl.cnf \
#  -extensions server_cert \
#  -days 375 -notext -md sha256 \
#  -passin pass:sandbox \
#  -in ./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.csr \
#  -out ./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.crt
      
```

# 3. Create a second set of the same kind of certificates and keys:
```shell

mkdir -p ./bd-k8s-module/istio/example_certs2

openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/example_certs2/httpbin.sandbox.net.csr \
-keyout ./bd-k8s-module/istio/example_certs2/httpbin.sandbox.net.key \
-subj "/CN=httpbin.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net" \
-addext "subjectAltName = DNS:localhost,DNS:*.sandbox.net,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
-addext "extendedKeyUsage = serverAuth"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/example_certs2/httpbin.sandbox.net.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-passin pass:sandbox \
-set_serial 1002 \
-CA ./bd-setup-module/security/ca/intermediate/certs/intermediate-ca.cert.pem \
-CAkey ./bd-setup-module/security/ca/intermediate/private/intermediate-ca.key.pem \
-in ./bd-k8s-module/istio/example_certs2/httpbin.sandbox.net.csr \
-out ./bd-k8s-module/istio/example_certs2/httpbin.sandbox.net.crt \
-extensions v3_req \
-extfile ./bd-k8s-module/istio/example_certs2/httpbin-server-extfile.cnf

```

# 4. Generate a certificate and a private key for helloworld.example.com:
```shell

openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/example_certs1/helloworld.example.com.csr \
-keyout ./bd-k8s-module/istio/example_certs1/helloworld.example.com.key \
-subj "/CN=helloworld.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net" \
-addext "subjectAltName = DNS:localhost,DNS:*.sandbox.net,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
-addext "extendedKeyUsage = serverAuth"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/example_certs1/helloworld.example.com.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-passin pass:sandbox \
-set_serial 1003 \
-CA ./bd-setup-module/security/ca/intermediate/certs/intermediate-ca.cert.pem \
-CAkey ./bd-setup-module/security/ca/intermediate/private/intermediate-ca.key.pem \
-in ./bd-k8s-module/istio/example_certs1/helloworld.example.com.csr \
-out ./bd-k8s-module/istio/example_certs1/helloworld.example.com.crt \
-extensions v3_req \
-extfile ./bd-k8s-module/istio/example_certs1/helloworld-server-extfile.cnf

```

# 5. Generate a client certificate and private key:
```shell

openssl req \
-newkey rsa:2048 -nodes \
-out ./bd-k8s-module/istio/example_certs1/client.example.com.csr \
-keyout ./bd-k8s-module/istio/example_certs1/client.example.com.key \
-subj "/CN=client.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net"

openssl x509 \
-req \
-sha256 \
-days 365 \
-passin pass:sandbox \
-set_serial 1004 \
-CA ./bd-setup-module/security/ca/intermediate/certs/intermediate-ca.cert.pem \
-CAkey ./bd-setup-module/security/ca/intermediate/private/intermediate-ca.key.pem \
-in ./bd-k8s-module/istio/example_certs1/client.example.com.csr \
-out ./bd-k8s-module/istio/example_certs1/client.example.com.crt

```

ls ./bd-k8s-module/istio/example_cert*

#
# Configure a TLS ingress gateway for a single host
#

# 1. Create a secret for the ingress gateway:
```shell

kubectl create -n istio-system secret tls httpbin-credential \
--key=./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.key \
--cert=./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.crt

```

# 2. Configure the ingress gateway:
```shell
cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1
kind: Gateway
metadata:
  name: mygateway
spec:
  selector:
    istio: ingressgateway # use istio default ingress gateway
  servers:
  - port:
      number: 443
      name: https
      protocol: HTTPS
    tls:
      mode: SIMPLE
      credentialName: httpbin-credential # must be the same as secret
    hosts:
    - httpbin.sandbox.net
EOF

```
# Next, configure the gateway’s ingress traffic routes by defining a corresponding virtual service:

```shell
cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1
kind: VirtualService
metadata:
  name: httpbin
spec:
  hosts:
  - "httpbin.sandbox.net"
  gateways:
  - mygateway
  http:
  - match:
    - uri:
        prefix: /status
    - uri:
        prefix: /delay
    route:
    - destination:
        port:
          number: 8000
        host: httpbin
EOF

```

export INGRESS_NAME=istio-ingressgateway
export INGRESS_NS=istio-system

kubectl get svc "$INGRESS_NAME" -n "$INGRESS_NS"

export INGRESS_HOST=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="https")].port}')
export TCP_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="tcp")].port}')

# 3. Send an HTTPS request to access the httpbin service through HTTPS:
curl -v -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/example_certs1/example.com.crt "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"

# 4. Change the gateway’s credentials by deleting the gateway’s secret and then recreating it using different certificates and keys

kubectl -n istio-system delete secret httpbin-credential
kubectl create -n istio-system secret tls httpbin-credential \
--key=./bd-k8s-module/istio/example_certs2/httpbin.sandbox.net.key \
--cert=./bd-k8s-module/istio/example_certs2/httpbin.sandbox.net.crt

# 5. Access the httpbin service with curl using the new certificate chain:
curl -v -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/example_certs2/example.com.crt "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"

# 6. If you try to access httpbin using the previous certificate chain, the attempt now fails:
curl -v -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/example_certs1/example.com.crt "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"



#
# Configure a mutual TLS ingress gateway
#

# Change the credentials of the ingress gateway by deleting its secret and creating a new one. 
# The server uses the CA certificate to verify its clients, and we must use the key ca.crt to hold the CA certificate.
```shell

kubectl -n istio-system delete secret httpbin-credential

kubectl create -n istio-system secret generic httpbin-credential \
--from-file=tls.key=./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.key \
--from-file=tls.crt=./bd-k8s-module/istio/example_certs1/httpbin.sandbox.net.crt \
--from-file=ca.crt=./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--dry-run=client \
-o yaml
```

# Configure the ingress gateway:
```shell
cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1
kind: Gateway
metadata:
  name: mygateway
spec:
  selector:
    istio: ingressgateway # use istio default ingress gateway
  servers:
  - port:
      number: 443
      name: https
      protocol: HTTPS
    tls:
      mode: MUTUAL
      credentialName: httpbin-credential # must be the same as secret
    hosts:
    - httpbin.sandbox.net
EOF
```

# Attempt to send an HTTPS request using the prior approach and see how it fails:
```shell
curl -v -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```

# Pass a client certificate and private key to curl and resend the request. 
# Pass your client’s certificate with the --cert flag and your private key with the --key flag to curl:
```shell
curl -v -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-k8s-module/istio/example_certs1/client.example.com.crt \
--key ./bd-k8s-module/istio/example_certs1/client.example.com.key \
"https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```