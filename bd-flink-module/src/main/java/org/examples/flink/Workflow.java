package org.examples.flink;

import org.examples.flink.config.WorkflowConfig;
import org.examples.flink.utils.Constants;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

public class Workflow {

    protected WorkflowConfig workflowConfig;
    private StreamExecutionEnvironment session = null;

    public Workflow(WorkflowConfig workflowConfig){
        this.workflowConfig = workflowConfig;
    }

    protected StreamExecutionEnvironment flinkSession() {

        if(this.session != null){
            return this.session;
        }

        if(this.workflowConfig != null) {


            Map<String,String> workflowConf = workflowConfig.workflowConf();
            //String currentJobName = flinkConfig.get("workflow.app.name");

            StreamExecutionEnvironment env;
            String engine_type = workflowConf.get(Constants.ENGINE_TYPE);

            Configuration flinkConfig = workflowConfig.flinkConf();
            flinkConfig.set(RestOptions.PORT, 8881);
            switch (engine_type){
                case Constants.LOCAL_CLUSTER :
                    FileSystem.initialize(flinkConfig);
                    env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(flinkConfig);
                    break;
                case Constants.MINI_CLUSTER:
                    FileSystem.initialize(flinkConfig);
                    env = StreamExecutionEnvironment.getExecutionEnvironment(flinkConfig);
                    break;
                case Constants.REMOTE_CLUSTER:
                    env = StreamExecutionEnvironment.getExecutionEnvironment();
                    break;
                default:
                    env = StreamExecutionEnvironment.getExecutionEnvironment();
            }
            env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
            env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

            this.session = env;

        }

        return this.session;
    }

    protected MiniCluster getMiniCluster() {

        Configuration config = workflowConfig.flinkConf();
        config.set(RestOptions.PORT, 8881);
        final MiniClusterConfiguration cfg =
                new MiniClusterConfiguration.Builder()
                        .setNumTaskManagers(1)
                        .setNumSlotsPerTaskManager(4)
                        .setConfiguration(config)
                        .build();
        return new MiniCluster(cfg);

    }
}
