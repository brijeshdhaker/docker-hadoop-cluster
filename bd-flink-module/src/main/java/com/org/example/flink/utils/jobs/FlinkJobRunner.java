package com.org.example.flink.utils.jobs;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public interface FlinkJobRunner {

    StreamExecutionEnvironment getStreamExecutionEnvironment();

    void run(String tablePath) throws Exception;

    StreamExecutionEnvironment createPipeline(
            String tablePath,
            int sourceParallelism,
            int sinkParallelism
    );
}
