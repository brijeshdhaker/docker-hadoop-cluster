package org.examples.spark.streaming.structure

import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{SparkSession, functions}

import scala.concurrent.duration.DurationInt

/*

mvn exec:exec@run-local -Drunclass=com.spark.streaming.structure.KafkaStructuredStream -Dparams="50"

$SPARK_HOME/bin/spark-submit \
--class org.examples.spark.SparkPi \
--name "transaction-avro-stream" \
--master local[4] \
--packages org.apache.spark:spark-avro_2.12:3.4.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 \
bd-spark-module/dist/bd-spark-module-1.0-SNAPSHOT.jar

*/
object KafkaStructuredStream extends App {

  val spark: SparkSession = SparkSession.builder()
    .master("local[4]")
    .appName("KafkaStructuredStream")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  spark.conf.set("spark.sql.streaming.checkpointLocation", "/structured-stream/checkpoints/")
  spark.conf.set("spark.sql.shuffle.partitions", "1")
  spark.conf.set("spark.sql.hive.convertMetastoreParquet", "false")
  spark.conf.set("spark.sql.parquet.writeLegacyFormat", "true")
  spark.conf.set("spark.sql.parquet.binaryAsString", "true")

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


  val structureStreamDf = spark
    .readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "kafka-broker:9092")
    .option("subscribe", "structured-stream-source")
    .option("includeHeaders", "true")
    .option("startingOffsets", "latest")
    .option("failOnDataLoss", "false")
    .load()
    .withColumn("key", col("key").cast(StringType))
    .withColumn("value", from_json(col("value").cast(StringType), schema))
    .withColumn("txn_receive_date", date_format(functions.current_date(), "yyyy-MM-dd"))

  // Returns True for DataFrames that have streaming sources
  print("structureStreamDf.isStreaming : " + structureStreamDf.isStreaming)
  print("Schema for structureStreamDf  : ")
  structureStreamDf.printSchema()

  val recordsDF = structureStreamDf.select("value.*", "txn_receive_date", "timestamp")
  //# Group the data by window and word and compute the count of each group

  val windowAggregationDF = recordsDF.withWatermark("timestamp", "10 minutes")
    .groupBy(window(recordsDF("timestamp"), "10 minutes", "5 minutes"), recordsDF("country"))
    .count()

  val hiveWarehouseDF = structureStreamDf.select("value.*", "txn_receive_date")
  print("Schema for hiveWarehouseDF   : ")
  hiveWarehouseDF.printSchema()


  // Writing to Kafka
  /*
  structureStreamDf.selectExpr("CAST(id AS STRING) AS key", "to_json(struct(*)) AS value")
    .writeStream
    .format("kafka")
    .outputMode("append")
    .option("kafka.bootstrap.servers", "localhost:19092")
    .option("topic", "structured-stream-sink")
    .start().awaitTermination()
  */

  // Writing to File sink can be "parquet" "orc", "json", "csv", etc.
  hiveWarehouseDF.writeStream
    .format("parquet")
    .option("path", "hdfs://namenode:9000/transaction_details/")
    .option("checkpointLocation", "hdfs://namenode:9000/checkpoints/transaction_details/")
    .partitionBy("txn_receive_date")
    .trigger(Trigger.ProcessingTime(30.seconds))
    .outputMode(OutputMode.Append)
    .start()
    .awaitTermination()

  // Writing to console sink (for debugging)
  windowAggregationDF.writeStream
    .outputMode("update")
    .format("console")
    .trigger(Trigger.ProcessingTime("10 seconds"))
    .start()
    .awaitTermination()

}