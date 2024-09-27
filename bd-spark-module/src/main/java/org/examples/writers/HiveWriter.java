package org.examples.writers;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.examples.processor.StreamJobProcessor;

public class HiveWriter extends DataWriter {

    public HiveWriter(){

    }

    @Override
    public void write(Dataset<Row> records, String storeDir) {
        //name='default.tweeter_tweets', format='hive', mode='append'
        records.write().option("fileFormat", "parquet").saveAsTable("SANDBOXDB.transactions");
    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {

    }
}
