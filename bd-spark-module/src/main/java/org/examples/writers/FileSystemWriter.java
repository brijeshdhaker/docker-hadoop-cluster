package org.examples.writers;

import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

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

        records.write().mode(saveMode)
                .partitionBy(partitionColumns).
                format("parquet")
                .save(storeDir);
    }
}
