"""

$SPARK_HOME/bin/spark-submit \
--name "spark-structured-avro-stream" \
--master local[4] \
--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 \
/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-python-module/src/main/com/example/streams/structured/structured-kafka-stream.py

"""


#
from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.types import *

"""
spark-text-txn-topic
spark-json-txn-topic
spark-avro-txn-topic
spark-xml-txn-topic
"""



# Convenience function for turning JSON strings into DataFrames.
def jsonToDataFrame(json, schema=None):
    # SparkSessions are available with Spark 2.0+
    reader = spark.read
    if schema:
        reader.schema(schema)
    return reader.json(spark.parallelize([json]))


#
#
#
def writeToSQLWarehouse(df, epochId):
    df.write.format("com.databricks.spark.sqldw") \
        .mode('overwrite') \
        .option("url", "jdbc:sqlserver://<the-rest-of-the-connection-string>") \
        .option("forward_spark_azure_storage_credentials", "true") \
        .option("dbtable", "my_table_in_dw_copy") \
        .option("tempdir", "wasbs://<your-container-name>@<your-storage-account-name>.blob.core.windows.net/<your-directory-name>") \
        .save()

#
#
#
def writeToHiveWarehouse(df, epochId):

    print("Hive HiveWarehouse processing started for Micro Batch {} ".format(epochId))
    hiveRDD = df.rdd
    hivedf = spark.createDataFrame(hiveRDD)
    hivedf.show()
    hivedf.write.mode('ignore').insertInto("default.transaction_details")
    #df.printSchema()
    #df.createOrReplaceTempView("batch_records")
    #df.drop("tansaction_uuid").write.insertInto('default.transaction_detail_hive_tbl', 'append')
    query = """
        INSERT INTO TABLE transaction_detail_hive_tbl SELECT 'transaction_id', 'transaction_card_type', 'transaction_ecommerce_website_name',
        'transaction_product_name', 'transaction_datetime', 'transaction_amount', 'transaction_city_name', 'transaction_country_name' 
        from  batch_records
    """
    #spark.sql("SELECT transaction_id, transaction_card_type, transaction_ecommerce_website_name, transaction_product_name, transaction_datetime, transaction_amount, transaction_city_name, transaction_country_name from  batch_records").show()
    #df.drop("tansaction_uuid").show()
    print("Micro Batch {} successfully written into HiveWarehouse".format(epochId))


def process_txn_stream(args):
    #
    builder = SparkSession.builder
    builder.appName(args['app_name'])
    if args['run_mode'] == "local" :
        builder.master("local[*]")

    spark = builder.getOrCreate()


    #
    # spark.conf.set("spark.sql.streaming.checkpointLocation", "/apps/var/checkpoints/spark-structured-txn-stream")
    spark.conf.set("spark.sql.shuffle.partitions", "1")
    spark.conf.set("spark.sql.hive.convertMetastoreParquet", "false")
    spark.sparkContext.setLogLevel('ERROR')

    #
    binary_to_string = udf(lambda x: str(int.from_bytes(x, byteorder='big')), StringType())

    # Subscribe to 1 topic
    #.option("endingOffsets", "latest")
    # Using a struct
    schema = StructType() \
        .add("id", IntegerType()) \
        .add("uuid", StringType()) \
        .add("cardtype", StringType()) \
        .add("website", StringType()) \
        .add("product", StringType()) \
        .add("amount", DoubleType()) \
        .add("city", StringType()) \
        .add("country", StringType()) \
        .add("addts", LongType())

    # Subscribe to 1 topic
    structureStreamDf = spark.readStream.format("kafka") \
        .option("kafka.bootstrap.servers", "kafkabroker.sandbox.net:9092") \
        .option("subscribe", "txn-text-stream-topic") \
        .option("startingOffsets", "earliest") \
        .option("failOnDataLoss", "false") \
        .load() \
        .withColumn('key', col("key").cast(StringType())) \
        .withColumn('value', from_json(col("value").cast(StringType()), schema)) \
        .withColumn("txn_receive_date", date_format(current_date(), "yyyy-MM-dd"))


    # Returns True for DataFrames that have streaming sources
    print("structureStreamDf.isStreaming : " + str(structureStreamDf.isStreaming))
    print("Schema for structureStreamDf  : ")
    structureStreamDf.printSchema()

    recordsDF = structureStreamDf.select("value.*", "txn_receive_date", "timestamp")

    # Group the data by window and word and compute the count of each group
    windowAggregationDF = recordsDF.withWatermark("timestamp", "10 seconds") \
        .groupBy(window(recordsDF.timestamp, "10 seconds", "5 seconds"), recordsDF.country) \
        .count()

    #
    hiveWarehouseDF = structureStreamDf.select("value.*", "txn_receive_date")
    print("Schema for hiveWarehouseDF   : ")
    hiveWarehouseDF.printSchema()

    """
    result_df = data_df.withColumn("txn_receive_date", date_format(current_date(), "yyyy-MM-dd"))\
                .withColumn("txn_date", to_utc_timestamp(from_unixtime(col("transaction_datetime").cast("Long")/1000,'yyyy-MM-dd HH:mm:ss'),'IST'))\
    """

    """


    
    # Foreach sink - Runs arbitrary computation on the records in the output.
    structureStreamDf.writeStream \
        .foreach(lambda x : x) \
        .start()
    """

    if args['sink_format'] == "kafka" :
        # Writing to Kafka
        structureStreamDf.writeStream \
            .format("kafka") \
            .option("kafka.bootstrap.servers", "kafkabroker.sandbox.net:9092") \
            .option("topic", "structured-stream-sink") \
            .trigger(processingTime="15 seconds") \
            .start()

    if args['sink_format'] == "jdbc" :
        structureStreamDf.write.format('jdbc') \
            .option("url", "jdbc:mysql://mysqlserver:3306/USER") \
            .option("driver", "com.mysql.jdbc.Driver") \
            .option("dbtable", "USER") \
            .option("user", "root") \
            .option("password", "p@SSW0rd") \
            .mode('append') \
            .trigger(processingTime="15 seconds") \
            .save()

    if args['sink_format'] == "batch" :
        # ForeachBatch Sink
        hiveWarehouseDF.writeStream \
            .foreachBatch(writeToHiveWarehouse) \
            .start() \
            .trigger(processingTime="15 seconds") \
            .awaitTermination()


    if args['sink_format'] == "memory" :
        # Writing to Memory sink (for debugging)
        structureStreamDf.writeStream\
            .format("memory")\
            .queryName("tableName") \
            .outputMode("complete") \
            .trigger(processingTime="15 seconds") \
            .start()
        spark.sql("select * from tableName").show()   # interactively query in-memory table

    #
    if args['sink_format'] == "file" :
        # Writing to File sink can be "parquet" "orc", "json", "csv", etc.
        hiveWarehouseDF.writeStream \
            .format("parquet") \
            .option("path", "hdfs://namenode.sandbox.net:9000/warehouse/tablespace/external/hive/transaction_details/") \
            .option("checkpointLocation", "hdfs://namenode.sandbox.net:9000/apps/var/checkpoints/transaction_details/") \
            .partitionBy("txn_receive_date") \
            .trigger(processingTime="15 seconds") \
            .start()

    if args['sink_format'] == "console" :
        # Writing to console sink (for debugging)
        windowAggregationDF.writeStream \
            .outputMode("update") \
            .format("console") \
            .trigger(processingTime="15 seconds") \
            .start() \
            .awaitTermination()

    spark.stop()


#
#
#
if __name__ != '__main__':
    pass
else:
    args = {
        'app_name': "spark-structured-txn-stream",
        'topic': "spark-text-txn-topic",
        'auth_type': "PLAINTEXT",
        'run_mode': "local",
        'sink_format': 'console'
    }
    process_txn_stream(args)

#