package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParquetWriter extends DataWriter {


    private static final Logger logger = LoggerFactory.getLogger(ParquetWriter.class);

    @Override
    public void write(Dataset<Row> records, String storeDir) {

        logger.info("Saving records to {} dir", storeDir);

        if(logger.isDebugEnabled()) {
            logger.debug("Head example : {}", records.head());
        }

        records.write()
                .mode(saveMode)
                .parquet(storeDir);
    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {

        logger.info("Saving records to [{}] with partition column(s): {}", storeDir, partitionColumns);

        if(logger.isDebugEnabled()) {
            logger.debug("Head example : {}", records.head());
        }

        records.write().mode(saveMode)
                .partitionBy(partitionColumns)
                .parquet(storeDir);
    }
}
