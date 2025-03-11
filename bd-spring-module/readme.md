-Dspring.profiles.active=cloud

helm create bd-spring-module/helm --namespace AA100121

helm template bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set favoriteDrink=slurm

helm template bd-spring-module ./bd-spring-module/helm/archives/repo/bd-spring-module-1.0.0.tgz \
--namespace AA100121 \
--dry-run \
--debug \
--set favoriteDrink=slurm
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