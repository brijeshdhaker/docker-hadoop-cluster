#
### https://helm.sh/docs/intro/cheatsheet/
#
-Dspring.profiles.active=cloud

#
helm create ./bd-spring-module/helm-chart --namespace AA100121

#
helm template bd-spring-module ./bd-spring-module/helm-chart \
--namespace AA100121 \
--set author=brijeshdhaker@gmail.com \
--dry-run \
--debug \
--output-dir ./bd-spring-module/helm-chart/manifests/

#
helm package ./bd-spring-module/helm-chart --destination ./bd-spring-module/helm-chart/distro

#
helm template bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com \
--output-dir ./bd-spring-module/helm-chart/manifests/

#
helm install bd-spring-module ./bd-spring-module/helm-chart \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com \
--version 1.0.0 > ./bd-spring-module/helm-chart/manifests/bd-spring-module.yaml

#
helm install bd-spring-module ./bd-spring-module/helm-chart/distro/bd-spring-module-0.1.0.tgz \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com \
--version 1.0.0

#
helm uninstall bd-spring-module

#
helm upgrade bd-spring-module ./bd-spring-module/helm-chart \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com
--output-dir ./bd-spring-module/helm-chart/manifests/

#
helm repo add bd-spring-module https://nexus.repo.com --namespace AA100121 
helm repo update bd-spring-module https://nexus.repo.com