package org.examples.processor;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;

public interface StreamJobProcessor <Key, Value, RddEntity> {

    JavaRDD<RddEntity> process();

    default String path(SparkConf sparkConf){
        return "";
    }
}
