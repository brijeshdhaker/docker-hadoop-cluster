#!/bin/bash

echo "Configuring Job Manager on `hostname -i` Node."
#sed -i -e "s/jobmanager.rpc.address: localhost/jobmanager.rpc.address: `hostname -i`/g" $FLINK_HOME/conf/flink-conf.yaml
#sed -i -e "s/jobmanager.bind-host: localhost/jobmanager.bind-host: 0.0.0.0/g" $FLINK_HOME/conf/flink-conf.yaml
#sed -i -e "s/rest.address: localhost/rest.address: `hostname -i`/g" $FLINK_HOME/conf/flink-conf.yaml
#sed -i -e "s/rest.bind-address: localhost/rest.bind-address: 0.0.0.0/g" $FLINK_HOME/conf/flink-conf.yaml
#echo "blob.server.port: 6124" >> $FLINK_HOME/conf/flink-conf.yaml
#echo "query.server.port: 6125" >> $FLINK_HOME/conf/flink-conf.yaml
export JVM_ARGS="$JVM_ARGS -Djava.security.krb5.conf=/etc/krb5.conf"

${FLINK_HOME}/bin/jobmanager.sh start #cluster #local
echo "Cluster started."

echo "Config file: " && grep '^[^\n#]' ${FLINK_HOME}/conf/flink-conf.yaml
echo "Sleeping 10 seconds, then start to tail the log file"
sleep 10 && tail -f `ls ${FLINK_HOME}/log/*.log | head -n1`

