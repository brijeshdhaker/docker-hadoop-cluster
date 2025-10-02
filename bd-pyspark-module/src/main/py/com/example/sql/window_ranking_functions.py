from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.window import Window

# create Spark context with Spark configuration
spark = SparkSession \
    .builder \
    .appName("PySpark_Ranking_Functions") \
    .enableHiveSupport() \
    .getOrCreate()
spark.sparkContext.setLogLevel("WARN")

# sample data for dataframe
sampleData = ((101, "Ram", "Biology", 80),
              (103, "Meena", "Social Science", 78),
              (104, "Robin", "Sanskrit", 58),
              (102, "Kunal", "Physics", 89),
              (101, "Ram", "Biology", 80),
              (106, "Srishti", "Maths", 70),
              (108, "Jeny", "Physics", 75),
              (107, "Hitesh", "Maths", 88),
              (109, "Kailash", "Maths", 90),
              (105, "Sharad", "Social Science", 84)
              )

# column names for dataframe
columns = ["Roll_No", "Student_Name", "Subject", "Marks"]

df = spark.createDataFrame(data=sampleData, schema=columns)
df.show()

windowPartition = Window.partitionBy("Subject").orderBy("Marks")

""" row_number """
# importing row_number() from pyspark.sql.functions
from pyspark.sql.functions import row_number

# applying the row_number() function
df.withColumn("row_number",
               row_number().over(windowPartition)).show()

""" rank """
# importing rank() from pyspark.sql.functions
from pyspark.sql.functions import rank

# applying the rank() function
df.withColumn("rank", rank().over(windowPartition)) \
    .show()

""" percent_rank """
# importing percent_rank() from pyspark.sql.functions
from pyspark.sql.functions import percent_rank

# applying the percent_rank() function
df.withColumn("percent_rank", percent_rank().over(windowPartition)) \
    .show()

""" dense_rank """
# importing dense_rank() from pyspark.sql.functions
from pyspark.sql.functions import dense_rank

# applying the dense_rank() function
df.withColumn("dense_rank", dense_rank().over(windowPartition)) \
    .show()

#
spark.stop()