#
# Start the httpbin sample:
#
kubectl apply -f ./bd-k8s-module/istio/samples/httpbin/httpbin.yaml

# 1. Create a root certificate and private key to sign the certificates for your services:
```shell

mkdir -p ./bd-k8s-module/istio/certs

# 1.1 ca-root
openssl req -x509 -sha256 \
-nodes \
-days 7300 \
-newkey rsa:2048 \
-subj "/CN=Root CA/O=Sandbox/OU=Security/L=Pune/ST=MH/C=IN/emailAddress=security@sandbox.net" \
-extensions v3_ca \
-keyout ./bd-k8s-module/istio/certs/ca-root.key.pem \
-out ./bd-k8s-module/istio/certs/ca-root.cert.pem

# 1.2.1 ca-intermediate CSR
openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/certs/ca-intermediate.csr \
-keyout ./bd-k8s-module/istio/certs/ca-intermediate.key.pem \
-subj "/CN=Intermediate CA/O=Sandbox/OU=Security/L=Pune/ST=MH/C=IN/emailAddress=security@sandbox.net" \
-addext "basicConstraints = critical, CA:true, pathlen:0" \
-addext "keyUsage = critical, digitalSignature, cRLSign, keyCertSign"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/ca-intermediate.csr

# 1.2.2 ca-intermediate
openssl x509 \
-req \
-sha256 \
-days 3650 \
-CAcreateserial \
-CA ./bd-k8s-module/istio/certs/ca-root.cert.pem \
-CAkey ./bd-k8s-module/istio/certs/ca-root.key.pem \
-in ./bd-k8s-module/istio/certs/ca-intermediate.csr \
-out ./bd-k8s-module/istio/certs/ca-intermediate.cert.pem

# 1.3 CA CHAIN CRT
cat ./bd-k8s-module/istio/certs/ca-intermediate.cert.pem \
  ./bd-k8s-module/istio/certs/ca-root.cert.pem > ./bd-k8s-module/istio/certs/ca-chain.cert.pem

```

# 2. Generate a certificate and a private key for httpbin.sandbox.net:
```shell
openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/certs/sbhttps.csr \
-keyout ./bd-k8s-module/istio/certs/sbhttps.key.pem \
-subj "/CN=httpbin.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net" \
-addext "subjectAltName = DNS:localhost,DNS:httpbin.sandbox.net,DNS:*.sandbox.net,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
-addext "extendedKeyUsage = serverAuth"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/httpbin.sandbox.net.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-CAcreateserial \
-CA ./bd-k8s-module/istio/certs/ca-intermediate.cert.pem \
-CAkey ./bd-k8s-module/istio/certs/ca-intermediate.key.pem \
-in ./bd-k8s-module/istio/certs/sbhttps.csr \
-out ./bd-k8s-module/istio/certs/sbhttps.cert.pem \
-extensions v3_req \
-extfile ./bd-k8s-module/istio/certs/sbhttps-extfile.cnf

```
# 3. Generate a client certificate and private key:
```shell
openssl req \
-newkey rsa:2048 -nodes \
-out ./bd-k8s-module/istio/certs/sbhttps-client.csr \
-keyout ./bd-k8s-module/istio/certs/sbhttps-client.key.pem \
-subj "/CN=client.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/sbhttps-client.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-CAcreateserial \
-CA ./bd-k8s-module/istio/certs/ca-intermediate.cert.pem \
-CAkey ./bd-k8s-module/istio/certs/ca-intermediate.key.pem \
-in ./bd-k8s-module/istio/certs/sbhttps-client.csr \
-out ./bd-k8s-module/istio/certs/sbhttps-client.cert.pem
```

# Configure a TLS ingress gateway for a single host

# 1. Create a secret for the ingress gateway:
```shell
kubectl -n istio-system delete secret ingress-tls-credential

kubectl -n istio-system create secret tls ingress-tls-credential \
--key=./bd-k8s-module/istio/certs/sbhttps.key.pem \
--cert=./bd-k8s-module/istio/certs/sbhttps.cert.pem

kubectl -n istio-system get secret ingress-tls-credential

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
      # must be the same as secret
      credentialName: ingress-tls-credential 
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

```shell
export INGRESS_NAME=istio-ingressgateway
export INGRESS_NS=istio-system

kubectl get svc "$INGRESS_NAME" -n "$INGRESS_NS"

export INGRESS_HOST=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="https")].port}')
export TCP_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="tcp")].port}')
```

# 3. Send an HTTPS request to access the httpbin service through HTTPS:
```shell
curl -v \
  -HHost:httpbin.sandbox.net \
  --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
  --cacert ./bd-k8s-module/istio/certs/ca-chain.cert.pem \
  "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```

#
# Configure a mutual TLS ingress gateway
#

# Change the credentials of the ingress gateway by deleting its secret and creating a new one. 
# The server uses the CA certificate to verify its clients, and we must use the key ca.crt to hold the CA certificate.
```shell

kubectl -n istio-system delete secret ingress-mtls-credential

kubectl -n istio-system create secret generic ingress-mtls-credential \
--from-file=tls.key=./bd-k8s-module/istio/certs/sbhttps.key.pem \
--from-file=tls.crt=./bd-k8s-module/istio/certs/sbhttps.cert.pem \
--from-file=ca.crt=./bd-k8s-module/istio/certs/ca-chain.cert.pem \
--dry-run=client \
-o yaml

kubectl -n istio-system get secret ingress-mtls-credential --output=yaml

```

# Configure the ingress gateway:
```shell
cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1
kind: Gateway
metadata:
  name: mygateway
spec:
  # use istio default ingress gateway
  selector:
    istio: ingressgateway 
  servers:
  - port:
      number: 443
      name: https
      protocol: HTTPS
    tls:
      mode: MUTUAL
      credentialName: ingress-mtls-credential 
    hosts:
    - httpbin.sandbox.net
EOF
```

# Attempt to send an HTTPS request using the prior approach and see how it fails:
```shell
curl -v \
-HHost:httpbin.sandbox.net \
--cacert ./bd-k8s-module/istio/certs/ca-chain.cert.pem \
--resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
"https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```

# Pass a client certificate and private key to curl and resend the request. 
# Pass your client’s certificate with the --cert flag and your private key with the --key flag to curl:
```shell
curl -v -k -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/certs/ca-chain.cert.pem \
--cert ./bd-k8s-module/istio/certs/sbhttps-client.cert.pem \
--key ./bd-k8s-module/istio/certs/sbhttps-client.cert.pem \
"https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```