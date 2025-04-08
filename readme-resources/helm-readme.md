-Dspring.profiles.active=cloud

helm create bd-spring-module/helm --namespace AA100121

helm template bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com

helm template bd-spring-module ./bd-spring-module/helm/archives/repo/bd-spring-module-1.0.0.tgz \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com
--output-dir ./bd-spring-module/helm/k8s/

#
helm install bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set favoriteDrink=slurm \
--version 1.0.0

#
helm upgrade bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set favoriteDrink=slurm
--output-dir ./bd-spring-module/helm/k8s/

#
helm repo add bd-spring-module https://nexus.repo.com --namespace AA100121
helm repo update bd-spring-module https://nexus.repo.com

#
#
#
docker run --rm -it \
  -p 8080:8080 \
  -e DEBUG=1 \
  -e STORAGE=local \
  -e STORAGE_LOCAL_ROOTDIR=/charts \
  -v $(pwd)/charts:/charts \
  ghcr.io/helm/chartmuseum:v0.14.0
  

#
# Install nginx ingress using helm
#

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx

# OR

helm upgrade --install ingress-nginx ingress-nginx \
  --repo https://kubernetes.github.io/ingress-nginx \
  --namespace ingress-nginx \
  --create-namespace

helm show values ingress-nginx --repo https://kubernetes.github.io/ingress-nginx

# Upgrade nginx controller
helm upgrade --reuse-values ingress-nginx ingress-nginx/ingress-nginx

# Maven Plugin
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