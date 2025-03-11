kubectl -n kube-system get pods -l k8s-app=calico-node -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app=calico-node' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app in (calico-node)' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app notin (qa)' -L k8s-app
kubectl -n kube-system get pods -l 'k8s-app notin (qa), tier in (app)' -L k8s-app

kubectl -n kube-system get pods --selector=batch.kubernetes.io/job-name=pi --output=jsonpath='{.items[*].metadata.name}'

kubectl create job hello --image=busybox:1.28 --dry-run=client -o yaml -- bin/sh -c "date; echo 'Hello World' " >> k8s-job.yaml
kubectl create cronjob hello --image=busybox:1.28 --schedule="*/1 8 * * * *" --dry-run=client -o yaml -- bin/sh -c "date; echo 'Hello World' " >> cron-job.yaml

kubectl apply -f - <<EOF
apiVersion: v1
kind: Service
metadata:
creationTimestamp: null
labels:
run: app-springboot
name: app-springboot
namespace: i100121
spec:
ports:
- port: 80
  protocol: TCP
  targetPort: 8080
  selector:
  run: app-springboot
  type: NodePort
  status:
  loadBalancer: {}
EOF
