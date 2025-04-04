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
-keyout ./bd-k8s-module/istio/certs/ca-root.key \
-out ./bd-k8s-module/istio/certs/ca-root.crt

# 1.2.1 ca-intermediate CSR
openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/certs/ca-intermediate.csr \
-keyout ./bd-k8s-module/istio/certs/ca-intermediate.key \
-subj "/CN=Intermediate CA/O=Sandbox/OU=Security/L=Pune/ST=MH/C=IN/emailAddress=security@sandbox.net"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/ca-intermediate.csr

# 1.2.2 ca-intermediate
openssl x509 \
-req \
-sha256 \
-days 3650 \
-set_serial 1000 \
-CA ./bd-k8s-module/istio/certs/ca-root.crt \
-CAkey ./bd-k8s-module/istio/certs/ca-root.key \
-in ./bd-k8s-module/istio/certs/ca-intermediate.csr \
-out ./bd-k8s-module/istio/certs/ca-intermediate.crt

# 1.3 CA CHAIN CRT
cat ./bd-k8s-module/istio/certs/ca-intermediate.crt \
  ./bd-k8s-module/istio/certs/ca-root.crt > ./bd-k8s-module/istio/certs/ca-cert-chain.crt

```

# 2. Generate a certificate and a private key for httpbin.sandbox.net:
```shell

openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/certs/httpbin.sandbox.net.csr \
-keyout ./bd-k8s-module/istio/certs/httpbin.sandbox.net.key \
-subj "/CN=httpbin.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net" \
-addext "subjectAltName = DNS:localhost,DNS:httpbin,DNS:*.sandbox.net,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
-addext "extendedKeyUsage = serverAuth"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/httpbin.sandbox.net.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-passin pass:sandbox \
-set_serial 1001 \
-CA ./bd-k8s-module/istio/certs/ca-intermediate.crt \
-CAkey ./bd-k8s-module/istio/certs/ca-intermediate.key \
-in ./bd-k8s-module/istio/certs/httpbin.sandbox.net.csr \
-out ./bd-k8s-module/istio/certs/httpbin.sandbox.net.crt \
-extensions v3_req \
-extfile ./bd-k8s-module/istio/certs/httpbin-server-extfile.cnf


```

# 3. Create a second set of the same kind of certificates and keys:
```shell

mkdir -p ./bd-k8s-module/istio/certs/set-02

openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/certs/set-02/httpbin.sandbox.net.csr \
-keyout ./bd-k8s-module/istio/certs/set-02/httpbin.sandbox.net.key \
-subj "/CN=httpbin.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net" \
-addext "subjectAltName = DNS:localhost,DNS:httpbin,DNS:*.sandbox.net,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
-addext "extendedKeyUsage = serverAuth"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/set-02/httpbin.sandbox.net.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-set_serial 1002 \
-CA ./bd-k8s-module/istio/certs/ca-intermediate.crt \
-CAkey ./bd-k8s-module/istio/certs/ca-intermediate.key \
-in ./bd-k8s-module/istio/certs/set-02/httpbin.sandbox.net.csr \
-out ./bd-k8s-module/istio/certs/set-02/httpbin.sandbox.net.crt \
-extensions v3_req \
-extfile ./bd-k8s-module/istio/certs/httpbin-server-extfile.cnf

```

# 4. Generate a certificate and a private key for helloworld.example.com:
```shell

openssl req \
-newkey rsa:2048 \
-nodes \
-out ./bd-k8s-module/istio/certs/helloworld.sandbox.net.csr \
-keyout ./bd-k8s-module/istio/certs/helloworld.sandbox.net.key \
-subj "/CN=helloworld.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net" \
-addext "subjectAltName = DNS:localhost,DNS:helloworld,DNS:*.sandbox.net,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
-addext "extendedKeyUsage = serverAuth"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/helloworld.sandbox.net.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-passin pass:sandbox \
-set_serial 1003 \
-CA ./bd-k8s-module/istio/certs/ca-intermediate.crt \
-CAkey ./bd-k8s-module/istio/certs/ca-intermediate.key \
-in ./bd-k8s-module/istio/certs/helloworld.sandbox.net.csr \
-out ./bd-k8s-module/istio/certs/helloworld.sandbox.net.crt \
-extensions v3_req \
-extfile ./bd-k8s-module/istio/certs/helloworld-server-extfile.cnf

```

# 5. Generate a client certificate and private key:
```shell

openssl req \
-newkey rsa:2048 -nodes \
-out ./bd-k8s-module/istio/certs/client.sandbox.net.csr \
-keyout ./bd-k8s-module/istio/certs/client.sandbox.net.key \
-subj "/CN=client.sandbox.net/O=Sandbox/OU=Istio/L=Pune/ST=MH/C=IN/emailAddress=support@sandbox.net"

openssl req -text -noout -verify -in ./bd-k8s-module/istio/certs/client.sandbox.net.csr

openssl x509 \
-req \
-sha256 \
-days 365 \
-set_serial 1004 \
-CA ./bd-k8s-module/istio/certs/ca-intermediate.crt \
-CAkey ./bd-k8s-module/istio/certs/ca-intermediate.key \
-in ./bd-k8s-module/istio/certs/client.sandbox.net.csr \
-out ./bd-k8s-module/istio/certs/client.sandbox.net.crt

```

ls -lt ./bd-k8s-module/istio/certs

#
# Configure a TLS ingress gateway for a single host
# 

# 1. Create a secret for the ingress gateway:
```shell
kubectl create -n istio-system secret tls httpbin-credential \
--key=./bd-k8s-module/istio/certs/httpbin.sandbox.net.key \
--cert=./bd-k8s-module/istio/certs/httpbin.sandbox.net.crt
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
  --cacert /bd-k8s-module/istio/certs/ca-cert-chain.crt \
  "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```

# 4. Change the gateway’s credentials by deleting the gateway’s secret and then recreating it using different certificates and keys
```shell
kubectl -n istio-system delete secret httpbin-credential
kubectl create -n istio-system secret tls httpbin-credential \
--key=./bd-k8s-module/istio/certs/set-02/httpbin.sandbox.net.key \
--cert=./bd-k8s-module/istio/certs/set-02/httpbin.sandbox.net.crt
```

# 5. Access the httpbin service with curl using the new certificate chain:
```shell
curl -v -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/certs/set-02/example.com.crt "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```
# 6. If you try to access httpbin using the previous certificate chain, the attempt now fails:
```shell
curl -v -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt "https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```


#
# Configure a mutual TLS ingress gateway
#

# Change the credentials of the ingress gateway by deleting its secret and creating a new one. 
# The server uses the CA certificate to verify its clients, and we must use the key ca.crt to hold the CA certificate.
```shell

kubectl -n istio-system delete secret httpbin-credential

kubectl create -n istio-system secret generic httpbin-credential \
--from-file=tls.key=./bd-k8s-module/istio/certs/httpbin.sandbox.net.key \
--from-file=tls.crt=./bd-k8s-module/istio/certs/httpbin.sandbox.net.crt \
--from-file=ca.crt=./bd-k8s-module/istio/certs/ca-cert-chain.crt \
--dry-run=client \
-o yaml

kubectl -n istio-system get secret httpbin-credential --output=yaml

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
curl -v \
-HHost:httpbin.sandbox.net \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt \
--resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
"https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```

# Pass a client certificate and private key to curl and resend the request. 
# Pass your client’s certificate with the --cert flag and your private key with the --key flag to curl:
```shell
curl -v -k -HHost:httpbin.sandbox.net --resolve "httpbin.sandbox.net:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt \
--cert ./bd-k8s-module/istio/certs/client.sandbox.net.crt \
--key ./bd-k8s-module/istio/certs/client.sandbox.net.key \
"https://httpbin.sandbox.net:$SECURE_INGRESS_PORT/status/418"
```