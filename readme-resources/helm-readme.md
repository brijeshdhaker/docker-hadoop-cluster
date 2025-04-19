### 
```bash
curl https://baltocdn.com/helm/signing.asc | gpg --dearmor | sudo tee /usr/share/keyrings/helm.gpg > /dev/null
sudo apt-get install apt-transport-https --yes
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/helm.gpg] https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
sudo apt-get update
sudo apt-get install helm
# or
sudo snap install helm --classic
```

# install metallb using helm
```bash
helm repo add metallb https://metallb.github.io/metallb
helm repo update
helm search repo metallb
helm show values metallb/metallb > ./bd-setup-module/kubernetes/metallb-values.yaml

helm install metallb metallb/metallb --namespace=metallb-system --create-namespace=true
# or
helm install -f ./bd-setup-module/kubernetes/metallb-values.yaml metallb metallb/metallb --generate-name

helm list --all-namespaces
helm ls -n metallb-system
helm status metallb -n metallb-system
helm history metallb -n metallb-system

helm uninstall metallb --namespace=metallb-system --purge
helm delete metallb --namespace=metallb-system --purge

```

#
docker run --rm -it \
  -p 8080:8080 \
  -e DEBUG=1 \
  -e STORAGE=local \
  -e STORAGE_LOCAL_ROOTDIR=/charts \
  -v $(pwd)/charts:/charts \
  ghcr.io/helm/chartmuseum:v0.14.0
  
### Install nginx ingress using helm
```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx
# OR
helm upgrade --reuse-values --install ingress-nginx ingress-nginx \
  --repo https://kubernetes.github.io/ingress-nginx \
  --namespace ingress-nginx \
  --create-namespace

helm show values ingress-nginx --repo https://kubernetes.github.io/ingress-nginx
helm show values ingress-nginx/ingress-nginx --repo https://kubernetes.github.io/ingress-nginx

# Get IP of the ingress service
IP=$(kubectl get services -n istio-system istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
curl -H "Host: foo.bar.com" http://$IP/testpath

# Upgrade nginx controller
helm upgrade --reuse-values ingress-nginx ingress-nginx/ingress-nginx

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


### Maven Plugin
```xml
<plugin>
    <groupId>com.kiwigrid</groupId>
    <artifactId>helm-maven-plugin</artifactId>
    <configuration>
        <chartDirectory>${project.basedir}</chartDirectory>
        <chartVersion>${helm.chart.version}</chartVersion>
        <useLocalHelmBinary>true</useLocalHelmBinary>
        <uploadRepoStable>
            <name>stable-repo</name>
            <url>${helm.repo.url}</url>
            <type>CHARTMUSEUM</type>
        </uploadRepoStable>
        <uploadRepoSnapshot>
            <name>snapshot-repo</name>
            <url>${helm.repo.url}</url>
            <type>CHARTMUSEUM</type>
        </uploadRepoSnapshot>
    </configuration>
    <executions>
        <execution>
            <id>helm-package</id>
            <phase>package</phase>
            <goals>
                <goal>dependency-build</goal>
                <goal>package</goal>
            </goals>
        </execution>
        <execution>
            <id>helm-upload</id>
            <phase>deploy</phase>
            <goals>
                <goal>upload</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

#
# Json PATH in Helm
#
config:
  app:
    name: myapp
    port: 8080
  database:
    host: localhost
    port: 5432

#
{{- $config := .Values.config | mustYaml | mustToJson -}}
appName: {{ get $config "config.app.name" }}
appPort: {{ get $config "config.app.port" }}
dbHost: {{ get $config "config.database.host" }}
dbPort: {{ get $config "config.database.port" }}


# config.json
{
"services": [
    {
    "name": "service1",
    "port": 8080
    },
    {
    "name": "service2",
    "port": 8081
    }
  ]
}

{{- $config := .Files.Get "config.json" | mustToJson -}}
apiVersion: v1
kind: ConfigMap
metadata:
name: myapp-config
data:
{{- range get $config "services[*]" }}
{{ .name }}.port: {{ .port | quote }}
{{- end }}

# filters
{{- $config := .Files.Get "config.json" | mustToJson -}}
{{- range get $config "services[?(@.port > 8080)]" }}
{{ .name }} has port greater than 8080
{{- end }}

# sorting
{{- $config := .Files.Get "config.json" | mustToJson -}}
{{- range sort (get $config "services[*]" "name") }}
{{ .name }}: {{ .port }}
{{- end }}