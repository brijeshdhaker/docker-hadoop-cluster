package org.examples.flink.utils.jobs.sql;

import org.examples.flink.utils.Utils;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


public abstract class SqlSinkExampleBase extends SqlExampleBase {

    public void run(String tablePath) throws Exception {
        System.out.println("Will use table path: " + tablePath);

        Utils.prepareDirs(tablePath);
        StreamTableEnvironment tableEnv = createTableStreamingEnv(false); // streamingMode = false
        runSqlJob(tablePath, tableEnv);
        Utils.printDeltaTableRows(tablePath,"Local");
    }
}
