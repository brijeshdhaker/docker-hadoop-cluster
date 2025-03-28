# 
# Install istioctl
# 
curl -L https://istio.io/downloadIstio | sh -
cd istio-1.25.0
export PATH=$PWD/bin:$PATH

istioctl install --set profile=minimal -y
istioctl version
istioctl dashboard kiali

# 
# Install Istio using the demo profile, without any gateways:
istioctl install -f samples/bookinfo/demo-profile-no-gateways.yaml -y

# 
# Install the Kubernetes Gateway API CRDs
$ kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || \
{ kubectl kustomize "github.com/kubernetes-sigs/gateway-api/config/crd?ref=v1.2.1" | kubectl apply -f -; }

# 
# Label the namespace that will host the application with istio-injection=enabled
# 
kubectl label namespace default istio-injection=enabled

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
