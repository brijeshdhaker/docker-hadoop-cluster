"""

$SPARK_HOME/bin/spark-submit \
--name "transaction-avro-stream" \
--master local[4] \
--packages org.apache.spark:spark-avro_2.12:3.4.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 \
bd-pyspark-module/src/main/py/com/example/streams/structured/structured-avro-stream.py

"""

import pyspark.sql.functions as fn
from confluent_kafka.schema_registry import SchemaRegistryClient
from pyspark.sql import SparkSession
from pyspark.sql.avro.functions import from_avro
from pyspark.sql.functions import *
from pyspark.sql.types import *


def process_avro_stream(args):

    #
    builder = SparkSession.builder
    builder.appName(args['app_name'])
    if args['run_mode'] == "local" :
        builder.master("local[*]")

    spark = builder.getOrCreate()

    #
    # spark.conf.set("spark.sql.streaming.checkpointLocation", "/apps/var/checkpoints/spark-structured-avro-stream")
    spark.conf.set("spark.sql.shuffle.partitions", "1")
    spark.conf.set("spark.sql.hive.convertMetastoreParquet", "false")
    spark.sparkContext.setLogLevel('ERROR')

    binary_to_string = fn.udf(lambda x: str(int.from_bytes(x, byteorder='big')), StringType())

    # Subscribe to 1 topic
    structureStreamDf = spark.readStream.format("kafka") \
        .option("kafka.bootstrap.servers", "kafka-broker.sandbox.net:9092") \
        .option("subscribe", args['topic']) \
        .option("startingOffsets", "earliest")\
        .option("failOnDataLoss", "false") \
        .load() \
        .withColumn('key', col("key").cast(StringType())) \
        .withColumn('fixedValue', expr("substring(value, 6, length(value)-5)")) \
        .withColumn('valueSchemaId', binary_to_string(expr("substring(value, 2, 4)"))) \
        .select('topic', 'partition', 'offset', 'timestamp', 'timestampType', 'key', 'valueSchemaId', 'fixedValue')

    # Returns True for DataFrames that have streaming sources
    print("structureStreamDf.isStreaming : " + str(structureStreamDf.isStreaming))
    print("Schema for structureStreamDf  : ")
    structureStreamDf.printSchema()

    #
    # Confluent Schema Registry
    #
    schema_registry_conf = {
        'url': 'http://schema-registry.sandbox.net:8081',
        'basic.auth.user.info': '{}:{}'.format('userid', 'password')
    }
    schema_registry_client = SchemaRegistryClient(schema_registry_conf)
    txn_schema_response = schema_registry_client.get_latest_version(args['topic']+"-value").schema
    txn_schema = txn_schema_response.schema_str


    fromAvroOptions = {"mode": "PERMISSIVE"}
    structuredGpsDf = (
        structureStreamDf.select(from_avro('fixedValue', txn_schema, fromAvroOptions).alias("transactions")).selectExpr("transactions.*")
    )


    # Writing to console sink (for debugging)
    structuredGpsDf.writeStream \
        .outputMode("update") \
        .format("console") \
        .option("maxRows", 50) \
        .option("truncate", False) \
        .trigger(processingTime="5 seconds")\
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
        'app_name': "transaction-avro-stream",
        'topic': "transaction-avro-topic",
        'auth_type': "PLAINTEXT",
        'run_mode': "local"
    }
    process_avro_stream(args)

#