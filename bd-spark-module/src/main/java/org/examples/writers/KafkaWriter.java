package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.concurrent.TimeoutException;

public class KafkaWriter extends DataWriter {

    public KafkaWriter(){

    }

    @Override
    public void write(Dataset<Row> records, String storeDir) {

        try {

            records.writeStream()
                        .format("kafka")
                        .option("","")
                        .start();

        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {

    }
}

