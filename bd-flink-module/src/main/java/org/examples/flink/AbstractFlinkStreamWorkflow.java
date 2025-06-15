package org.examples.flink;

import org.examples.flink.config.WorkflowConfig;
import org.examples.flink.utils.Constants;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFlinkStreamWorkflow extends Workflow {

    public AbstractFlinkStreamWorkflow(WorkflowConfig workflowConfig){
        super(workflowConfig);
    }

    private JobExecutionResult runJobInBackground(StreamExecutionEnvironment env) throws Exception {
        AtomicReference<JobExecutionResult> results = null;
        new Thread(() -> {
            try (MiniCluster miniCluster = getMiniCluster()) {
                miniCluster.start();
                results.set(miniCluster.executeJobBlocking(env.getStreamGraph().getJobGraph()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).start();
        return results.get();
    }

    public JobExecutionResult startWorkflow(String workflow_name) throws Exception {

        Map<String,String> workflowConf = workflowConfig.workflowConf();
        StreamExecutionEnvironment env = createPipeline();
        String engine_type = workflowConf.get(Constants.ENGINE_TYPE);
        if(engine_type.equalsIgnoreCase(Constants.MINI_CLUSTER)) {
            env.getStreamGraph().setJobName(workflow_name);
            return runJobInBackground(env);
        }else{
            return env.execute(workflow_name);
        }

    }
    public JobExecutionResult startWorkflow() throws Exception {

        Map<String,String> workflowConf = workflowConfig.workflowConf();
        String workflow_name = workflowConf.get(Constants.WORKFLOW_NAME);

        return startWorkflow(workflow_name);

    }

    public abstract StreamExecutionEnvironment createPipeline() throws Exception ;

}
