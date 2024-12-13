package com.org.example.flink.utils.jobs;

import com.org.example.flink.utils.Constants;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

public abstract class FlinkJobRunnerBase implements FlinkJobRunner {

    public static final String WORKFLOW_NAME = "workflow-name";
    public static final String ENGINE_TYPE = "engine-type";

    public Configuration loadConfig(Map<String, String> params){

        String config_dir = params.containsKey("config-path") ?  params.get("config-path") : "/opt/flink/conf";
        /*
        URL url = ClassLoader.getSystemResource("/opt/flink/conf");
        File file = (url != null) ? new File(url.getFile()) : new File("/opt/flink/conf");
        if(!params.containsKey("config-path")){
            file = new File(params.get("config-path"));
        }
        */
        return GlobalConfiguration.loadConfiguration(config_dir);

    }


    public MiniCluster getMiniCluster(Map<String, String> params) {

        Configuration config = loadConfig(params);
        //config.setString(RestOptions.BIND_PORT, "18081-19000");
        final MiniClusterConfiguration cfg =
                new MiniClusterConfiguration.Builder()
                        .setNumTaskManagers(2)
                        .setNumSlotsPerTaskManager(4)
                        .setConfiguration(config)
                        .build();
        return new MiniCluster(cfg);
    }


    public StreamExecutionEnvironment getStreamExecutionEnvironment(Map<String, String> params) {

        Configuration config = loadConfig(params);

        String engine_type = params.get(ENGINE_TYPE);
        if(!engine_type.equalsIgnoreCase(Constants.REMOTE_CLUSTER)){
            FileSystem.initialize(config);
        }

        StreamExecutionEnvironment env = (engine_type.equalsIgnoreCase(Constants.LOCAL_CLUSTER) || engine_type.equalsIgnoreCase(Constants.MINI_CLUSTER)) ?
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config) :
                StreamExecutionEnvironment.getExecutionEnvironment();

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

        if(params.containsKey("source-table-path")) {
            String src_tbl_path = params.get("source-table-path");
            System.out.println("sink table path : " + src_tbl_path);
            //Utils.prepareDirs(src_tbl_path);
        }

        if(params.containsKey("sink-table-path")) {
            String sink_tab_path = params.get("sink-table-path");
            System.out.println("sink table path : " + sink_tab_path);
            //Utils.prepareDirs(sink_tab_path);
        }

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
