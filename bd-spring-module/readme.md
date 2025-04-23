#
### https://helm.sh/docs/intro/cheatsheet/
#
-Dspring.profiles.active=cloud
-Dserver.servlet.context-path=/api/v1
-Dspring.config.location=./bd-spring-module/helm-chart/configs/application-local.yaml

# default
-Dspring.config.location=bd-spring-module/helm-chart/configs/application.yaml

# local
-Dspring.profiles.active=local -Dspring.config.location=bd-spring-module/helm-chart/configs/application-local.yaml

# docker
-Dspring.profiles.active=docker -Dspring.config.location=bd-spring-module/helm-chart/configs/application-docker.yaml

# k8s
-Dspring.profiles.active=k8s -Dspring.config.location=bd-spring-module/helm-chart/configs/application-k8s.yaml

mvn spring-boot:run

### Swagger
```shell
http://localhost:9080/api/v1/api-docs
http://localhost:9080/api/v1/swagger-ui/index.html
```

```shell
kubectl create ns sb-apps
kubectl label namespace sb-apps istio-injection=enabled
kubectl label namespace sb-apps istio-injection-
```

# 1. Create a secret for the ingress gateway:
```shell
kubectl -n istio-system delete secret ingress-tls-credential

kubectl -n istio-system create secret tls ingress-tls-credential \
--key=./bd-setup-module/security/server/private/sbhttps.key.pem \
--cert=./bd-setup-module/security/server/certs/sbhttps.cert.pem \
--dry-run=client \
--output=yaml

kubectl -n istio-system get secret ingress-tls-credential --output=yaml

```

```shell
kubectl -n istio-system delete secret ingress-mtls-credential

kubectl -n istio-system create secret generic ingress-mtls-credential \
--from-file=tls.key=./bd-setup-module/security/server/private/sbhttps.key.pem \
--from-file=tls.crt=./bd-setup-module/security/server/certs/sbhttps.cert.pem \
--from-file=ca.crt=./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--dry-run=client \
--output=yaml

kubectl -n istio-system get secret ingress-mtls-credential --output=yaml

```

# Create Chart
```shell
helm create ./bd-spring-module/helm-chart --namespace sb-apps
```
# template
```shell
#
helm template bd-spring-module ./bd-spring-module/helm-chart \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--set environment=k8s \
--create-namespace=true \
--version=1.0.0 \
--dry-run \
--debug \
--output-dir ./bd-spring-module/helm-chart/manifests

#
helm template bd-spring-module ./bd-spring-module/helm-chart \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--set environment=k8s \
--create-namespace=true \
--version=1.0.0 \
--debug \
> ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml

#
helm template bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--version=1.0.0 \
--create-namespace=true \
--debug \
--dry-run \
--output-dir ./bd-spring-module/helm-chart/manifests/

```

# Distro
```shell
helm package ./bd-spring-module/helm-chart --destination ./bd-spring-module/helm-chart/distro
```
# Install Always IfNotPresent
```shell
helm install bd-spring-module ./bd-spring-module/helm-chart \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--set environment=k8s \
--version=1.0.0 \
--create-namespace=true \
--debug \
--dry-run \
> ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml

#
helm install bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--set environment=k8s \
--version=1.0.0 \
--create-namespace=true \
--debug \
--dry-run \
> ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml

```
# Upgrade
```shell
helm upgrade bd-spring-module ./bd-spring-module/helm-chart \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--set environment=k8s \
--version=1.0.0 \
--create-namespace=true \
--install \
--debug \
--dry-run \
--output-dir ./bd-spring-module/helm-chart/manifests/
```
#
```shell
helm show values ./bd-spring-module/helm-chart
```

#
```shell
helm get manifest bd-spring-module -n sb-apps > ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml
```
#
```shell
helm list --all-namespaces
helm ls --namespace=sb-apps

helm status bd-spring-module --namespace=sb-apps
helm history bd-spring-module --namespace=sb-apps

helm uninstall bd-spring-module --namespace=sb-apps
helm delete bd-spring-module --namespace=sb-apps
```
#
```shell
kubectl --namespace=sb-apps get all 
kubectl --namespace=sb-apps get pods --show-labels
kubectl --namespace=sb-apps get hpa

export POD_NAME=$(kubectl get pods --namespace sb-apps -l "app.kubernetes.io/name=springboot-app,app.kubernetes.io/instance=bd-spring-module" -o jsonpath="{.items[0].metadata.name}")
kubectl --namespace=sb-apps exec -it pod/$POD_NAME -- /bin/bash
  
```
#
```shell
helm repo add bd-spring-module https://nexus.repo.com --namespace sb-apps 
helm repo update bd-spring-module https://nexus.repo.com
```
# 
# Label the namespace that will host the application with istio-injection=enabled
# 
```shell
kubectl label namespace sb-apps istio-injection=enabled
kubectl label namespace sb-apps istio-injection-
```
# Test Clients
```shell
kubectl run mysql-client -it --rm --restart=Never --image=mysql:8.0.33 -- /bin/bash -c "mysql --user=root --password=p@SSW0rd --host=mysql-external-svc.sb-apps.svc.cluster.local --database=SANDBOXDB"

kubectl run mysql-client -it --rm --restart=Never --image=mysql:8.0.33 -- /bin/bash -c "mysql --user=root --password=p@SSW0rd --host=mysql-service.sb-apps.svc.cluster.local --database=SANDBOXDB"

kubectl run mysql-client -it --rm --restart=Never --image=mysql:8.0.33 -- /bin/bash -c "mysql --user=root --password=p@SSW0rd --host=mysql-docker.sb-apps.svc.cluster.local --database=SANDBOXDB"

kubectl run curl-client -i --rm --restart=Never  --image=dockerqa/curl:ubuntu-trusty -- /bin/sh -c "curl -v http://springboot-app-svc.sb-apps.svc.cluster.local:9080/api/v1/ | grep 'Hello, World'"
kubectl run curl-client -i --rm --restart=Never --image=dockerqa/curl:ubuntu-trusty --command -- curl --silent http://sbhttp.sandbox.net/api/v1/

# Run this in a separate terminal
# so that the load generation continues and you can carry on with the rest of the steps
kubectl run -i --tty load-generator --rm --image=busybox:1.28 --restart=Never -- /bin/sh -c "while sleep 0.01; do wget -q -O- http://springboot-app-svc.sb-apps:9080/api/v1; done"

kubectl --namespace=sb-apps get hpa springboot-app --watch

```

# HTTP
```shell

# Get IP of the ingress service

IP=$(kubectl get services -n istio-system istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

curl -v \
--resolve "sbhttp.sandbox.net:80:${IP}" \
"http://sbhttp.sandbox.net/api/v1/health" | grep 'OK'

curl -v -k \
-HHost:sbhttp.sandbox.net \
--resolve "sbhttp.sandbox.net:80:${IP}" \
"http://sbhttp.sandbox.net/api/v1/home" | grep 'Hello, World'
```

# HTTPS : HTTP/1.1
```shell
curl -v \
-HHost:sbhttps.sandbox.net \
--resolve sbhttps.sandbox.net:443:${IP} \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
"https://sbhttps.sandbox.net:443/api/v1/home" | grep 'Hello, World'
```
# HTTPS : HTTP/2
```shell
curl -v -k \
-HHost:sbhttps.sandbox.net \
--resolve sbhttps.sandbox.net:443:${IP} \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
"https://sbhttps.sandbox.net:443/api/v1/home" | grep 'Hello, World'
```

# mTLS : HTTP/1.1
```shell
curl -v -sI \
-HHost:sbmtls.sandbox.net \
--resolve sbmtls.sandbox.net:443:${IP} \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-setup-module/security/client/certs/sbhttps-client.cert.pem \
--key ./bd-setup-module/security/client/private/sbhttps-client.key.pem \
"https://sbmtls.sandbox.net:443/api/v1/home" | grep 'Hello, World'
```
# mTLS : HTTP/2
```shell
curl -X GET -s \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-setup-module/security/client/certs/sbhttps-client.cert.pem \
--key ./bd-setup-module/security/client/private/sbhttps-client.key.pem \
"https://sbmtls.sandbox.net:443/api/v1/home" | grep 'Hello, World'

curl -v -k \
-HHost:sbmtls.sandbox.net \
--resolve sbmtls.sandbox.net:443:${IP} \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-setup-module/security/client/certs/sbhttps-client.cert.pem \
--key ./bd-setup-module/security/client/private/sbhttps-client.key.pem \
"https://sbmtls.sandbox.net:443/api/v1/home" | grep 'Hello, World'

for a in {1..1000}; do 
curl \
-HHost:sbmtls.sandbox.net \
--resolve sbmtls.sandbox.net:443:${IP} \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-setup-module/security/client/certs/sbhttps-client.cert.pem \
--key ./bd-setup-module/security/client/private/sbhttps-client.key.pem \
"https://sbmtls.sandbox.net:443/api/v1/home"| grep 'Hello, World' ;

done;

```
#
https://sbhttps.sandbox.net/api/v1/actuator/env

#
#
#
```shell
{{ range $index, $service := (lookup "v1" "Service" "mynamespace" "").items }}
    {{/* do something with each service */}}
{{ end }}

{{- range $index, $topping := .Values.pizzaToppings }}
    {{ $index }}: {{ $topping }}
{{- end }}

#
favorite:
    drink: coffee
    food: pizza

{{- range $key, $val := .Values.favorite }}
    {{ $key }}: {{ $val | quote }}
{{- end }}

{{ (.Files.Glob "configs/*.toml").AsConfig | indent 2 }}


{{- if eq .Values.environment "local" }}
{{- (.Files.Glob "configs/dev/application.yaml").AsConfig | nindent 2 -}}
{{- else if eq .Values.environment "docker" }}
{{- (.Files.Glob "configs/docker/application.yaml").AsConfig | nindent 2 -}}
{{- end }}

#
aapplication.yaml: |- {{ range .Files.Lines "configs/config1.toml" }}
{{ . }}{{ end }}

# kubectl -n istio-system get secret ingress-tls-credential --output='jsonpath={.data.tls\.crt}' | base64 -d
---
apiVersion: v1
kind: Secret
metadata:
  name: ingress-tls-credential
  namespace: istio-system
type: Opaque
data:
  tls.key: {{ .Files.Get "certs/sbhttps.key.pem" | b64enc }}
  tls.crt: {{ .Files.Get "certs/sbhttps.cert.pem" | b64enc }}

# kubectl -n istio-system get secret ingress-mtls-credential --output='jsonpath={.data.tls\.crt}' | base64 -d
---
apiVersion: v1
kind: Secret
metadata:
  name: ingress-mtls-credential
  namespace: istio-system
type: Opaque
data:
  tls.key: {{ .Files.Get "certs/sbhttps.key.pem" | b64enc }}
  tls.crt: {{ .Files.Get "certs/sbhttps.cert.pem" | b64enc }}
  ca.crt: {{ .Files.Get "certs/ca-chain.cert.pem" | b64enc }}
```

```shell
kubectl -n istio-system get secret ingress-tls-credential --output='jsonpath={.data.tls\.crt}' | base64 -d
kubectl -n istio-system get secret ingress-mtls-credential --output='jsonpath={.data.tls\.crt}' | base64 -d
kubectl -n sb-apps create secret docker-registry docker-reg-cred --docker-server=https://index.docker.io/v1/ --docker-username=brijeshdhaker --docker-password=Accoo7@k47 --docker-email=brijeshdhaker@gmail.com --dry-run=client --output=yaml
kubectl get secret docker-reg-cred --output="jsonpath={.data.\.dockerconfigjson}" | base64 --decode
kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "docker-reg-cred"}]}'
```