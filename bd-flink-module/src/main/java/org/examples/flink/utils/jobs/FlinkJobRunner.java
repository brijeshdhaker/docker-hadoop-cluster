package org.examples.flink.utils.jobs;

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

    MiniCluster getMiniCluster(Map<String, String> params);

    StreamExecutionEnvironment getStreamExecutionEnvironment(Map<String, String> params);

    StreamExecutionEnvironment createPipeline(Map<String, String> params);

}
