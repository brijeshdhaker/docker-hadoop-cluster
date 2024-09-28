package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

public class ConsoleWriter extends DataWriter {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleWriter.class);

    @Override
    public void write(Dataset<Row> records, String storeDir) {
        logger.info("Showing records on console target for {} dir", storeDir);
        if(records!= null && !records.isEmpty()){
            if(!records.isStreaming()) {

                records.show();

            }else {

                // Writing to console sink (for debugging)
                try {

                    records.writeStream()
                            .format("console")
                            .outputMode(OutputMode.Append())
                            .option("truncate", false)
                            .trigger(Trigger.ProcessingTime("10 seconds"))
                            .start()
                            .awaitTermination();

                } catch (StreamingQueryException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {
        if(records!= null && !records.isEmpty()){
            records.show();
        }
    }
}
