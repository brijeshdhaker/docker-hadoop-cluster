package flink.playgrounds.utils.jobs;

import io.delta.flink.sink.DeltaSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import flink.playgrounds.utils.Utils;

public abstract class DeltaSinkLocalJobExampleBase implements DeltaExampleLocalJobRunner {

    public void run(String tablePath) throws Exception {
        System.out.println("Will use table path: " + tablePath);

        Utils.prepareDirs(tablePath);
        StreamExecutionEnvironment env = createPipeline(tablePath, 2, 3);
        runFlinkJobInBackground(env);
        Utils.printDeltaTableRows(tablePath);
    }

    public abstract DeltaSink<RowData> getDeltaSink(String tablePath);

}
