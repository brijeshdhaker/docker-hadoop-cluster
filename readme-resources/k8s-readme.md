kubectl -n kube-system get pods -l k8s-app=calico-node -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app=calico-node' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app in (calico-node)' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app notin (qa)' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app notin (qa), tier in (app)' -L k8s-app