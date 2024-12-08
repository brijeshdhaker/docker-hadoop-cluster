package flink.playgrounds.utils.jobs.continuous;

import flink.playgrounds.utils.Utils;
import flink.playgrounds.utils.jobs.DeltaExampleJobRunner;
import io.delta.flink.source.DeltaSource;
import org.apache.flink.table.data.RowData;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public abstract class DeltaContinuousSourceClusterJobExampleBase implements DeltaExampleJobRunner {

    private static final String workPath = "/tmp/delta-flink-example/source_table_work";

    @Override
    public void run(String tablePath) throws Exception {
        System.out.println("Will use table path: " + workPath);
        Utils.prepareDirs(tablePath, workPath);
        StreamExecutionEnvironment env = createPipeline(workPath, 1, 1);

        // Just to have better visual representation of Job on FLink's UI
        env.disableOperatorChaining();

        env.executeAsync("Continuous Example Job");
        Utils.runSourceTableUpdater(workPath).get();
    }

    public abstract DeltaSource<RowData> getDeltaSource(String tablePath);
}
