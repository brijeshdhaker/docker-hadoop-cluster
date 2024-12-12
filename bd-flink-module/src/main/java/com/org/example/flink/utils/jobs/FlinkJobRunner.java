package com.org.example.flink.utils.jobs;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.File;
import java.net.URL;
import java.util.Map;

public interface FlinkJobRunner {

    default MiniCluster getMiniCluster(Map<String, String> params) {

        URL url = ClassLoader.getSystemResource(".");
        File file = (url != null) ? new File(url.getFile()) : new File(".");
        String config_path = params.get("config-path");
        if(config_path != null && !config_path.isBlank()){
            file = new File(config_path);
        }
        Configuration config = GlobalConfiguration.loadConfiguration(file.getAbsolutePath());
        //config.setString(RestOptions.BIND_PORT, "18081-19000");
        final MiniClusterConfiguration cfg =
                new MiniClusterConfiguration.Builder()
                        .setNumTaskManagers(2)
                        .setNumSlotsPerTaskManager(4)
                        .setConfiguration(config)
                        .build();
        return new MiniCluster(cfg);
    }

    default StreamExecutionEnvironment getStreamExecutionEnvironment(Map<String, String> params) {

        URL url = ClassLoader.getSystemResource(".");
        File file = (url != null) ? new File(url.getFile()) : new File(".");
        final org.apache.flink.configuration.Configuration config = GlobalConfiguration.loadConfiguration(file.getAbsolutePath());

        String engine_type = params.get("engine-type");
        if(!engine_type.equalsIgnoreCase("cluster")){
            FileSystem.initialize(config);
        }

        StreamExecutionEnvironment env = engine_type.equalsIgnoreCase("local") ?
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config) :
                StreamExecutionEnvironment.getExecutionEnvironment();

        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        return env;
    }

    StreamExecutionEnvironment createPipeline(Map<String, String> params);


}
