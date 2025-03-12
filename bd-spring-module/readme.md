#
### https://helm.sh/docs/intro/cheatsheet/
#
-Dspring.profiles.active=cloud

helm create bd-spring-module/helm --namespace AA100121

helm template bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--output-dir ./bd-spring-module/helm/k8s/ \
--set author=brijeshdhaker@gmail.com

#
helm package ./bd-spring-module/helm --destination ./bd-spring-module/helm/charts

#
helm template bd-spring-module ./bd-spring-module/helm/charts/bd-spring-module-0.1.0.tgz \
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
--set author=brijeshdhaker@gmail.com \
--version 1.0.0

#
helm install bd-spring-module ./bd-spring-module/helm/charts/bd-spring-module-0.1.0.tgz \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com \
--version 1.0.0

#
helm uninstall <name>

#
helm upgrade bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com
--output-dir ./bd-spring-module/helm/k8s/

#
helm repo add bd-spring-module https://nexus.repo.com --namespace AA100121 
helm repo update bd-spring-module https://nexus.repo.com