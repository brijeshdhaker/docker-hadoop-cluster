from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.window import Window

# .enableHiveSupport() \
# create Spark context with Spark configuration
spark = SparkSession \
    .builder \
    .appName("PySpark_Aggregate_Functions") \
    .getOrCreate()
spark.sparkContext.setLogLevel("WARN")

# sample data for dataframe
sampleData = (("Ram", "Sales", 3000),
              ("Meena", "Sales", 4600),
              ("Robin", "Sales", 4100),
              ("Kunal", "Finance", 3000),
              ("Ram", "Sales", 3000),
              ("Srishti", "Management", 3300),
              ("Jeny", "Finance", 3900),
              ("Hitesh", "Marketing", 3000),
              ("Kailash", "Marketing", 2000),
              ("Sharad", "Sales", 4100)
              )

# column names for dataframe
columns = ["Employee_Name", "Department", "Salary"]

dataFrame = spark.createDataFrame(data=sampleData, schema=columns)
dataFrame.show()

# creating a window partition of dataframe
windowPartitionAgg  = Window.partitionBy("Department")

""" Aggregate Functions """
dataFrame.withColumn("Avg", avg(col("salary")).over(windowPartitionAgg)) \
    .withColumn("Sum", sum(col("salary")).over(windowPartitionAgg)) \
    .withColumn("Min", min(col("salary")).over(windowPartitionAgg)) \
    .withColumn("Max", max(col("salary")).over(windowPartitionAgg)) \
    .show()

#
spark.stop()