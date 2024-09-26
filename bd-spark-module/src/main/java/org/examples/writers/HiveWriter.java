package org.examples.writers;

import org.apache.spark.api.java.JavaRDD;
import org.examples.processor.StreamJobProcessor;

public class HiveWriter implements StreamJobProcessor {

    @Override
    public JavaRDD process(JavaRDD rdd, long jobId, long jobStepID) {
        return null;
    }

    @Override
    public String save(JavaRDD rdd) {
        return "";
    }
}
