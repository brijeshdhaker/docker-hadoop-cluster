package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.streaming.StreamingQuery;

import java.util.Collection;

public abstract class DataWriter {

    protected final SaveMode saveMode = SaveMode.Append;

    public abstract void write(Dataset<Row> records, String storeDir);

    public abstract void write(Dataset<Row> records, String storeDir, String... partitionColumns);

    public abstract StreamingQuery write(Dataset<Row> records);
}
