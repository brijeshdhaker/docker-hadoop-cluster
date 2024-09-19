package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class ParquitWriter extends DataWriter {

    @Override
    public void write(Dataset<Row> records, String storeDir) {
        records.write().mode(saveMode).parquet(storeDir);
    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {
        records.write().mode(saveMode)
                .partitionBy(partitionColumns)
                .parquet(storeDir);
    }
}
