kubectl -n kube-system get pods -l 'k8s-app=calico-node' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app=calico-node' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app in (calico-node)' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app notin (qa)' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app notin (qa), tier in (app)' -L k8s-app

kubectl api-resources -o wide

kubectl -n kube-system get pods --selector=batch.kubernetes.io/job-name=pi --output=jsonpath='{.items[*].metadata.name}'

kubectl run pod nginx --image=nginx --dry-run=client -o yaml
kubectl expose pod app-springboot -n i100121 --port=80 --target-port=8080 --type=NodePort --dry-run=client -o yaml > app-svc.yaml

# 
# ingress
# 
kubectl get pods -n ingress

kubectl create deployment web --image=gcr.io/google-samples/hello-app:1.0
kubectl expose deployment web --type=NodePort --port=8080

kubectl create deployment web2 --image=gcr.io/google-samples/hello-app:2.0
kubectl expose deployment web2 --port=8080 --type=NodePort

curl http://192.168.65.128:30473/

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: example-ingress
spec:
  ingressClassName: nginx
  rules:
    - host: hello-world.example
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: web
                port:
                  number: 8080
          - path: /v2
	    pathType: Prefix
	    backend:
	      service:
		name: web2
		port:
		  number: 8080
```

kubectl create ingress example-ingress --class=nginx --rule="hello-world.example/*=web:8080"
kubectl get ingress example-ingress -o yaml > ./bd-spring-module/helm/k8s/app-ingress.yaml

curl --resolve "hello-world.example:80:192.168.65.128" -i http://hello-world.example
curl --resolve "hello-world.example:80:127.0.0.1" -i http://hello-world.example

kubectl port-forward --namespace=ingress-nginx service/ingress-nginx-controller 8080:80

# 
# 
# 
kubectl create deployment nginx --image=nginx:latest --dry-run=client -o yaml
kubectl expose deployment nginx -n i100121 --port=80 --target-port=8080 --type=NodePort --dry-run=client -o yaml > app-svc.yaml

kubectl autoscale deployment nginx --min=5 --max=10 --cpu-percent=85

kubectl create deployment web --image=gcr.io/google-samples/hello-app:1.0
kubectl expose deployment web --type=NodePort --port=8080
curl http://172.17.0.15:31637 

kubectl create deployment web2 --image=gcr.io/google-samples/hello-app:2.0
kubectl expose deployment web2 --type=NodePort --port=8080


#
#
#
kubectl create clusterrolebinding cluster-admin-binding \
--clusterrole cluster-admin \
--user $(gcloud config get-value account)

kubectl wait --namespace ingress-nginx \
--for=condition=ready pod \
--selector=app.kubernetes.io/component=controller \
--timeout=120s

helm upgrade --install ingress-nginx ingress-nginx \
--repo https://kubernetes.github.io/ingress-nginx \
--namespace ingress-nginx --create-namespace

kubectl create job hello --image=busybox:latest -- bin/sh -c "date; echo 'Hello World' " >> k8s-job.yaml
kubectl create job hello --image=busybox:latest --dry-run=client -o yaml -- bin/sh -c "date; echo 'Hello World' " >> k8s-job.yaml
kubectl create cronjob hello --image=busybox:latest --schedule="*/1 8 * * * *" --dry-run=client -o yaml -- bin/sh -c "date; echo 'Hello World' " >> cron-job.yaml

```shell
kubectl apply -f - <<EOF
apiVersion: v1
kind: Service
metadata:
creationTimestamp: null
labels:
run: app-springboot
name: app-springboot
namespace: i100121
spec:
ports:
- port: 80
  protocol: TCP
  targetPort: 8080
  selector:
  run: app-springboot
  type: NodePort
  status:
  loadBalancer: {}
EOF
```

#
# K8S N/W Policies : https://docs.tigera.io/calico/latest/network-policy/get-started/kubernetes-policy/
#

# Allow ingress traffic from pods in the same namespace
```yaml
#
# In the following example, incoming traffic to pods with label color=blue are allowed only if they come from a pod with color=red, on port 80.
#
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: allow-same-namespace
  namespace: default
spec:
  podSelector:
    matchLabels:
      color: blue
  ingress:
    - from:
        - podSelector:
            matchLabels:
              color: red
      ports:
        - port: 80
```

# 
# Allow ingress traffic from pods in a different namespace
# 
```yaml
#
# In the following example, incoming traffic is allowed only if they come from a pod with label color=red, in a namespace with label shape=square, on port 80.
#
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: allow-different-namespace
  namespace: default
spec:
  podSelector:
    matchLabels:
      color: blue
  ingress:
    - from:
        - podSelector:
            matchLabels:
              color: red
          namespaceSelector:
            matchLabels:
              shape: square
      ports:
        - port: 80
```

#
# egress policies
#

# Allow egress traffic from pods in the same namespace
```yaml
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: allow-egress-same-namespace
  namespace: default
spec:
  podSelector:
    matchLabels:
      color: blue
  egress:
    - to:
        - podSelector:
            matchLabels:
              color: red
      ports:
        - port: 80
```

# Allow egress traffic to IP addresses or CIDR range
```yaml
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: allow-egress-external
  namespace: default
spec:
  podSelector:
    matchLabels:
      color: red
  egress:
    - to:
        - ipBlock:
            cidr: 172.18.0.0/24
```

# Best practice: create deny-all default network policy

```yaml
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: default-deny
  namespace: policy-demo
spec:
  podSelector:
    matchLabels: {}
  policyTypes:
    - Ingress
    - Egress
```


#
# Calico N/W Policies https://docs.tigera.io/calico/latest/network-policy/get-started/calico-policy/calico-network-policy
#

# Control traffic to/from endpoints in a namespace

```yaml
#
# In the following example, ingress traffic to endpoints in the namespace: production with label color: red is allowed, only if it comes from a pod in the same namespace with color: blue, on port 6379
#
apiVersion: projectcalico.org/v3
kind: NetworkPolicy
metadata:
  name: allow-tcp-6379
  namespace: production
spec:
  selector: color == 'red'
  ingress:
    - action: Allow
      protocol: TCP
      source:
        selector: color == 'blue'
      destination:
        ports:
          - 6379
```

### To allow ingress traffic from endpoints in other namespaces, 
### use a namespaceSelector in the policy rule. A namespaceSelector matches namespaces based on the labels that are applied in the namespace. 
### In the following example, ingress traffic is allowed from endpoints in namespaces that match shape == circle.

```yaml

apiVersion: projectcalico.org/v3
kind: NetworkPolicy
metadata:
  name: allow-tcp-6379
  namespace: production
spec:
  selector: color == 'red'
  ingress:
    - action: Allow
      protocol: TCP
      source:
        selector: color == 'blue'
        namespaceSelector: shape == 'circle'
      destination:
        ports:
          - 6379
```


## Control traffic to/from endpoints independent of namespace

The following Calico network policy is similar to the previous example, but uses kind: GlobalNetworkPolicy so it applies to all endpoints, regardless of namespace.


```yaml
#
# In the following example, incoming TCP traffic to any pods with label color: red is denied if it comes from a pod with color: blue.
#
apiVersion: projectcalico.org/v3
kind: GlobalNetworkPolicy
metadata:
  name: deny-blue
spec:
  selector: color == 'red'
  ingress:
    - action: Deny
      protocol: TCP
      source:
        selector: color == 'blue'
```

As with kind: NetworkPolicy, you can allow or deny ingress traffic from endpoints in specific namespaces using a namespaceSelector in the policy rule:

```yaml
apiVersion: projectcalico.org/v3
kind: GlobalNetworkPolicy
metadata:
  name: deny-circle-blue
spec:
  selector: color == 'red'
  ingress:
    - action: Deny
      protocol: TCP
      source:
        selector: color == 'blue'
        namespaceSelector: shape == 'circle'
```

# 
## Control traffic to/from endpoints using IP addresses or CIDR ranges
# 
```yaml
apiVersion: projectcalico.org/v3
kind: NetworkPolicy
metadata:
  name: allow-egress-external
  namespace: production
spec:
  selector: color == 'red'
  types:
    - Egress
  egress:
    - action: Allow
      destination:
        nets:
          - 1.2.3.0/24
```

## Apply network policies in specific order
