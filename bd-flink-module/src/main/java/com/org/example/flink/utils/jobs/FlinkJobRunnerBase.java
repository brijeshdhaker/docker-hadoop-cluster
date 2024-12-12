package com.org.example.flink.utils.jobs;

import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

public abstract class FlinkJobRunnerBase implements FlinkJobRunner {



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

        String engine_type = params.get("engine-type");
        if(engine_type.equalsIgnoreCase("mini-cluster")) {
            runJobInBackground(env,params);
        }else{
            env.execute(params.get("flink-workflow-name"));
        }

        //Utils.runSourceTableUpdater(workPath);

    }

}
