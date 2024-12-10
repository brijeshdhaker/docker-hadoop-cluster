package com.org.example.flink.utils.jobs;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public interface DeltaExampleJobRunner {

    StreamExecutionEnvironment getStreamExecutionEnvironment();

    void run(String tablePath) throws Exception;

    StreamExecutionEnvironment createPipeline(
            String tablePath,
            int sourceParallelism,
            int sinkParallelism
    );
}
