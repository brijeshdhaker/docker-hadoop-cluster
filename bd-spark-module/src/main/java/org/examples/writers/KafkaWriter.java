package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.concurrent.TimeoutException;

public class KafkaWriter extends DataWriter {

    public KafkaWriter(){

    }

    @Override
    public void write(Dataset<Row> records, String storeDir) {

        try {

            // Writing to Kafka
            records.selectExpr("CAST(id AS STRING) AS key", "to_json(struct(*)) AS value")
                    .writeStream()
                    .format("kafka")
                    .outputMode("append")
                    .option("kafka.bootstrap.servers", "localhost:19092")
                    .option("topic", "structured-stream-sink")
                    .start().awaitTermination();

        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (StreamingQueryException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {

    }
}

