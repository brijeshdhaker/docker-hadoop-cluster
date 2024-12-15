package com.org.example.flink.transaction;

import com.org.example.flink.utils.jobs.FlinkJobRunnerBase;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

/*

--engine-type local-cluster --table-name transactions --config-path /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-flink-module/src/main/resources/local-cluster

*/
public class ProductsHourlySales extends FlinkJobRunnerBase {

    public static void main(String[] args) throws Exception {

    }

    @Override
    public StreamExecutionEnvironment createPipeline(Map<String, String> params) {
        return null;
    }
}
