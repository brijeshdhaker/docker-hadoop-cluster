package org.examples.processor;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.examples.context.RawContext;
import org.examples.enums.MessageFormat;
import org.examples.enums.MessageSource;
import org.examples.enums.MessageType;
import org.examples.enums.Zone;
import org.examples.utils.HadoopPathBuilder;
import org.examples.writers.DataWriter;

import java.util.Iterator;

public class JsonJobProcessor extends KafkaJobProcessor <String, GenericRecord> {

    public JsonJobProcessor(SparkSession spark, DataWriter dataWriter) {
        super(spark, dataWriter);
    }

    @Override
    protected FlatMapFunction<Iterator<ConsumerRecord<String, GenericRecord>>, Row> partitionProcessor(RawContext rawContext) {
        return null;
    }

    @Override
    public JavaRDD<Row> process(JavaRDD<ConsumerRecord<String, GenericRecord>> rdd, long jobId, long jobStepID) {
        return null;
    }

    @Override
    public String save(JavaRDD<Row> rdd) {
        return "";
    }

    public String path() {

        SparkConf sparkConf = spark.sparkContext().conf();

        return HadoopPathBuilder.rootPath(sparkConf.get("workflow.root.path"))
                .zone(Zone.RAW)
                .messageType(MessageType.TRANSACTIONS)
                .messageSource(MessageSource.KAFKA_JSON)
                .messageFormat(MessageFormat.JSON)
                .instance(sparkConf.get("spark.confluent.kafka.group"))
                .build();

    }
}
