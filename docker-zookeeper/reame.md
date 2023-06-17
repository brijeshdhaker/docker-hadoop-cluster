#
#

kinit sandbox

$ /opt/zookeeper/bin/zkCli.sh -server zookeeper.sandbox.net:2181

[zk: zookeeper.sandbox.net:2181(CONNECTED) 0] create /testznode testznodedata sasl:sandbox@SANDBOX.NET:cdwra

[zk: zookeeper.sandbox.net:2181(CONNECTED) 0] getAcl /testznode