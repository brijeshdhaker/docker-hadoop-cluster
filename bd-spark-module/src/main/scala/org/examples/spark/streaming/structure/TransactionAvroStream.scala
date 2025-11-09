/*

mvn exec:exec@run-local -Drunclass=com.spark.streaming.structure.KafkaStructuredStream -Dparams="50"

$SPARK_HOME/bin/spark-submit \
--name "transaction-avro-stream" \
--master local[4] \
--packages org.apache.spark:spark-avro_2.12:3.4.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 \
--class org.examples.spark.streaming.structure.TransactionAvroStream \
bd-spark-module/dist/bd-spark-module-1.0-SNAPSHOT.jar -b kafka-broker.sandbox.net:9092 -t transaction-avro-topic -s http://schema-registry.sandbox.net:8081

*/


package org.examples.spark.streaming.structure

import io.confluent.kafka.schemaregistry.client.{CachedSchemaRegistryClient, SchemaRegistryClient}
import io.confluent.kafka.schemaregistry.client.rest.RestService
import org.apache.commons.cli.CommandLine
import org.apache.avro.Schema
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession, functions}

import java.nio.file.{Files, Paths}
import org.apache.spark.sql.avro._

import scala.concurrent.duration.DurationInt

object TransactionAvroStream {

  private var schemaRegistryClient: SchemaRegistryClient = _
  private var kafkaAvroDeserializer: AvroDeserializer = _

  val avro_schema = new String(Files.readAllBytes(Paths.get("/apps/sandbox/schema-registry/avro/transaction-record.avsc")))
  val SCHEMA_REGISTRY_URL = "http://schema-registry.sandbox.net:8081"
  val TOPIC = "transaction-avro-topic"


  def lookupTopicSchema(topic: String, isKey: Boolean = false) = {
    schemaRegistryClient.getLatestSchemaMetadata(topic + (if (isKey) "-key" else "-value")).getSchema
  }

  def avroSchemaToSparkSchema(avroSchema: String) = {
    SchemaConverters.toSqlType(new Schema.Parser().parse(avroSchema))
  }

  private def consumeUsingAvroDeserializer(spark: SparkSession, bootstrapServers: String, topic: String, schemaRegistryUrl: String): Unit = {
    import spark.implicits._

    // Setup the Avro deserialization UDF
    schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 128)
    kafkaAvroDeserializer = new AvroDeserializer(schemaRegistryClient)

    //
    spark.udf.register("deserialize", (bytes: Array[Byte]) =>
      kafkaAvroDeserializer.deserialize(bytes)
    )

    // Load the raw Kafka topic (byte stream)
    val rawDf = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("subscribe", topic)
      .option("startingOffsets", "earliest")
      .load()

    // Deserialize byte stream into strings (Avro fields become JSON)
    import org.apache.spark.sql.functions._
    val jsonDf = rawDf.select(
      // 'key.cast(DataTypes.StringType),  // string keys are simplest to use
      call_udf("deserialize", 'key).as("key"), // but sometimes they are avro
      call_udf("deserialize", 'value).as("value")
      // excluding topic, partition, offset, timestamp, etc
    )

    // Get the Avro schema for the topic from the Schema Registry and convert it into a Spark schema type
    val dfValueSchema = {
      val rawSchema = lookupTopicSchema(topic)
      avroSchemaToSparkSchema(rawSchema)
    }

    // Apply structured schema to JSON stream
    val parsedDf = jsonDf.select(
      'key, // keys are usually plain strings
      from_json('value, dfValueSchema.dataType).alias("value") // values are JSONified Avro records
    ).select('key, $"value.*") // flatten out the value

    // parsedDf.printSchema()

    //
    toConsole(parsedDf)

  }

  private def consumeAvro(spark: SparkSession, bootstrapServers: String, topic: String, schemaRegistryUrl: String): Unit = {

    // Create Schema for Dataframes
    val schema = new StructType()
      .add("id", IntegerType)
      .add("uuid", StringType)
      .add("cardtype", StringType)
      .add("website", StringType)
      .add("product", StringType)
      .add("amount", DoubleType)
      .add("city", StringType)
      .add("country", StringType)
      .add("addts", LongType)

    //    Create REST service to access schema registry and retrieve topic schema (latest)
    val restService = new RestService(SCHEMA_REGISTRY_URL)
    val valueRestResponseSchema = restService.getLatestVersion(TOPIC + "-value")
    val avroSchema = valueRestResponseSchema.getSchema

    val structureStreamDf = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("subscribe", topic)
      .option("includeHeaders", "true")
      .option("startingOffsets", "earliest") //earliest //latest
      .option("failOnDataLoss", "false")
      .load()
      .withColumn("key", col("key").cast(StringType))
      // Skip the first 5 bytes (reserved by schema registry encoding protocol)
      .withColumn("fixedValue", expr("substring(value, 6, length(value)-5)"))
      .withColumn("valueSchemaId", expr("substring(value, 2, 4)"))
      //.withColumn("value", from_json(col("value").cast(StringType), schema))
      .withColumn("txn_receive_date", date_format(functions.current_date(), "yyyy-MM-dd"))
      .select("topic", "partition", "offset", "timestamp", "timestampType", "key", "valueSchemaId", "txn_receive_date", "fixedValue")

    val transactionDF = structureStreamDf.withColumn("transaction", from_avro(col("fixedValue"), avroSchema))


    // Returns True for DataFrames that have streaming sources
    print("structureStreamDf.isStreaming : " + transactionDF.isStreaming)
    print("Schema for structureStreamDf  : ")
    transactionDF.printSchema()

    val recordsDF = transactionDF.select("transaction.*", "key", "valueSchemaId", "txn_receive_date", "timestamp")
    //# Group the data by window and word and compute the count of each group

    val windowAggregationDF = recordsDF.withWatermark("timestamp", "10 minutes")
      .groupBy(window(recordsDF("txn_receive_date"), "10 minutes", "5 minutes"), recordsDF("country"))
      .count()

    //
    toConsole(recordsDF)

  }

  private def toConsole(streamingDF: DataFrame): Unit = {

    // Writing to console sink (for debugging)
    streamingDF.writeStream
      .format("console")
      .outputMode(OutputMode.Append())
      .option("truncate", false)
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()
      .awaitTermination()

  }

  private def toObjectStore(streamingDF: DataFrame): Unit = {

  }

  private def toHive(streamingDF: DataFrame): Unit = {

    /*
      val hiveWarehouseDF = structureStreamDf.select("transaction.*", "txn_receive_date")
      print("Schema for hiveWarehouseDF   : ")
      hiveWarehouseDF.printSchema()
    */

    // Writing to File sink can be "parquet" "orc", "json", "csv", etc.
    streamingDF.writeStream
      .format("parquet")
      .option("path", "s3a://warehouse-raw/transactions/")
      .option("checkpointLocation", "s3a://warehouse-raw/checkpoints/transactions/")
      .partitionBy("txn_receive_date")
      .trigger(Trigger.ProcessingTime(30.seconds))
      .outputMode(OutputMode.Append)
      .start()
      .awaitTermination()

  }

  private def toDatabase(streamingDF: DataFrame): Unit = {

  }

  private def toKafka(streamingDF: DataFrame): Unit = {

    // Writing to Kafka
    streamingDF.selectExpr("CAST(id AS STRING) AS key", "to_json(struct(*)) AS value")
      .writeStream
      .format("kafka")
      .outputMode("append")
      .option("kafka.bootstrap.servers", "localhost:19092")
      .option("topic", "structured-stream-sink")
      .start().awaitTermination()

  }

  private def parseArg(args: Array[String]): CommandLine = {
    import org.apache.commons.cli._

    val options = new Options

    val masterOption = new Option("m", "master", true, "Spark master")
    masterOption.setRequired(false)
    options.addOption(masterOption)

    val bootstrapOption = new Option("b", "bootstrap-server", true, "Bootstrap servers")
    bootstrapOption.setRequired(true)
    options.addOption(bootstrapOption)

    val topicOption = new Option("t", "topic", true, "Kafka topic")
    topicOption.setRequired(true)
    options.addOption(topicOption)

    val schemaRegOption = new Option("s", "schema-registry", true, "Schema Registry URL")
    schemaRegOption.setRequired(true)
    options.addOption(schemaRegOption)

    val parser = new BasicParser
    parser.parse(options, args)
  }

  def main(args: Array[String]): Unit = {

    val cmd: CommandLine = parseArg(args)

    val master = cmd.getOptionValue("master", "local[4]")

    val spark: SparkSession = SparkSession.builder()
      .master(master)
      .appName(TransactionAvroStream.getClass.getName)
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")
    spark.conf.set("spark.sql.shuffle.partitions", "1")
    spark.conf.set("spark.sql.hive.convertMetastoreParquet", "false")
    spark.conf.set("spark.sql.parquet.writeLegacyFormat", "true")
    spark.conf.set("spark.sql.parquet.binaryAsString", "true")
    // spark.conf.set("spark.sql.streaming.checkpointLocation", "/structured-stream/checkpoints/")


    val bootstrapServers = cmd.getOptionValue("bootstrap-server")
    val topic = cmd.getOptionValue("topic")
    val schemaRegistryUrl = cmd.getOptionValue("schema-registry")

    //
    //consumeUsingAvroDeserializer(spark, bootstrapServers, topic, schemaRegistryUrl)

    //
    consumeAvro(spark, bootstrapServers, topic, schemaRegistryUrl)

    spark.stop()
  }

}