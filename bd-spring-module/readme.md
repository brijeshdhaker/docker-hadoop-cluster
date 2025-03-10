-Dspring.profiles.active=cloud

helm create bd-spring-module/helm --namespace AA100121

helm template bd-spring-module ./bd-spring-module/helm --namespace AA100121 --dry-run --debug --set favoriteDrink=slurm

helm install bd-spring-module ./bd-spring-module/helm --namespace AA100121 --dry-run --debug --set favoriteDrink=slurm