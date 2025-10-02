from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.window import Window

# create Spark context with Spark configuration
spark = SparkSession \
    .builder \
    .appName("PySpark_Analytical_Functions") \
    .enableHiveSupport() \
    .getOrCreate()
spark.sparkContext.setLogLevel("WARN")

# Create sample data for dataframe
sampleData = (("Ram", 28, "Sales", 3000),
              ("Meena", 33, "Sales", 4600),
              ("Robin", 40, "Sales", 4100),
              ("Kunal", 25, "Finance", 3000),
              ("Ram", 28, "Sales", 3000),
              ("Srishti", 46, "Management", 3300),
              ("Jeny", 26, "Finance", 3900),
              ("Hitesh", 30, "Marketing", 3000),
              ("Kailash", 29, "Marketing", 2000),
              ("Sharad", 39, "Sales", 4100)
              )

# column names for dataframe
columns = ["Employee_Name", "Age", "Department", "Salary"]

df = spark.createDataFrame(data=sampleData, schema=columns)
df.show()

windowPartition = Window.partitionBy("Department").orderBy("Age")

""" cume_dist """
# importing cume_dist()
# from pyspark.sql.functions
from pyspark.sql.functions import cume_dist

# applying window function with the help of DataFrame.withColumn
df.withColumn("cume_dist", cume_dist().over(windowPartition)).show()

"""lag"""
# importing lag() from pyspark.sql.functions
from pyspark.sql.functions import lag

df.withColumn("Lag", lag("Salary", 2).over(windowPartition)) \
    .show()

"""lead"""
# importing lead() from pyspark.sql.functions
from pyspark.sql.functions import lead

df.withColumn("Lead", lead("salary", 2).over(windowPartition)) \
    .show()

#
spark.stop()