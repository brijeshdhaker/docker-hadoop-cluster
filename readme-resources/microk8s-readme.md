#
sudo snap install microk8s --classic

#
sudo microk8s start

#
sudo microk8s status --wait-ready

#
mkdir -p ~/.kube/
sudo microk8s.config  > ~/.kube/config

#
sudo microk8s stop

#
microk8s enable dns
microk8s enable hostpath-storage
microk8s enable dashboard
microk8s enable helm3
microk8s enable metallb
microk8s enable metrics-server
microk8s enable prometheus



#
sudo usermod -a -G microk8s $USER
cd $HOME
mkdir -p ~/.kube
chmod 0700 ~/.kube
cd ~/.kube
microk8s config > config

#
alias kubectl='microk8s kubectl'

#
microk8s ctr images ls
microk8s ctr image import - < nginx.tar

microk8s images import < nginx.tar
microk8s images export-local > images.tar

#
microk8s kubectl get nodes
microk8s kubectl get services
microk8s kubectl get pods
microk8s kubectl create deployment nginx --image=nginx --dry-run=client -o yaml