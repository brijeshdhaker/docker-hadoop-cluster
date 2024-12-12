package com.org.example.flink.utils.jobs.bounded;

import com.org.example.flink.utils.Utils;
import com.org.example.flink.utils.jobs.ClusterFlinkJobRunner;
import io.delta.flink.source.DeltaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;


public abstract class DeltaBoundedSourceClusterJobExampleBase implements ClusterFlinkJobRunner {

    private final String workPath = Utils.resolveTableAbsolutePath("data/source_delta_table", getRunnerType());

    @Override
    public void run(String tablePath) throws Exception {
        System.out.println("Will use table path: " + workPath);
        Utils.prepareDirs(tablePath, workPath);
        StreamExecutionEnvironment env = createPipeline(workPath, 1, 1);
        env.execute("Bounded Example Job");
    }

    public abstract DeltaSource<RowData> getDeltaSource(String tablePath);
}
