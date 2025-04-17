# 
# Install istioctl
# 
```bash
curl -L https://istio.io/downloadIstio | sh -
cd istio-1.25.0
export PATH=$PWD/bin:$PATH

istioctl install --set profile=demo -y
istioctl install --set profile=minimal -y
istioctl version
istioctl dashboard kiali
istioctl tag list
```

### Install Istio  using helm
```bash

helm repo add istio https://istio-release.storage.googleapis.com/charts

helm install istio-base istio/base -n istio-system --set defaultRevision=default --create-namespace
helm install istio-base istio/base -n istio-system --set profile=demo

kubectl create namespace istio-system
helm show values istio/istiod

# control plan
helm install istiod istio/istiod --namespace istio-system --create-namespace=true --set profile=demo --wait
helm ls -n istio-system
helm status istiod -n istio-system

# Install an ingress gateway
kubectl create namespace istio-ingress
helm show values istio/gateway
helm install istio-ingressgateway istio/gateway -n istio-ingress


helm delete istiod --namespace istio-system
```

# 
# Install Istio using the demo profile, without any gateways:
istioctl install -f samples/bookinfo/demo-profile-no-gateways.yaml -y

#
#
#

# 
# Install the Kubernetes Gateway API CRDs
$ kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || \
{ kubectl kustomize "github.com/kubernetes-sigs/gateway-api/config/crd?ref=v1.2.1" | kubectl apply -f -; }

# 
# Label the namespace that will host the application with istio-injection=enabled
# 
kubectl label namespace default istio-injection=enabled
kubectl label namespace default istio-injection-

for i in $(seq 1 100); do curl -s -o /dev/null "http://$GATEWAY_URL/productpage"; done

#
# Install Sample application
kubectl create -f samples/bookinfo/platform/kube/bookinfo.yaml

kubectl exec "$(kubectl get pod -l app=ratings -o jsonpath='{.items[0].metadata.name}')" -c ratings -- curl -sS productpage:9080/productpage | grep -o "<title>.*</title>"
<title>Simple Bookstore App</title>

# Create Gateway
kubectl apply -f samples/bookinfo/gateway-api/bookinfo-gateway.yaml

# Gateway Host & Ports
export INGRESS_HOST=$(kubectl get gtw bookinfo-gateway -o jsonpath='{.status.addresses[0].value}')
export INGRESS_PORT=$(kubectl get gtw bookinfo-gateway -o jsonpath='{.spec.listeners[?(@.name=="http")].port}')

# Gateway URL
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT

http://192.168.1.151/productpage

# fetching root ca from k8s cluster
kubectl get secret istio-ca-secret -n istio-system -o "jsonpath={.data['root-cert\.pem']}" | base64 -d
kubectl get secret istio-ca-secret -n istio-system -o "jsonpath={.data['ca-cert\.pem']}" | base64 -d
kubectl get secret istio-ca-secret -n istio-system -o "jsonpath={.data['ca-key\.pem']}" | base64 -d

# Uninstallation
istioctl uninstall -y --purge
kubectl delete namespace istio-system
kubectl label namespace default istio-injection-
kubectl kustomize "github.com/kubernetes-sigs/gateway-api/config/crd/experimental?ref=v1.2.1" | kubectl delete -f -
kubectl kustomize "github.com/kubernetes-sigs/gateway-api/config/crd?ref=v1.2.1" | kubectl delete -f -
