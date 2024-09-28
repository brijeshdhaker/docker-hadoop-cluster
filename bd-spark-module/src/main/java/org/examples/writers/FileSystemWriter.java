package org.examples.writers;

import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

public class FileSystemWriter extends DataWriter {


    private static final Logger logger = LoggerFactory.getLogger(FileSystemWriter.class);

    public FileSystemWriter(){

    }

    @Override
    public void write(Dataset<Row> records, String storeDir) {

        logger.info("Saving records to {} dir", storeDir);

        if(logger.isDebugEnabled()) {
            logger.debug("Head example : {}", records.head());
        }

        records.write()
                .format("parquet")
                .save(storeDir);

    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {

        logger.info("Saving records to [{}] with partition column(s): {}", storeDir, partitionColumns);

        if(logger.isDebugEnabled()) {
            logger.debug("Head example : {}", records.head());
        }
        if(!records.isStreaming()) {

            records.write().mode(saveMode)
                    .partitionBy(partitionColumns).
                    format("parquet")
                    .save(storeDir);

        }else{

            // Writing to File sink can be "parquet" "orc", "json", "csv", etc.
            try {

                records.writeStream()
                        .format("parquet")
                        .option("path", storeDir) //"s3a://warehouse-raw/transactions/"
                        .option("checkpointLocation", "s3a://warehouse-raw/checkpoints/transactions/")
                        .partitionBy(partitionColumns)
                        .trigger(Trigger.ProcessingTime("10 seconds"))
                        .outputMode(OutputMode.Append())
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
