# cgroup v1
sudo vi /etc/default/grub
GRUB_CMDLINE_LINUX="cdgroup_enable=memory swapaccount=1 systemd.unified_cgroup_hierarchy=0"

#
# To boot the host with cgroup v2, add the following string to the GRUB_CMDLINE_LINUX line in /etc/default/grub and then run sudo update-grub:
GRUB_CMDLINE_LINUX="cdgroup_enable=memory swapaccount=1 systemd.unified_cgroup_hierarchy=1"

# Change cgroupV1
sudo dnf install -y grubby
sudo grubby --update-kernel="ALL" --args="systemd.unified_cgroup_hierarchy=0"


# SSH on to VirtualBox Virtual Machine
ssh root@sandbox-hdp.hortonworks.com -p 2200

# SSH on to HDP Cluster
ssh root@sandbox-hdp.hortonworks.com -p 2222

ambari-admin-password-reset

#
# Mysql Admin Password
#
root / hortonworks1

#
# Ranger Admin Password
#
admin / hortonworks1


#
# Hive URL
#

jdbc:hive2://sandbox-hdp.hortonworks.com:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2

beeline -u jdbc:hive2://sandbox-hdp.hortonworks.com:10000 -n hive -p


beeline -u jdbc:hive2://sandbox-hdp.hortonworks.com:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2 -n hive

