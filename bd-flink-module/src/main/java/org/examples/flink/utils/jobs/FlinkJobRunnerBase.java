package org.examples.flink.utils.jobs;

import org.examples.flink.utils.Constants;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

public abstract class FlinkJobRunnerBase implements FlinkJobRunner {

    public static final String WORKFLOW_NAME = "workflow-name";
    public static final String ENGINE_TYPE = "engine-type";
    public static final String CONFIG_PATH = "config-path";

    public Configuration loadConfig(Map<String, String> params){

        String config_dir = params.getOrDefault(CONFIG_PATH, "/opt/flink/conf");
        /*
        URL url = ClassLoader.getSystemResource("/opt/flink/conf");
        File file = (url != null) ? new File(url.getFile()) : new File("/opt/flink/conf");
        if(!params.containsKey("config-path")){
            file = new File(params.get("config-path"));
        }
        */
        String engine_type = params.get(ENGINE_TYPE);
        return engine_type.equalsIgnoreCase(Constants.REMOTE_CLUSTER) ?
                GlobalConfiguration.loadConfiguration() :
                GlobalConfiguration.loadConfiguration(config_dir);
    }


    public MiniCluster getMiniCluster(Map<String, String> params) {

        Configuration config = loadConfig(params);
        config.set(RestOptions.PORT, 8881);
        final MiniClusterConfiguration cfg =
                new MiniClusterConfiguration.Builder()
                        .setNumTaskManagers(1)
                        .setNumSlotsPerTaskManager(4)
                        .setConfiguration(config)
                        .build();
        return new MiniCluster(cfg);
    }


    public StreamExecutionEnvironment getStreamExecutionEnvironment(Map<String, String> params) {

        StreamExecutionEnvironment env;
        String engine_type = params.get(ENGINE_TYPE);
        Configuration config = loadConfig(params);
        config.set(RestOptions.PORT, 8881);
        switch (engine_type){
            case Constants.LOCAL_CLUSTER :
                FileSystem.initialize(config);
                env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config);
                break;
            case Constants.MINI_CLUSTER:
                FileSystem.initialize(config);
                env = StreamExecutionEnvironment.getExecutionEnvironment(config);
                break;
            case Constants.REMOTE_CLUSTER:
                env = StreamExecutionEnvironment.getExecutionEnvironment();
                break;
            default:
                env = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);
        return env;
    }

    public void runJobInBackground(StreamExecutionEnvironment env, Map<String, String> params) throws Exception {
            new Thread(() -> {
                try (MiniCluster miniCluster = getMiniCluster(params)) {
                    miniCluster.start();
                    miniCluster.executeJobBlocking(env.getStreamGraph().getJobGraph());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }).start();
    }

    public void run(Map<String, String> params) throws Exception {

        //2,3
        StreamExecutionEnvironment env = createPipeline(params);

        String engine_type = params.get(ENGINE_TYPE);
        if(engine_type.equalsIgnoreCase(Constants.MINI_CLUSTER)) {
            runJobInBackground(env,params);
            //env.execute(params.get(WORKFLOW_NAME));
        }else{
            env.execute(params.get(WORKFLOW_NAME));
        }

        //Utils.runSourceTableUpdater(workPath);

    }

}
