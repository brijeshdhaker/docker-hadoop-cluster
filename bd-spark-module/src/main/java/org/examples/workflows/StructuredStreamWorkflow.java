package org.examples.workflows;

import static org.apache.spark.sql.avro.functions.from_avro;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.date_format;
import static org.apache.spark.sql.functions.expr;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
//import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
//import static org.apache.spark.sql.functions.col;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.DataTypes;
import org.examples.config.KafkaConfig;
import org.examples.config.WorkflowConfig;
import org.examples.processor.StreamJobProcessor;
import org.examples.service.OffsetService;
import org.examples.service.ServiceProvider;
import org.examples.service.TopicService;
import org.examples.utils.ListUtil;
import org.examples.utils.TimeUtil;
import org.examples.writers.ConsoleWriter;
import org.examples.writers.DataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructuredStreamWorkflow extends AbstractStreamWorkflow<String, byte[], Row> {


    private static final Logger logger = LoggerFactory.getLogger(StructuredStreamWorkflow.class);

    public StructuredStreamWorkflow(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }

    @Override
    public void startWorkflow() throws Exception {

        LocalDateTime startTime = LocalDateTime.now();
        if (this.workflowConfig != null) {

            SparkConf sparkConf = workflowConfig.sparkConf();
            String workflowAppId = sparkConf.get("workflow.app.id");
            List<String> topics = ListUtil.listFromStrings(sparkConf.get("spark.confluent.kafka.topics"));
            ServiceProvider serviceProvider = ServiceProvider.getInstance(sparkConf);
            OffsetService offsetService = serviceProvider.offsetService();
            TopicService topicService = serviceProvider.topicService(topics);

            try {

                long startBatchTime = System.currentTimeMillis();
                String avro_schema = new String(Files.readAllBytes(Paths.get("/apps/sandbox/schema-registry/avro/transaction-record.avsc")));
                String key_schema = "{\"type\": \"string\"}";

                //
                SparkSession spark = sparkSession();

                //spark.sparkContext().setLogLevel("ERROR"); checkpoints

                // Subscribe to 1 topic
                Dataset<Row> streamDF = spark
                        .readStream()
                        .format("kafka")
                        .option("kafka.bootstrap.servers", "kafka-broker.sandbox.net:9092")
                        .option("subscribe", "transaction-avro-topic")
                        .option("includeHeaders", "true")
                        .option("startingOffsets", "latest")
                        .option("failOnDataLoss", "false")
                        .load()
                        .withColumn("key", col("key").cast(DataTypes.StringType))
                        .withColumn("fixedValue", expr("substring(value, 6, length(value)-5)")) // Skip the first 5 bytes (reserved by schema registry encoding protocol)
                        .withColumn("valueSchemaId", expr("substring(value, 2, 4)")) //.withColumn("value", from_json(col("value").cast(StringType), schema))
                        .withColumn("txn_receive_date", date_format(functions.current_date(), "yyyy-MM-dd"));

                //streamDF.as()
                Dataset<Row> transactionDF = streamDF.withColumn("transaction", from_avro(col("fixedValue"), avro_schema))
                        .select("transaction.*",  "valueSchemaId", "txn_receive_date", "timestamp");

                //# Group the data by window and word and compute the count of each group

                // Returns True for DataFrames that have streaming sources
                logger.info("structureStreamDf.isStreaming : {}",  transactionDF.isStreaming());
                System.out.println("Schema for structureStreamDf  : ");
                transactionDF.printSchema();

                DataWriter writer = new ConsoleWriter();
                StreamingQuery query = writer.write(transactionDF);


            } catch (Exception e) {

                logger.error("DStream batch failed for jobStepId {}", workflowAppId, e);

            } finally {

                logger.info("Metrics >> Job started at {} and is running for {}", TimeUtil.toString(startTime), TimeUtil.diffWithNow(startTime));

            }
        }

    }

    @Override
    protected StreamJobProcessor<String, byte[], Row> streamProcessor() {
        return null;
    }


    @Override
    protected Map<String, Object> kafkaConfig() {
        Map<String, Object> kafkaConfig = KafkaConfig.sstreamConfig(
                workflowConfig.sparkConf()
        );
        return kafkaConfig;
    }
}
