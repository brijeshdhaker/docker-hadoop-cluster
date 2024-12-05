/wait-for-step.sh

echo "Submit application ${FLINK_APPLICATION_JAR_LOCATION} with main class ${FLINK_APPLICATION_MAIN_CLASS} to Flink master"
echo "Passing arguments ${FLINK_APPLICATION_ARGS}"
JOB_MANAGER_RPC_ADDRESS=`host ${JOB_MANAGER_RPC_ADDRESS} | grep "has address" | awk '{print $4}'`
JOB_MANAGER_RPC_PORT=6123

/execute-step.sh

$FLINK_HOME/bin/flink run -c ${FLINK_APPLICATION_MAIN_CLASS} -m $JOB_MANAGER_RPC_ADDRESS:$JOB_MANAGER_RPC_PORT \
    ${FLINK_APPLICATION_JAR_LOCATION} ${FLINK_APPLICATION_ARGS}

/finish-step.sh
