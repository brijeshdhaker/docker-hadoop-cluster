package org.examples.writers;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.examples.processor.StreamJobProcessor;

public class HiveWriter extends DataWriter {

    public HiveWriter(){

    }

    @Override
    public void write(Dataset<Row> records, String storeDir) {
        if(!records.isStreaming()) {

            //name='default.tweeter_tweets', format='hive', mode='append'
            records.write()
                    .option("fileFormat", "parquet")
                    .option("path", "s3a://defaultfs/warehouse/tablespace/managed/hive/transactions")
                    .mode(SaveMode.Append)
                    //.format("hive")
                    .saveAsTable("TRANSACTIONS");

        }else{

        }
    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {

    }
}
