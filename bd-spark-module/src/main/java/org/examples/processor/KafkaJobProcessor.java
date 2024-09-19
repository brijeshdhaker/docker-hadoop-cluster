package org.examples.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.examples.writers.DataWriter;

import java.util.Iterator;

public abstract class KafkaJobProcessor<Key, Value> implements StreamJobProcessor <Key, Value, Row> {

    protected SparkConf sparkConf;
    protected StructType schema;

    private DataWriter dataWriter;

    public KafkaJobProcessor(SparkConf sparkConf){
        this.sparkConf = sparkConf;

    }


    @Override
    public String save(JavaRDD<Row> rdd) {
        try{

            SparkSession spark = SparkSession
                    .builder()
                    .master("local[*]")  // // spark://spark-iceberg.sandbox.net:7077")
                    .appName("Java Spark SQL basic example")
                    .config(this.sparkConf)
                    .getOrCreate();

            String path = path(sparkConf);

            spark.sparkContext().setLogLevel("ERROR");

            dataWriter.write(spark.createDataFrame(rdd,schema),path);

        }catch (Exception e){

        }
        return "";
    }

    @Override
    public JavaRDD<Row> process(JavaRDD<ConsumerRecord<Key, Value>> rdd, long jobId, long jobStepID) {
        return rdd.mapPartitions(partitionProcessor());
    }


    protected abstract FlatMapFunction<Iterator<ConsumerRecord<Key,Value>>, Row> partitionProcessor();
}
