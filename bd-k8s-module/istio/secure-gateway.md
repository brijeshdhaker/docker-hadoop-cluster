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
-subj '/O=example Inc./CN=example.com' \
-keyout ./bd-k8s-module/istio/example_certs1/example.com.key \
-out ./bd-k8s-module/istio/example_certs1/example.com.crt

```

# 2. Generate a certificate and a private key for httpbin.example.com:
```shell

openssl req \
-out ./bd-k8s-module/istio/example_certs1/httpbin.example.com.csr \
-newkey rsa:2048 \
-nodes \
-keyout ./bd-k8s-module/istio/example_certs1/httpbin.example.com.key \
-subj "/CN=httpbin.example.com/O=httpbin organization"

openssl x509 -req -sha256 \
-days 365 \
-CA ./bd-k8s-module/istio/example_certs1/example.com.crt \
-CAkey ./bd-k8s-module/istio/example_certs1/example.com.key \
-set_serial 0 \
-in ./bd-k8s-module/istio/example_certs1/httpbin.example.com.csr \
-out ./bd-k8s-module/istio/example_certs1/httpbin.example.com.crt

```

# 3. Create a second set of the same kind of certificates and keys:
```shell

mkdir -p ./bd-k8s-module/istio/example_certs2

openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -subj '/O=example Inc./CN=example.com' \
-keyout ./bd-k8s-module/istio/example_certs2/example.com.key \
-out ./bd-k8s-module/istio/example_certs2/example.com.crt

openssl req \
-out ./bd-k8s-module/istio/example_certs2/httpbin.example.com.csr \
-newkey rsa:2048 -nodes \
-keyout ./bd-k8s-module/istio/example_certs2/httpbin.example.com.key \
-subj "/CN=httpbin.example.com/O=httpbin organization"

openssl x509 -req -sha256 -days 365 \
-CA ./bd-k8s-module/istio/example_certs2/example.com.crt \
-CAkey ./bd-k8s-module/istio/example_certs2/example.com.key \
-set_serial 0 \
-in ./bd-k8s-module/istio/example_certs2/httpbin.example.com.csr \
-out ./bd-k8s-module/istio/example_certs2/httpbin.example.com.crt

```

# 4. Generate a certificate and a private key for helloworld.example.com:
```shell

openssl req \
-out ./bd-k8s-module/istio/example_certs1/helloworld.example.com.csr -newkey rsa:2048 -nodes \
-keyout ./bd-k8s-module/istio/example_certs1/helloworld.example.com.key \
-subj "/CN=helloworld.example.com/O=helloworld organization"

openssl x509 -req -sha256 -days 365 \
-CA ./bd-k8s-module/istio/example_certs1/example.com.crt \
-CAkey ./bd-k8s-module/istio/example_certs1/example.com.key -set_serial 1 \
-in ./bd-k8s-module/istio/example_certs1/helloworld.example.com.csr \
-out ./bd-k8s-module/istio/example_certs1/helloworld.example.com.crt

```

# 5. Generate a client certificate and private key:
```shell

openssl req \
-out ./bd-k8s-module/istio/example_certs1/client.example.com.csr -newkey rsa:2048 -nodes \
-keyout ./bd-k8s-module/istio/example_certs1/client.example.com.key \
-subj "/CN=client.example.com/O=client organization"

openssl x509 -req -sha256 -days 365 \
-CA ./bd-k8s-module/istio/example_certs1/example.com.crt \
-CAkey ./bd-k8s-module/istio/example_certs1/example.com.key -set_serial 1 \
-in ./bd-k8s-module/istio/example_certs1/client.example.com.csr \
-out ./bd-k8s-module/istio/example_certs1/client.example.com.crt

```

ls ./bd-k8s-module/istio/example_cert*

#
# Configure a TLS ingress gateway for a single host
#

# 1. Create a secret for the ingress gateway:
kubectl create -n istio-system secret tls httpbin-credential \
--key=./bd-k8s-module/istio/example_certs1/httpbin.example.com.key \
--cert=./bd-k8s-module/istio/example_certs1/httpbin.example.com.crt

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
    - httpbin.example.com
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
  - "httpbin.example.com"
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
curl -v -HHost:httpbin.example.com --resolve "httpbin.example.com:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/example_certs1/example.com.crt "https://httpbin.example.com:$SECURE_INGRESS_PORT/status/418"

# 4. Change the gateway’s credentials by deleting the gateway’s secret and then recreating it using different certificates and keys

kubectl -n istio-system delete secret httpbin-credential
kubectl create -n istio-system secret tls httpbin-credential \
--key=./bd-k8s-module/istio/example_certs2/httpbin.example.com.key \
--cert=./bd-k8s-module/istio/example_certs2/httpbin.example.com.crt

# 5. Access the httpbin service with curl using the new certificate chain:
curl -v -HHost:httpbin.example.com --resolve "httpbin.example.com:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/example_certs2/example.com.crt "https://httpbin.example.com:$SECURE_INGRESS_PORT/status/418"

# 6. If you try to access httpbin using the previous certificate chain, the attempt now fails:
curl -v -HHost:httpbin.example.com --resolve "httpbin.example.com:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
--cacert ./bd-k8s-module/istio/example_certs1/example.com.crt "https://httpbin.example.com:$SECURE_INGRESS_PORT/status/418"
