package org.examples.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;

public interface StreamJobProcessor <Key, Value, RddEntity> {

    JavaRDD<RddEntity> process(JavaRDD<ConsumerRecord<Key, Value>> rdd, long jobId, long jobStepID);

    String save(JavaRDD<RddEntity> rdd);

    default String path(SparkConf sparkConf){
        sparkConf.get("root.path");
        return "";
    }
}
