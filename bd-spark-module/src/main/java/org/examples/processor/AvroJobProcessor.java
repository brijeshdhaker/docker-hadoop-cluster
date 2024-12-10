package org.examples.processor;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.examples.context.RawContext;
import org.examples.enums.MessageFormat;
import org.examples.enums.MessageSource;
import org.examples.enums.MessageType;
import org.examples.enums.Zone;
import org.examples.models.Transaction;
import org.examples.utils.HadoopPathBuilder;
import org.examples.utils.JsonUtil;
import org.examples.writers.DataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

public class AvroJobProcessor extends KafkaJobProcessor <String, GenericRecord>{

    private static final Logger logger = LoggerFactory.getLogger(AvroJobProcessor.class);

    public AvroJobProcessor(SparkSession spark, DataWriter dataWriter){
        super(spark, dataWriter);
    }

    @Override
    public String save(JavaRDD<Row> rdd) {

        String path = path();
        /*
        rdd.foreach(row -> {
            String key   = (String)row.get(0);
            byte[] value = (byte[])row.get(1);
            System.out.println("Key : " + key + " *******  Value : []" + value.length);
        });
        */
        JavaRDD<Transaction> transactionRDD = rdd.map(row -> {
            String jsonValue = row.get(1).toString();
            Transaction transaction = JsonUtil.fromJson(jsonValue, Transaction.class);
            return transaction;
        });

        Dataset<Row> txnDF = this.spark.createDataFrame(transactionRDD, Transaction.class);
        this.dataWriter.write(txnDF,path);

        return path;

    }

    @Override
    public JavaRDD<Row> process(JavaRDD<ConsumerRecord<String, GenericRecord>> rdd, long jobId, long jobStepID) {
        try {

            RawContext rawContext = RawContext.as()
                    .jobId(jobId)
                    .jobStepId(jobStepID)
                    .transactionTime(LocalDateTime.now())
                    .messageSource(MessageSource.KAFKA_AVRO)
                    .messageType(MessageType.TRANSACTIONS)
                    .schema(schema);

            return rdd.mapPartitions(partitionProcessor(rawContext));

        } catch (Exception e) {
            logger.error("Unable to map partition");
            throw new RuntimeException("Unable to map partitions", e);

        }

    }

    @Override
    protected FlatMapFunction<Iterator<ConsumerRecord<String, GenericRecord>>, Row> partitionProcessor(RawContext rawContext) {
        Map<String, String> errorTopics = topicService.error_topics();
        return AvroPartitionProcessor.as(rawContext, errorTopics);
    }

    public String path() {

        SparkConf sparkConf = spark.sparkContext().conf();

        return HadoopPathBuilder.rootPath(sparkConf.get("workflow.root.path"))
                .zone(Zone.RAW)
                .messageType(MessageType.TRANSACTIONS)
                .messageSource(MessageSource.KAFKA_AVRO)
                .messageFormat(MessageFormat.AVRO)
                .instance(sparkConf.get("spark.confluent.kafka.group"))
                .build();

    }
}
