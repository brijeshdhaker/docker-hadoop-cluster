package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.concurrent.TimeoutException;

public class KafkaWriter extends DataWriter {

    public KafkaWriter(){

    }

    @Override
    public void write(Dataset<Row> records, String storeDir) {

        if(!records.isStreaming()) {

            records.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
                    .write()
                    .format("kafka")
                    .option("kafka.bootstrap.servers", "kafkabroker.sandbox.net:9092")
                    .option("topic", "stream-batch-sink")
                    .save();

        } else {
            try {

                // Writing to Kafka
                StreamingQuery query = records.selectExpr("CAST(id AS STRING) AS key", "to_json(struct(*)) AS value")
                        .writeStream()
                        .format("kafka")
                        .outputMode("append")
                        .option("kafka.bootstrap.servers", "kafkabroker.sandbox.net:9092")
                        .option("topic", "stream-batch-sink")
                        .start();

                query.awaitTermination();

            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            } catch (StreamingQueryException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {

    }


    public StreamingQuery write(Dataset<Row> records){
        StreamingQuery query = null;
        try {

            // Writing to Kafka
            query = records.selectExpr("CAST(id AS STRING) AS key", "to_json(struct(*)) AS value")
                    .writeStream()
                    .format("kafka")
                    .outputMode("append")
                    .option("kafka.bootstrap.servers", "kafkabroker.sandbox.net:9092")
                    .option("topic", "stream-batch-sink")
                    .start();

            query.awaitTermination();

        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (StreamingQueryException e) {
            throw new RuntimeException(e);
        }

        return query;
    }
}

