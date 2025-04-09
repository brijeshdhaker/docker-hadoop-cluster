#
### https://helm.sh/docs/intro/cheatsheet/
#
-Dspring.profiles.active=cloud
-Dserver.servlet.context-path=/api/v1


kubectl label namespace sb-apps istio-injection=enabled
kubectl label namespace sb-apps istio-injection-

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
--create-namespace true \
--debug \
--dry-run \
--output-dir ./bd-spring-module/helm-chart/manifests/
```

# Distro
```shell
helm package ./bd-spring-module/helm-chart --destination ./bd-spring-module/helm-chart/distro
```
# Install Always
```shell
helm install bd-spring-module ./bd-spring-module/helm-chart \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=IfNotPresent \
--version=1.0.0 \
--create-namespace=true \
--debug \
--dry-run 
> ./bd-spring-module/helm-chart/manifests/bd-spring-module.txt

#
helm install bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=Always \
--version=1.0.0 \
--create-namespace=true \
--debug \
--dry-run
> ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml

```
# Upgrade
```shell
helm upgrade bd-spring-module ./bd-spring-module/helm-chart \
--namespace=sb-apps \
--set author=brijeshdhaker@gmail.com \
--set image.pullPolicy=IfNotPresent \
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
helm show values ingress-nginx --repo https://kubernetes.github.io/ingress-nginx
```

#
```shell
helm get manifest bd-spring-module -n sb-apps > ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml
```
#
```shell
helm uninstall bd-spring-module --namespace=sb-apps
```
#
```shell
kubectl --namespace=sb-apps get all 
kubectl --namespace=sb-apps get pods --show-labels

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
```

# HTTP
```shell
curl -v \
--resolve "sbhttp.sandbox.net:80:192.168.9.150" \
"http://sbhttp.sandbox.net/api/v1/" | grep 'Hello, World'

curl -v -k \
-HHost:sbhttp.sandbox.net \
--resolve "sbhttp.sandbox.net:80:192.168.9.150" \
"http://sbhttp.sandbox.net/api/v1/" | grep 'Hello, World'
```

# HTTPS : HTTP/1.1
```shell
curl -v \
-HHost:sbhttps.sandbox.net \
--resolve sbhttps.sandbox.net:443:192.168.9.150 \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
https://sbhttps.sandbox.net:443/api/v1/ | grep 'Hello, World'
```
# HTTPS : HTTP/2
```shell
curl -v -k \
-HHost:sbhttps.sandbox.net \
--resolve sbhttps.sandbox.net:443:192.168.9.150 \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
https://sbhttps.sandbox.net:443/api/v1/ | grep 'Hello, World'
```

# mTLS : HTTP/1.1
```shell
curl -v -sI \
-HHost:sbmtls.sandbox.net \
--resolve sbmtls.sandbox.net:443:192.168.9.150 \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-setup-module/security/client/certs/sbhttps-client.cert.pem \
--key ./bd-setup-module/security/client/private/sbhttps-client.key.pem \
https://sbmtls.sandbox.net:443/api/v1/ | grep 'Hello, World'
```
# mTLS : HTTP/2
```shell
curl -X GET -s \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-setup-module/security/client/certs/sbhttps-client.cert.pem \
--key ./bd-setup-module/security/client/private/sbhttps-client.key.pem \
https://sbmtls.sandbox.net:443/api/v1/ | grep 'Hello, World'

curl -v -k \
-HHost:sbmtls.sandbox.net \
--resolve sbmtls.sandbox.net:443:192.168.9.150 \
--cacert ./bd-setup-module/security/ca/intermediate/certs/ca-chain.cert.pem \
--cert ./bd-setup-module/security/client/certs/sbhttps-client.cert.pem \
--key ./bd-setup-module/security/client/private/sbhttps-client.key.pem \
https://sbmtls.sandbox.net:443/api/v1/ | grep 'Hello, World'

```
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

# kubectl -n istio-system get secret ingress-tls-credential --output='jsonpath={.data.tls\.crt}' | base64 -d
---
# Source: bd-spring-module/templates/secrets.yaml
apiVersion: v1
kind: Secret
metadata:
name: ingress-tls-credential
namespace: istio-system
type: Opaque
data:
tls.key: LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRQzJCaWY0UDhqb2djZXUKUlgrbStUa1YraDcwVTVHZzdkd2N2dWEyS3ZycEh1TmJWVERJSzVOMFdWSkNzS2c2V1pMVkh6UzhkNkFBOHYvaQpxSkRBdDEwdmM0NWgyUWN4dHQ2clNxclFDbUI5a2Vaak9xVFJ4R3BjREx6SmtRMlFkM3ZsUzRmSlBVdDc2cDFjCkFsV09rTFBnbkpuT0NjbDlJVGJ2RE1WRThCMUg1d3c3T01qVDBEd0lsRUhocElxcG5nd1Fhc3JvUUt2NGZMUFMKZmRyQnp6NFFPNkgvVDlCS3cyamNNaHQxcW8rNHB0Y2htdWIrb25leVlPTWdwZzR5emxyeVB0TlcvRVBoM0pKbApxbGZyOEcrb1UxWXRRYlJFR1B0eWtmZGNjVzV3QTRPdWl5SW4wSU9LTWR0N3ZSMGtGOHNPVWF1RzBaWDlFaVFDCmgrTmF4MlVQQWdNQkFBRUNnZ0VBSGtxKyt4ck9XaGRDL2xjVmk5bWM3UEhmbGVTVWJ6WTJjOWczTitqR01lYVEKaXFJT1NMbXJ3K2liZm5xOHZGMHN5T2YxRkVwS0orNUl0RlUrSmJ3K04wZmhVWnBYRHFoeXZRbmpqRlMxd3gvMQpPZDFJRVpyQTRvbkNIejlUQlJCRlg1N212WkJFVUI1MnlwbWUyR2R2Yi9sUHhWQ3VwZkxSbkEzQmhKdjdHM3hvCmxkYURHQi9FcXhFeCtUTFBZWnlpUXY3TEs2Y2tlWmd1TGNpOXpnMkdRUk8wZWhnRSs0UzNzQzdwN2IyejBTYlMKZysySzFhaW5FNzNxV3pyM3FKL2s0UHh1VmVTZVJFcmxPS2pVQmN6RjNpeFg5YUxoVnpEOUNxUXZDUTRTcWF2SQorRWJ5QWtCOFdFVjdGdWNCSGd4eFJxbm53c1ZHcVo1Z0ZydTZ3NU1ZQ1FLQmdRRC9sMWovSXd1a0Y3Q3R3aXBiCmtWQmJKN0k3VGUyUENOeHlSZTZVbVFRdjZCeXdzdllrVkJ6UHgzaWJ3RGtzczN0OTlhSmtnNVFBVXZMaStYWWUKRW5OUktrVmtQZTYyMThVdCtvK2xWMHVqdGNkMVN2RGZza3o1WjNFVXB2VzY0Sk1QSG01MzlFb200NGFkUVNVOQpPbGl2amxGRFB4bDdta0NlNjJ3TWxqNWgrUUtCZ1FDMlVLK3Uzc25iTGxxd0FxNlNkMnlrWlRSbGg3UXQ4cVo0ClE0RVNQbHMvODJFMEZOeXJKQnlmajhhQzk3KzVXc0h4T3hYa0NkQ2loMm95SHo3OGZuMWp4c3NnbjErNU4xMlMKN1dYRTd0UXg1b0FMbXArZ2tLbWFuQ2g4aDFXUXpjNW1VRmNDbDlWbkJRNE5nTlRzN1Z3ZUMxUWNjdTVCVGpKNgpsWG5QWStwQlJ3S0JnRWdLT0tOeEZVN2kvb0Y3UU1VZWFDL0pPSXRPbzY2MUtLMXMySm81cHR4NTU5eEdGT3FZCmFxYUY2cEM3MVdHc0ZScWpxV243VERQckxWcWNSWmFrVEt5TDVaTWpnYTAzay9RcHMyTSsvNnJkQ2FNZHhGYkkKd3JscFZvNGpvUTlUVFIvdHkrVXpqZEw0UG5FRWM2S1dWM2pqL0RPZ0RXYXhTNVYvZGF6MmpBN0JBb0dCQUxBWgp1Y2k0cTlQSnI1RkoxZVBsOVF2S01kVk1RaThMcmQ2R25icklmbzJFQWNVenpYNkYwQndUM1ZTT0NDUDYxVkMwCm1NMSs2WWJFTkVDYnk5UktQY1hOdEZFYmdIUEFWZU5nNmFkU1phYVpsZDhmaU1hb1ZScmhjWDN2bnlmU2syWmkKZmRra1hkNDNsTDVZVGROWTVseWk1cWpnSHVEM3JteVh5MlFXaDZSckFvR0FPZEkwdWVPK3V4RlVaMkRwVHlMdApxRHlNMlAyeFp1WTNTM2NVZG51Wk1uTnVjdG4yWUNoelhaZzkrVUtRMTF5a2hObzFlaDhRUmdhZU8wUEtvQ21uClNFWXNIVEh1V2pzVkd5clgzb1A1UkhJYXh3bThFNW1qaGVoQU9EbnpISGx2T3JOekk3YzhDbzFjVGZKYWRXcjgKd3AyQWM5TlRMaVdvOGxUK1BuTnhrSEU9Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0K
tls.crt: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUZQekNDQXllZ0F3SUJBZ0lVV1NwK0g2R0dDV0RxNGJkZmZBbXhnQ3IvSjZZd0RRWUpLb1pJaHZjTkFRRUwKQlFBd2ZqRUxNQWtHQTFVRUJoTUNTVTR4Q3pBSkJnTlZCQWdNQWsxSU1SQXdEZ1lEVlFRS0RBZFRZVzVrWW05NApNUkV3RHdZRFZRUUxEQWhUWldOMWNtbDBlVEVZTUJZR0ExVUVBd3dQU1c1MFpYSnRaV1JwWVhSbElFTkJNU013CklRWUpLb1pJaHZjTkFRa0JGaFJ6WldOMWNtbDBlVUJ6WVc1a1ltOTRMbTVsZERBZUZ3MHlOVEEwTURjeU1qRTUKTXpsYUZ3MHlOakEwTURjeU1qRTVNemxhTUlHRU1SQXdEZ1lEVlFRRERBZHpZbWgwZEhCek1SQXdEZ1lEVlFRSwpEQWRUWVc1a1ltOTRNUkF3RGdZRFZRUUxEQWRUWlhKMlpYSnpNUTB3Q3dZRFZRUUhEQVJRZFc1bE1Rc3dDUVlEClZRUUlEQUpOU0RFTE1Ba0dBMVVFQmhNQ1NVNHhJekFoQmdrcWhraUc5dzBCQ1FFV0ZITmxZM1Z5YVhSNVFITmgKYm1SaWIzZ3VibVYwTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUF0Z1luK0QvSQo2SUhIcmtWL3B2azVGZm9lOUZPUm9PM2NITDdtdGlyNjZSN2pXMVV3eUN1VGRGbFNRckNvT2xtUzFSODB2SGVnCkFQTC80cWlRd0xkZEwzT09ZZGtITWJiZXEwcXEwQXBnZlpIbVl6cWswY1JxWEF5OHlaRU5rSGQ3NVV1SHlUMUwKZStxZFhBSlZqcEN6NEp5WnpnbkpmU0UyN3d6RlJQQWRSK2NNT3pqSTA5QThDSlJCNGFTS3FaNE1FR3JLNkVDcgorSHl6MG4zYXdjOCtFRHVoLzAvUVNzTm8zREliZGFxUHVLYlhJWnJtL3FKM3NtRGpJS1lPTXM1YThqN1RWdnhECjRkeVNaYXBYNi9CdnFGTldMVUcwUkJqN2NwSDNYSEZ1Y0FPRHJvc2lKOUNEaWpIYmU3MGRKQmZMRGxHcmh0R1YKL1JJa0FvZmpXc2RsRHdJREFRQUJvNEd0TUlHcU1CTUdBMVVkSlFRTU1Bb0dDQ3NHQVFVRkJ3TUJNRk1HQTFVZApFUVJNTUVxQ0NXeHZZMkZzYUc5emRJSUhjMkpvZEhSd2M0SVRjMkpvZEhSd2N5NXpZVzVrWW05NExtNWxkSUlOCktpNXpZVzVrWW05NExtNWxkSWNFZndBQUFZY0V3S2dKZ0ljRXdLZ2VnREFkQmdOVkhRNEVGZ1FVTzROaEtSSG4KMHVjclJhUzZ1MWVzWm9veFZ0d3dId1lEVlIwakJCZ3dGb0FVc0ZuNTdoaWlnYXlOWnBJRGtCUjk4aEJQSUtJdwpEUVlKS29aSWh2Y05BUUVMQlFBRGdnSUJBS2FCNndlQWN0TFJ4MTZXMnJ6ODhEVHJtdU9kNDRyUDFNQ3VOQnBLCnU2eDk1elZDQnJSN3oycXhMUTJ1NUgzRjVjTXQrbHgxRVB4K3ZhWjVuMWJtWGR4Y0FQUmtkU2pua3F2YXZUSWsKZnRBSnhKYmF0eEJlZXhIRiszQkc5SEhWbldCTUFtZlNOcm04cnR2eXNyL0NpS1BHc1BjbzhhdTA1REdRN2N0bQpCMDZkc05XWWZucjlMRUIrclgvR3N6WXNwY0lFRDFOUFpjZytyQkNyeW9FZU15VURneFp2M24xZ0thdjg2SkxDCnpuaFZad05ZM3IzVkRLM0x4WDM1OHEva1pvckp3T0JzNFlHKzUyL2E3b1FvSVJidjZhNWtURGpKNWJxL2I2MTEKcjYzWWxnS2hZWjQ4dm9IQ3FLSlFUcW5RV3A2L0UzRGNzNDZSdUZEZmZtQmdXbHdPRTVEZTFRdnY1ektGM2t1QQpwWGVxRFRndFFKUURBVkJXNjlaQ1NmLzlKVUFiblBadFlROUo2czhoVkR1SlhaZWZrQW44SnFPSVdlS3ZjVjBPCmtDakUxb1hBNzNXNnJVdzJFSFgra2ZESUlPNE1EQVhjeC84OGRlT1NiUHpkQWs3TmZkUXhmYVYyUi9wclQyblMKMVlGYUxJRW0rUkhwaTBHZWJud3U1MytseU5ycEYwR3dFTG5DNFVLWVptQnV0bys1bi9BZ1BuWGhDUkdLRTl4ZAp4TjRieVdtTHhEU3Byamc1QVJVc25jNjVqZ09oZmhQMWVwOElJRjRoUVlZd1p1aGJYL204aUtVaDU4dmZ1aXR0CnFQR1FIZXJ3WGNhSldtaENsQUhDQk9xT01MR2VTMndIdkxBamxnWWd1ZjRoS1M0OGgyaDRUTEl0RlpXOEkxa1oKajBXVgotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==

# kubectl -n istio-system get secret ingress-mtls-credential --output='jsonpath={.data.tls\.crt}' | base64 -d
---
# Source: bd-spring-module/templates/secrets.yaml
apiVersion: v1
kind: Secret
metadata:
name: ingress-mtls-credential
namespace: istio-system
type: Opaque
data:
tls.key: LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRQzJCaWY0UDhqb2djZXUKUlgrbStUa1YraDcwVTVHZzdkd2N2dWEyS3ZycEh1TmJWVERJSzVOMFdWSkNzS2c2V1pMVkh6UzhkNkFBOHYvaQpxSkRBdDEwdmM0NWgyUWN4dHQ2clNxclFDbUI5a2Vaak9xVFJ4R3BjREx6SmtRMlFkM3ZsUzRmSlBVdDc2cDFjCkFsV09rTFBnbkpuT0NjbDlJVGJ2RE1WRThCMUg1d3c3T01qVDBEd0lsRUhocElxcG5nd1Fhc3JvUUt2NGZMUFMKZmRyQnp6NFFPNkgvVDlCS3cyamNNaHQxcW8rNHB0Y2htdWIrb25leVlPTWdwZzR5emxyeVB0TlcvRVBoM0pKbApxbGZyOEcrb1UxWXRRYlJFR1B0eWtmZGNjVzV3QTRPdWl5SW4wSU9LTWR0N3ZSMGtGOHNPVWF1RzBaWDlFaVFDCmgrTmF4MlVQQWdNQkFBRUNnZ0VBSGtxKyt4ck9XaGRDL2xjVmk5bWM3UEhmbGVTVWJ6WTJjOWczTitqR01lYVEKaXFJT1NMbXJ3K2liZm5xOHZGMHN5T2YxRkVwS0orNUl0RlUrSmJ3K04wZmhVWnBYRHFoeXZRbmpqRlMxd3gvMQpPZDFJRVpyQTRvbkNIejlUQlJCRlg1N212WkJFVUI1MnlwbWUyR2R2Yi9sUHhWQ3VwZkxSbkEzQmhKdjdHM3hvCmxkYURHQi9FcXhFeCtUTFBZWnlpUXY3TEs2Y2tlWmd1TGNpOXpnMkdRUk8wZWhnRSs0UzNzQzdwN2IyejBTYlMKZysySzFhaW5FNzNxV3pyM3FKL2s0UHh1VmVTZVJFcmxPS2pVQmN6RjNpeFg5YUxoVnpEOUNxUXZDUTRTcWF2SQorRWJ5QWtCOFdFVjdGdWNCSGd4eFJxbm53c1ZHcVo1Z0ZydTZ3NU1ZQ1FLQmdRRC9sMWovSXd1a0Y3Q3R3aXBiCmtWQmJKN0k3VGUyUENOeHlSZTZVbVFRdjZCeXdzdllrVkJ6UHgzaWJ3RGtzczN0OTlhSmtnNVFBVXZMaStYWWUKRW5OUktrVmtQZTYyMThVdCtvK2xWMHVqdGNkMVN2RGZza3o1WjNFVXB2VzY0Sk1QSG01MzlFb200NGFkUVNVOQpPbGl2amxGRFB4bDdta0NlNjJ3TWxqNWgrUUtCZ1FDMlVLK3Uzc25iTGxxd0FxNlNkMnlrWlRSbGg3UXQ4cVo0ClE0RVNQbHMvODJFMEZOeXJKQnlmajhhQzk3KzVXc0h4T3hYa0NkQ2loMm95SHo3OGZuMWp4c3NnbjErNU4xMlMKN1dYRTd0UXg1b0FMbXArZ2tLbWFuQ2g4aDFXUXpjNW1VRmNDbDlWbkJRNE5nTlRzN1Z3ZUMxUWNjdTVCVGpKNgpsWG5QWStwQlJ3S0JnRWdLT0tOeEZVN2kvb0Y3UU1VZWFDL0pPSXRPbzY2MUtLMXMySm81cHR4NTU5eEdGT3FZCmFxYUY2cEM3MVdHc0ZScWpxV243VERQckxWcWNSWmFrVEt5TDVaTWpnYTAzay9RcHMyTSsvNnJkQ2FNZHhGYkkKd3JscFZvNGpvUTlUVFIvdHkrVXpqZEw0UG5FRWM2S1dWM2pqL0RPZ0RXYXhTNVYvZGF6MmpBN0JBb0dCQUxBWgp1Y2k0cTlQSnI1RkoxZVBsOVF2S01kVk1RaThMcmQ2R25icklmbzJFQWNVenpYNkYwQndUM1ZTT0NDUDYxVkMwCm1NMSs2WWJFTkVDYnk5UktQY1hOdEZFYmdIUEFWZU5nNmFkU1phYVpsZDhmaU1hb1ZScmhjWDN2bnlmU2syWmkKZmRra1hkNDNsTDVZVGROWTVseWk1cWpnSHVEM3JteVh5MlFXaDZSckFvR0FPZEkwdWVPK3V4RlVaMkRwVHlMdApxRHlNMlAyeFp1WTNTM2NVZG51Wk1uTnVjdG4yWUNoelhaZzkrVUtRMTF5a2hObzFlaDhRUmdhZU8wUEtvQ21uClNFWXNIVEh1V2pzVkd5clgzb1A1UkhJYXh3bThFNW1qaGVoQU9EbnpISGx2T3JOekk3YzhDbzFjVGZKYWRXcjgKd3AyQWM5TlRMaVdvOGxUK1BuTnhrSEU9Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0K
tls.crt: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUZQekNDQXllZ0F3SUJBZ0lVV1NwK0g2R0dDV0RxNGJkZmZBbXhnQ3IvSjZZd0RRWUpLb1pJaHZjTkFRRUwKQlFBd2ZqRUxNQWtHQTFVRUJoTUNTVTR4Q3pBSkJnTlZCQWdNQWsxSU1SQXdEZ1lEVlFRS0RBZFRZVzVrWW05NApNUkV3RHdZRFZRUUxEQWhUWldOMWNtbDBlVEVZTUJZR0ExVUVBd3dQU1c1MFpYSnRaV1JwWVhSbElFTkJNU013CklRWUpLb1pJaHZjTkFRa0JGaFJ6WldOMWNtbDBlVUJ6WVc1a1ltOTRMbTVsZERBZUZ3MHlOVEEwTURjeU1qRTUKTXpsYUZ3MHlOakEwTURjeU1qRTVNemxhTUlHRU1SQXdEZ1lEVlFRRERBZHpZbWgwZEhCek1SQXdEZ1lEVlFRSwpEQWRUWVc1a1ltOTRNUkF3RGdZRFZRUUxEQWRUWlhKMlpYSnpNUTB3Q3dZRFZRUUhEQVJRZFc1bE1Rc3dDUVlEClZRUUlEQUpOU0RFTE1Ba0dBMVVFQmhNQ1NVNHhJekFoQmdrcWhraUc5dzBCQ1FFV0ZITmxZM1Z5YVhSNVFITmgKYm1SaWIzZ3VibVYwTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUF0Z1luK0QvSQo2SUhIcmtWL3B2azVGZm9lOUZPUm9PM2NITDdtdGlyNjZSN2pXMVV3eUN1VGRGbFNRckNvT2xtUzFSODB2SGVnCkFQTC80cWlRd0xkZEwzT09ZZGtITWJiZXEwcXEwQXBnZlpIbVl6cWswY1JxWEF5OHlaRU5rSGQ3NVV1SHlUMUwKZStxZFhBSlZqcEN6NEp5WnpnbkpmU0UyN3d6RlJQQWRSK2NNT3pqSTA5QThDSlJCNGFTS3FaNE1FR3JLNkVDcgorSHl6MG4zYXdjOCtFRHVoLzAvUVNzTm8zREliZGFxUHVLYlhJWnJtL3FKM3NtRGpJS1lPTXM1YThqN1RWdnhECjRkeVNaYXBYNi9CdnFGTldMVUcwUkJqN2NwSDNYSEZ1Y0FPRHJvc2lKOUNEaWpIYmU3MGRKQmZMRGxHcmh0R1YKL1JJa0FvZmpXc2RsRHdJREFRQUJvNEd0TUlHcU1CTUdBMVVkSlFRTU1Bb0dDQ3NHQVFVRkJ3TUJNRk1HQTFVZApFUVJNTUVxQ0NXeHZZMkZzYUc5emRJSUhjMkpvZEhSd2M0SVRjMkpvZEhSd2N5NXpZVzVrWW05NExtNWxkSUlOCktpNXpZVzVrWW05NExtNWxkSWNFZndBQUFZY0V3S2dKZ0ljRXdLZ2VnREFkQmdOVkhRNEVGZ1FVTzROaEtSSG4KMHVjclJhUzZ1MWVzWm9veFZ0d3dId1lEVlIwakJCZ3dGb0FVc0ZuNTdoaWlnYXlOWnBJRGtCUjk4aEJQSUtJdwpEUVlKS29aSWh2Y05BUUVMQlFBRGdnSUJBS2FCNndlQWN0TFJ4MTZXMnJ6ODhEVHJtdU9kNDRyUDFNQ3VOQnBLCnU2eDk1elZDQnJSN3oycXhMUTJ1NUgzRjVjTXQrbHgxRVB4K3ZhWjVuMWJtWGR4Y0FQUmtkU2pua3F2YXZUSWsKZnRBSnhKYmF0eEJlZXhIRiszQkc5SEhWbldCTUFtZlNOcm04cnR2eXNyL0NpS1BHc1BjbzhhdTA1REdRN2N0bQpCMDZkc05XWWZucjlMRUIrclgvR3N6WXNwY0lFRDFOUFpjZytyQkNyeW9FZU15VURneFp2M24xZ0thdjg2SkxDCnpuaFZad05ZM3IzVkRLM0x4WDM1OHEva1pvckp3T0JzNFlHKzUyL2E3b1FvSVJidjZhNWtURGpKNWJxL2I2MTEKcjYzWWxnS2hZWjQ4dm9IQ3FLSlFUcW5RV3A2L0UzRGNzNDZSdUZEZmZtQmdXbHdPRTVEZTFRdnY1ektGM2t1QQpwWGVxRFRndFFKUURBVkJXNjlaQ1NmLzlKVUFiblBadFlROUo2czhoVkR1SlhaZWZrQW44SnFPSVdlS3ZjVjBPCmtDakUxb1hBNzNXNnJVdzJFSFgra2ZESUlPNE1EQVhjeC84OGRlT1NiUHpkQWs3TmZkUXhmYVYyUi9wclQyblMKMVlGYUxJRW0rUkhwaTBHZWJud3U1MytseU5ycEYwR3dFTG5DNFVLWVptQnV0bys1bi9BZ1BuWGhDUkdLRTl4ZAp4TjRieVdtTHhEU3Byamc1QVJVc25jNjVqZ09oZmhQMWVwOElJRjRoUVlZd1p1aGJYL204aUtVaDU4dmZ1aXR0CnFQR1FIZXJ3WGNhSldtaENsQUhDQk9xT01MR2VTMndIdkxBamxnWWd1ZjRoS1M0OGgyaDRUTEl0RlpXOEkxa1oKajBXVgotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==
ca.crt: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUY1akNDQTg2Z0F3SUJBZ0lDRUFBd0RRWUpLb1pJaHZjTkFRRUxCUUF3Z1lVeEVEQU9CZ05WQkFNTUIxSnYKYjNRZ1EwRXhFREFPQmdOVkJBb01CMU5oYm1SaWIzZ3hFVEFQQmdOVkJBc01DRk5sWTNWeWFYUjVNUTB3Q3dZRApWUVFIREFSUWRXNWxNUXN3Q1FZRFZRUUlEQUpOU0RFTE1Ba0dBMVVFQmhNQ1NVNHhJekFoQmdrcWhraUc5dzBCCkNRRVdGSE5sWTNWeWFYUjVRSE5oYm1SaWIzZ3VibVYwTUI0WERUSTFNRFF3TnpJeU1URXdNbG9YRFRNMU1EUXcKTlRJeU1URXdNbG93ZmpFTE1Ba0dBMVVFQmhNQ1NVNHhDekFKQmdOVkJBZ01BazFJTVJBd0RnWURWUVFLREFkVApZVzVrWW05NE1SRXdEd1lEVlFRTERBaFRaV04xY21sMGVURVlNQllHQTFVRUF3d1BTVzUwWlhKdFpXUnBZWFJsCklFTkJNU013SVFZSktvWklodmNOQVFrQkZoUnpaV04xY21sMGVVQnpZVzVrWW05NExtNWxkRENDQWlJd0RRWUoKS29aSWh2Y05BUUVCQlFBRGdnSVBBRENDQWdvQ2dnSUJBS2dhb2JTSmZVMGdZc29OTHdYWVdlMHVGckZ2Zks5SgpLOGJmUjRjQ3djS3dlZ0VPUTc5aXM4U05hQTFKOWh6aU9OcGRrSDNUSFB2YlFqeFA0bjB6cFRqUlVVcS82SWhnCjRVakNlak1DR3ZUcktDTHlONUJPV2c3b0wwQmV1d3NIaDFtZnhkWnFtS3ZCbHNoNWRmZXpQbTlGSWZxQzRxNnMKVFpVS1YzRHNZdVZJczBncC9aMFVVVk5UdmVrdm1Ob2w3bFR5aVAvQWRsM2lyRVdJSnk1UHRhbzRua2kydHdSego5bHk3QmRQaDdUTWUzZ3l2azVBdmZ3ZTl0K1ZEM0VqblZUb0owVHJaTWRyQ2NxS2lWNWplTWtXVVlRWXJPOHo2ClJYR2ZMM0ozWUZJaGlqMlhVTFBnQ1gwYmtDYmxLb0dSZVZtaUFkU1pOZEtGM1l3ZzNxdkFCRU1XeFBSWk9rM2oKRy80cnZsWGJabW5BWjJUdGF6ZXdDY3VSVHkrUU9NR2t5SjROUTFESlRIVXNyc3FGc0E2dmNGcEtZMkJzbFk2MwpaNVJMMDVuRUhPbUZRZ2Zla3BzejB1RHJmMUtXb3RwNG1CdWQzd2dIM0NiS2dLM05aOWg3OSs0K3VMak5UcjFpCjhFL2JUYTBaTlFnWGdScGZ2T2E0aktybkxzWjFuTHh6SDRGbDlVSjN6UWNjR1NkVGovVE51UlBEajd5aGRIMFkKd3Z4QWZGaUtremd0RlRsTGhoS21uMHhHb0I3YTFCUEpPdGtyc0RscGlvZ2FGKzk1Um9mU2k2UzE2WkU2enJURApoYk9EV3dqUDl1eVRBQjY4ZkRZTVNPMWZRRkdWRnY4TGhFeC9yWmYzY3A0NjZpNnpGUW9meWRydnl4NkQ2akMyCmVTWUVpdkZUK3YvbEFnTUJBQUdqWmpCa01CMEdBMVVkRGdRV0JCU3dXZm51R0tLQnJJMW1rZ09RRkgzeUVFOGcKb2pBZkJnTlZIU01FR0RBV2dCUnM0VDFpNnFEL0dvWXIrVG5LTnlCaGg0VEh3akFTQmdOVkhSTUJBZjhFQ0RBRwpBUUgvQWdFQU1BNEdBMVVkRHdFQi93UUVBd0lCaGpBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQWdFQVV0Q0kzU1B1Cko2Kzh5b2xDYUs0VmRyckdiWHRmSTU4SURsVGQ1bm1jbTlvZHBGb0VzT1ora0g5bmxrMkp0UE4zcUZxRlVydzcKYVl4TXEralZqRGVvSmdsQnV3eFNnVUh3b2o3QXU5VU84UzV5MUd4NnRsL3hpRWk0M3JLaTdYRlRVMlYwRW9qLwphenR2eVlqQWVzZ3h4bU1idm5Fclp3WUZpUXhMaGRjSEx4clRueDhYQVJ3ODd4VTE4N0wvdmcvRUt6Y2dCS0pvCjF2QUxKNFJZVlhEKzhpdng1bUNCUXlzQUpaZnFlWXhUUmZWT2pRNytYRVo3Y2JkUjh0RmR2NTlrZzlyaXBsaFkKN0tVRTh6Rm9XUHM0ZkxvSDUvS3dJcGorTk4rR1RUOXNsMGZENkNDN0hWVmdMZEZCTU1POURxOTVyemx5TlpHZQpmamNteWtKZnh5eXNoVlJrYXFqMW1lRmlQeExWN2lpTnVJMHIyTmgzVFFrNFdYRWQ4RTBIdk1EcXVMV1lpSVJvCjhTYkRVWTF4Y2pwTHgrNVdsaTVQVE9zUHlDTWs0dTFXbFJ1cHQrUTVlKzV0RlhjdFZVV1JKdFBnVTV4SG9nL1gKaUloakV4S216NmRxR2g4Tm1uejViajVicjdWYW82L2VNdnVscDJDV1lUOWpvLzJvSzhaa0RDRVJoQkZETTNOSApmYzU4cENXWGE2bVQ5cnhFL2FsaGxRQkt4TWc0TWVkdWM5b2R5bXZRK1d3ampGMlFDK0pudE1aNnJvdzhNZVdzClZ3K0JHZGw3VThPc21oQldmbzJnenAycE0wZlRKWXp0SU42clRvQXV4bnJzYVd1RUZEcmJyeGlQcmVONkJnLzUKMVZabGdDcU53UTducVYvYXlJcWJQOXpyUzJ2TmYvOU9JMjQ9Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0KLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUYvVENDQStXZ0F3SUJBZ0lVQzUrRXhvYnAyTHUrek5XRGN5VnRZTzNiMDNZd0RRWUpLb1pJaHZjTkFRRUwKQlFBd2dZVXhFREFPQmdOVkJBTU1CMUp2YjNRZ1EwRXhFREFPQmdOVkJBb01CMU5oYm1SaWIzZ3hFVEFQQmdOVgpCQXNNQ0ZObFkzVnlhWFI1TVEwd0N3WURWUVFIREFSUWRXNWxNUXN3Q1FZRFZRUUlEQUpOU0RFTE1Ba0dBMVVFCkJoTUNTVTR4SXpBaEJna3Foa2lHOXcwQkNRRVdGSE5sWTNWeWFYUjVRSE5oYm1SaWIzZ3VibVYwTUI0WERUSTEKTURRd056SXlNVEF4TTFvWERUUTFNRFF3TWpJeU1UQXhNMW93Z1lVeEVEQU9CZ05WQkFNTUIxSnZiM1FnUTBFeApFREFPQmdOVkJBb01CMU5oYm1SaWIzZ3hFVEFQQmdOVkJBc01DRk5sWTNWeWFYUjVNUTB3Q3dZRFZRUUhEQVJRCmRXNWxNUXN3Q1FZRFZRUUlEQUpOU0RFTE1Ba0dBMVVFQmhNQ1NVNHhJekFoQmdrcWhraUc5dzBCQ1FFV0ZITmwKWTNWeWFYUjVRSE5oYm1SaWIzZ3VibVYwTUlJQ0lqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FnOEFNSUlDQ2dLQwpBZ0VBdTI3cGxvUlh5UkdVYnprakd1b2ViVEJCMjB0R25TUVNxMEhGZ3pTQ0tXckhIZnRzYXdFTkNvaXB1QkFPCkVkb0R3b2N6ODQ3dDM5S1hZMjArRjd1d2pFSmE5SmJ3RUNIekF0Umd4SE8vSEMyZlY2RVJqaWVVVUJTSVlsWU4KaWVSRGlNYnRGblhXdkl5bEFkakR4ak9oRlJNNUQ1MTR1YXBmVU15QmhyZjFtcUxQNDU0QTJEbjVCK2ZOS1ZOaApHUHBwQTByV28zVjM4V2hMWUs1ZnNybHJZOXZpQ3FxZnpBYTVodWhiRFhjWnZ1bXgyMVRQZGN6MHJySTMvTy9nCmVzU2NlSUxKQXpSZFNIalc3cVZBY3RINmc0UWpVMitpUDBXMVNvdkszek1hNjB2T3BOdk56eFVEWitNTHp0bDgKWEQzanhsaGFSYTRDY1VVaURVekxERlp5emhqUGhPTDlpMkMrYk94ZWhOaU9QVG84emdldUlPQkd5cm40ZHpjWgpEYUFVUFdib3AvWlplM0hBblJHZjJGZFlGQzBidjIwbkI5QS9MNmwyTlQwV3NESHh6ZHJ2VjV0UGVpQTJtM085CitCMG1qZ1hpQ244aDRwSzBRU2laQnhSbG00dVliTkhBTm05ZG1Yd05KWnoxR2dwNDdqa1ozVjVmWXRuTEdVRFkKK29lTEZBWTYzMGU2NkdmVStzeWJlMHpMUXNzYzdQK0lQMGQ2K1VKcytLS3NlOVRZS3JNSGVXOEs1VmpEYndNegorSXhKVWN0QU9xTFFnMk5DMVdmK3hsNStadG56WlpHblZaamFXaDBRNzRxMzZUZXpTMGNvK1BwSWgydGtrZTc1CnVZTjFwOXlIcFFiT3cvM0pTdHB5TFdQbXZCSnc1REpUVnRtU3J1L3VkSUdUVEcwQ0F3RUFBYU5qTUdFd0hRWUQKVlIwT0JCWUVGR3poUFdMcW9QOGFoaXY1T2NvM0lHR0hoTWZDTUI4R0ExVWRJd1FZTUJhQUZHemhQV0xxb1A4YQpoaXY1T2NvM0lHR0hoTWZDTUE4R0ExVWRFd0VCL3dRRk1BTUJBZjh3RGdZRFZSMFBBUUgvQkFRREFnR0dNQTBHCkNTcUdTSWIzRFFFQkN3VUFBNElDQVFCcXlpclA5bXF5VnBic1FBa0RiNU5JNGRyWDJITHhXbUFGcFdGYnQ1eXUKSXQ5a0VTZmtESUd3U0E2VU84QkhMNkkzVGk2RVRDYWIzcU9OU01iWFVycTkrT0FzcDlEL0pyMlIwQUEyOG4ybwpBUzVBUUtYK2JYaXppUjYyYzNJU3JaTzVWdlE4dEF1TGl2cHRqb1lqRDZpdEZtckxUNUUvMG9UY3FtNDhmMjRNCnp5VUtZVzk1c0xvUkxUcHRUenNya1dKakE3TW9SQW1nYWZtR0NhRVdhVXBHaU1taE5zT3Z3Q25CZlErNVIybU4KR3FlV1Z4ZUFJc0w4a2RGZFhyNVhpeDdqcWQwMWJXSFNPVU92S29nc1ZLVnA1RWliVFY0Mm93ZXBRcmRaV2JpWQpRNzU2WVo1MGRtYzZCWVQwTVBJWUFYMjNqRTh1bG8zZ3B2ZUp5dmEydmpxY1VJZGZ3bzFjTjRmUm9BUEZkZTlNCmxUVTRIRDgzNVJMd1N0U2JvdVB1M2tHUHZtOU5iOEV3YTdUUFJwUGJ1eW5CMVdnM2JvRjJoenB3SHZpMmtEazMKZWtseHJTR3lNL2lnUS9KYTZYdVBHdytBYUNWSmlaRXpHRGswVHJIeE03bjIrajc2U054OUo1NjZ2NnEyU3E5NQp5NzFZSUN6WlF6emVPVUdFd0Z5NHh3czR2UytBOUdXSVBIU1VqU1gvNnVQYTUwcjY3UU8rLzQ1YmNvcWw2S2VrCkN5dE0wUHpKR0dzS0cxckgzUzlaUU9CY2htd0hZcWMvUU1ldzBoRWIzb3owYk0rbG5tQlBxYzVsS0pIZStMZlAKNEpzSkNBN045cXU1elF1Q09DQStMUUpVbkhMNWZ2RzVzb1d4L3lSZlhvVHVnQVdiamwveG56cEVFcUdBa0NRaQpBdz09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K
---
# Source: bd-spring-module/templates/secrets.yaml
#
# kubectl -n sb-apps create secret docker-registry docker-reg-cred --docker-server=https://index.docker.io/v1/ --docker-username=brijeshdhaker --docker-password=Accoo7@k47 --docker-email=brijeshdhaker@gmail.com --dry-run=client --output=yaml
# kubectl get secret docker-reg-cred --output="jsonpath={.data.\.dockerconfigjson}" | base64 --decode
# kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "docker-reg-cred"}]}'
#
