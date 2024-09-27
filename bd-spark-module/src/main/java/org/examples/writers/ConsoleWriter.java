package org.examples.writers;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleWriter extends DataWriter {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleWriter.class);

    @Override
    public void write(Dataset<Row> records, String storeDir) {
        logger.info("Showing records on console target for {} dir", storeDir);
        if(records!= null && !records.isEmpty()){
            records.show();
        }
    }

    @Override
    public void write(Dataset<Row> records, String storeDir, String... partitionColumns) {
        if(records!= null && !records.isEmpty()){
            records.show();
        }
    }
}
