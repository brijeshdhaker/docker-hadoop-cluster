#
### https://helm.sh/docs/intro/cheatsheet/
#
-Dspring.profiles.active=cloud
-Dspring.server.context=/api/v1

#
helm create ./bd-spring-module/helm-chart --namespace sb-apps

# template
helm template bd-spring-module ./bd-spring-module/helm-chart \
--namespace sb-apps \
--set author=brijeshdhaker@gmail.com \
--dry-run \
--debug \
--output-dir ./bd-spring-module/helm-chart/manifests/

#
helm template bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace sb-apps \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com \
--output-dir ./bd-spring-module/helm-chart/manifests/

# Distro
helm package ./bd-spring-module/helm-chart --destination ./bd-spring-module/helm-chart/distro

# Install
helm install bd-spring-module ./bd-spring-module/helm-chart \
--namespace sb-apps \
--debug \
--set author=brijeshdhaker@gmail.com \
--version 1.0.0 \
--dry-run 
> ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml

#
helm install bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace sb-apps \
--set author=brijeshdhaker@gmail.com \
--version 1.0.0 \
--debug \
--dry-run

#
helm uninstall bd-spring-module
helm show values ./bd-spring-module/helm-chart
helm show values ingress-nginx --repo https://kubernetes.github.io/ingress-nginx

#
helm upgrade bd-spring-module ./bd-spring-module/helm-chart \
--namespace sb-apps \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com
--output-dir ./bd-spring-module/helm-chart/manifests/

#
helm repo add bd-spring-module https://nexus.repo.com --namespace sb-apps 
helm repo update bd-spring-module https://nexus.repo.com