package org.examples.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.examples.enums.MessageFormat;
import org.examples.enums.MessageSource;
import org.examples.enums.MessageType;
import org.examples.enums.Zone;
import org.examples.utils.HadoopPathBuilder;

public interface StreamJobProcessor <Key, Value, RddEntity> {

    JavaRDD<RddEntity> process(JavaRDD<ConsumerRecord<Key, Value>> rdd, long jobId, long jobStepID);

    String save(JavaRDD<RddEntity> rdd);

}
