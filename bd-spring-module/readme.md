-Dspring.profiles.active=cloud

helm create bd-spring-module/helm

helm template bd-spring-module ./bd-spring-module/helm --dry-run --debug --set favoriteDrink=slurm

helm install bd-spring-module ./bd-spring-module/helm --dry-run --debug --set favoriteDrink=slurm