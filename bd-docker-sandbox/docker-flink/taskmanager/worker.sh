# Start the flink task manager (slave)
echo "Configuring Task Manager on `hostname -i` Node."
FLINK_NUM_TASK_SLOTS=${FLINK_NUM_TASK_SLOTS:-`grep -c ^processor /proc/cpuinfo`}
JOB_MANAGER_RPC_ADDRESS=`host ${JOB_MANAGER_RPC_ADDRESS} | grep "has address" | awk '{print $4}'`

#sed -i -e "s/jobmanager.rpc.address: localhost/jobmanager.rpc.address: ${JOB_MANAGER_RPC_ADDRESS}/g" $FLINK_HOME/conf/flink-conf.yaml
#sed -i -e "s/taskmanager.numberOfTaskSlots: [0-9]\+/taskmanager.numberOfTaskSlots: ${FLINK_NUM_TASK_SLOTS}/g" $FLINK_HOME/conf/flink-conf.yaml
#echo "blob.server.port: 6124" >> $FLINK_HOME/conf/flink-conf.yaml
#echo "query.server.port: 6125" >> $FLINK_HOME/conf/flink-conf.yaml

echo "Starting Task Manager"
$FLINK_HOME/bin/taskmanager.sh start

echo "Config file: " && grep '^[^\n#]' $FLINK_HOME/conf/flink-conf.yaml
echo "Sleeping 10 seconds, then start to tail the log file"
sleep 10 && tail -f `ls $FLINK_HOME/log/*.log | head -n1`
