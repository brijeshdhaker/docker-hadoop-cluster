package org.apache.flink.playgrounds.ops.clickcount;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

public class HiveReader {

    /*
        CREATE CATALOG myhive WITH (
            'type' = 'hive',
            'default-database' = 'mydatabase',
            'hive-conf-dir' = '/opt/hive-conf'
        );
        -- set the HiveCatalog as the current catalog of the session
        USE CATALOG myhive;
     */

    public static void main(String[] args) {

        EnvironmentSettings settings = EnvironmentSettings.inStreamingMode();
        TableEnvironment tableEnv = TableEnvironment.create(settings);

        String name            = "myhive";
        String defaultDatabase = "mydatabase";
        String hiveConfDir     = "/opt/hive-conf";

        HiveCatalog hive = new HiveCatalog(name, defaultDatabase, hiveConfDir);
        tableEnv.registerCatalog("myhive", hive);

        // set the HiveCatalog as the current catalog of the session
        tableEnv.useCatalog("myhive");

    }
}
