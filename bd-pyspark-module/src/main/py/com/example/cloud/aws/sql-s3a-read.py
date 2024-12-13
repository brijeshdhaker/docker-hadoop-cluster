from pyspark import SparkConf
from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, StringType, IntegerType

conf = (
    SparkConf()
        .setAppName("Spark minIO Test")
        .setMaster("local[*]")
        .set("spark.hadoop.fs.s3a.endpoint", "http://minio.sandbox.net:9010")
        .set("spark.hadoop.fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO")
        .set("spark.hadoop.fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG")
        .set("spark.hadoop.fs.s3a.path.style.access", "true")
        .set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
        .set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
)

spark = SparkSession.builder.appName("Spark minIO Test").config(conf=conf).getOrCreate()
spark.sparkContext.setLogLevel("ERROR")

airlineSchema = StructType([
    StructField("id", IntegerType(), True),
    StructField("airlineName", StringType(),True),
    StructField("alias", StringType(),True),
    StructField("iataCode", StringType(), True),
    StructField("icaoCode", StringType(), True),
    StructField("callsign", StringType(), True),
    StructField("country", StringType(), True),
    StructField("active", StringType(), True)
])


airlinesWithSchema = spark.read.format("csv") \
    .option("header", False) \
    .option("delimiter", ',') \
    .schema(airlineSchema)\
    .load("s3a://warehouse/airlines.csv")

airlinesWithSchema.printSchema()
airlinesWithSchema.show(truncate=False)
airlinesWithSchema.filter("country='India'").show(truncate=False)

#print(sc.wholeTextFiles('s3a://warehouse/airlines.csv').collect())