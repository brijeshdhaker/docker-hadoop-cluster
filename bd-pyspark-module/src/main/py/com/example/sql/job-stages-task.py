import os
from pyspark import SparkConf
from pyspark.sql import SparkSession
from pyspark.sql.functions import *


sparkConf = SparkConf() \
    .set("spark.eventLog.enabled", "true") \
    .set("spark.eventLog.dir", "file:///apps/var/logs/spark-events")


spark = (
    SparkSession.builder.master("local[4]").
        appName('Sample Spark Application').
        config(conf=sparkConf).
        getOrCreate()
)

## Load data from csv
employee_df = spark.read.format("csv") \
    .option("header", "true") \
    .load("file:///apps/sandbox/defaultfs/employee.csv")

## Load data from csv
##

"""
employee_df = spark.read.csv("file:///apps/sandbox/defaultfs/employee.csv",
    header=True,
    nullValue="NA",
    inferSchema=True
)
"""
#employee_df.printSchema()

print(employee_df.rdd.getNumPartitions())

employee_df = employee_df.repartition(2)
print(employee_df.rdd.getNumPartitions())

employee_df = employee_df.filter(col("emp_salary") > 2000)\
    .select("emp_id", "emp_name", "emp_dept", "emp_salary")\
    .groupby("emp_dept")\
    .count()

employee_df.collect()

input("Press Enter to terminate the application...")
spark.stop()