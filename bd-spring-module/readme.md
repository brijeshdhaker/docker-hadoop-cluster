#
### https://helm.sh/docs/intro/cheatsheet/
#
-Dspring.profiles.active=cloud
-Dserver.servlet.context-path=/api/v1

kubectl label namespace sb-apps istio-injection=enabled

#
helm create ./bd-spring-module/helm-chart --namespace sb-apps

# template
helm template bd-spring-module ./bd-spring-module/helm-chart \
--namespace sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--create-namespace=true \
--version=1.0.0 \
--dry-run \
--debug \
--output-dir ./bd-spring-module/helm-chart/manifests

#
helm template bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--version 1.0.0 \
--create-namespace=true \
--debug \
--dry-run \
--output-dir ./bd-spring-module/helm-chart/manifests/

# Distro
helm package ./bd-spring-module/helm-chart --destination ./bd-spring-module/helm-chart/distro

# Install
helm install bd-spring-module ./bd-spring-module/helm-chart \
--namespace sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--version=1.0.0 \
--create-namespace=true \
--debug \
--dry-run 
> ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml

#
helm install bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace sb-apps \
--set author=brijeshdhaker@gmail.com \
--version=1.0.0 \
--debug \
--dry-run

#
helm uninstall bd-spring-module -n sb-apps
helm show values ./bd-spring-module/helm-chart
helm show values ingress-nginx --repo https://kubernetes.github.io/ingress-nginx


#
helm upgrade bd-spring-module ./bd-spring-module/helm-chart \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--version=1.0.0 \
--create-namespace=true \
--install \
--debug \
--dry-run \
--output-dir ./bd-spring-module/helm-chart/manifests/

#
kubectl --namespace=sb-apps get pods 
#
helm repo add bd-spring-module https://nexus.repo.com --namespace sb-apps 
helm repo update bd-spring-module https://nexus.repo.com


kubectl run mysql-client -it --rm --restart=Never --image=mysql:8.0.33 -- /bin/bash -c "mysql --user=root --password=p@SSW0rd --host=mysql-external-svc.sb-apps.svc.cluster.local --database=SANDBOXDB"
kubectl run mysql-client -it --rm --restart=Never --image=mysql:8.0.33 -- /bin/bash -c "mysql --user=root --password=p@SSW0rd --host=mysql-docker.sb-apps.svc.cluster.local --database=SANDBOXDB"

kubectl run curl-client -i --rm --restart=Never  --image=dockerqa/curl:ubuntu-trusty -- /bin/sh -c "curl -v http://springboot-app-svc.sb-apps.svc.cluster.local:9080/api/v1/ | grep 'Hello, World'"
kubectl run curl-client -i --rm --restart=Never --image=dockerqa/curl:ubuntu-trusty --command -- curl --silent springboot-app-svc.sb-apps.svc.cluster.local:9080/api/v1/

# 
# Label the namespace that will host the application with istio-injection=enabled
# 
kubectl label namespace sb-apps istio-injection=enabled
kubectl label namespace sb-apps istio-injection-

# http
curl -v \
--resolve "sbhttp.sandbox.net:80:192.168.9.150" \
"http://sbhttp.sandbox.net/api/v1/" | grep 'Hello, World'

curl -v -k \
-HHost:sbhttp.sandbox.net \
--resolve "sbhttp.sandbox.net:80:192.168.9.150" \
"http://sbhttp.sandbox.net/api/v1/" | grep 'Hello, World'


# https : HTTP/1.1
curl -v \
-HHost:sbhttps.sandbox.net \
--resolve sbhttps.sandbox.net:443:192.168.9.150 \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt \
https://sbhttps.sandbox.net:443/api/v1/ | grep 'Hello, World'

# https : HTTP/2
curl -v -k \
-HHost:sbhttps.sandbox.net \
--resolve sbhttps.sandbox.net:443:192.168.9.150 \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt \
https://sbhttps.sandbox.net:443/api/v1/ | grep 'Hello, World'


# mtls : HTTP/1.1
curl -v \
-HHost:sbmtls.sandbox.net \
--resolve sbmtls.sandbox.net:443:192.168.9.150 \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt \
--cert ./bd-k8s-module/istio/certs/client.sandbox.net.crt \
--key ./bd-k8s-module/istio/certs/client.sandbox.net.key \
https://sbmtls.sandbox.net:443/api/v1/ | grep 'Hello, World'

# mtls : HTTP/2
curl -v -k \
-HHost:sbmtls.sandbox.net \
--resolve sbmtls.sandbox.net:443:192.168.9.150 \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt \
--cert ./bd-k8s-module/istio/certs/client.sandbox.net.crt \
--key ./bd-k8s-module/istio/certs/client.sandbox.net.key \
https://sbmtls.sandbox.net:443/api/v1/ | grep 'Hello, World'

#
#
#
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