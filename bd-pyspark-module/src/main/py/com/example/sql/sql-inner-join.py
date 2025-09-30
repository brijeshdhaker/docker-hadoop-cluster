from pyspark import SparkConf
from pyspark.sql import SparkSession


conf = (SparkConf.setAppName("sql-inner-join")
        .set("",""))

spark = SparkSession.builder.appName().config(conf=conf)