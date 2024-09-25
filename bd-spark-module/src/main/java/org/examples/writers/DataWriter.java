package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

public abstract class DataWriter {


    protected final SaveMode saveMode = SaveMode.Append;

    public abstract void write(Dataset<Row> records, String storeDir);

    public abstract void write(Dataset<Row> records, String storeDir, String... partitionColumns);
}
