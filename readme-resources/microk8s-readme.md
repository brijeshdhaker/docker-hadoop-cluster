#
sudo snap install microk8s --classic --channel=latest/stable

#
sudo microk8s start

#
sudo -S <<< "Accoo7@k47" microk8s start

#
sudo -S <<< "Accoo7@k47" microk8s status --wait-ready

#
#
sudo usermod -a -G microk8s $USER
cd $HOME
mkdir -p ~/.kube
chmod 0700 ~/.kube
cd ~/.kube
microk8s config > ~/.kube/config

# 
sudo microk8s stop
echo Accoo7@k47 | sudo -S -k microk8s stop


# Enable Addons
microk8s enable dns
microk8s enable hostpath-storage
microk8s enable dashboard
microk8s enable helm3
microk8s enable metallb
microk8s enable metrics-server
microk8s enable prometheus
microk8s enable ingress
microk8s enable cert-manager

# Community Supported 
microk8s enable community
microk8s enable istio

#
microk8s disable cert-manager

#
alias kubectl='microk8s kubectl'

#
# microk8s images 
#
microk8s ctr images ls
microk8s ctr images check
microk8s ctr images pull
microk8s ctr images push
microk8s ctr images remove
microk8s ctr images rm
microk8s ctr images label
microk8s ctr image import - < nginx.tar

microk8s images import < nginx.tar
microk8s images export-local > images.tar

#
microk8s kubectl get nodes
microk8s kubectl get services
microk8s kubectl get pods
microk8s kubectl create deployment nginx --image=nginx --dry-run=client -o yaml

#
/var/snap/microk8s/current
