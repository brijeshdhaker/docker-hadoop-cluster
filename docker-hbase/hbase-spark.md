#
# Spark & Hbase
#


## Create Required HBase Table

create 'Person', 'Name', 'Address'
put 'Person', '1', 'Name:First', 'Raymond'
put 'Person', '1', 'Name:Last', 'Tang'
put 'Person', '1', 'Address:Country', 'Australia'
put 'Person', '1', 'Address:State', 'VIC'

put 'Person', '2', 'Name:First', 'Dnomyar'
put 'Person', '2', 'Name:Last', 'Gnat'
put 'Person', '2', 'Address:Country', 'USA'
put 'Person', '2', 'Address:State', 'CA'

## The table returns the following result when scanning:

$ hbase> scan 'Person'

ROW                             COLUMN+CELL
1                              column=Address:Country, timestamp=2021-02-05T20:48:42.088, value=Australia
1                              column=Address:State, timestamp=2021-02-05T20:48:46.750, value=VIC
1                              column=Name:First, timestamp=2021-02-05T20:48:32.544, value=Raymond
1                              column=Name:Last, timestamp=2021-02-05T20:48:37.085, value=Tang
2                              column=Address:Country, timestamp=2021-02-05T20:49:00.692, value=USA
2                              column=Address:State, timestamp=2021-02-05T20:49:04.972, value=CA
2                              column=Name:First, timestamp=2021-02-05T20:48:51.653, value=Dnomyar
2                              column=Name:Last, timestamp=2021-02-05T20:48:56.665, value=Gnat
2 row(s)

# Build HBase Spark connector

## 1) Clone the repository using the following command:

   git clone https://github.com/apache/hbase-connectors.git

## 2) Change directory to the clone repo:

   cd hbase-connectors/

## 3) Build the project using the following command:
   mvn -Dspark.version=3.0.1 -Dscala.version=2.12.10 -Dscala.binary.version=2.12 -Dhbase.version=2.2.4 -Dhadoop.profile=3.0 -Dhadoop-three.version=3.2.0 -DskipTests -Dcheckstyle.skip -U clean package
   The version arguments need to match with your Hadoop, Spark and HBase versions.

# Run Sparkshell

   spark-shell --jars /home/brijeshdhaker/git-repos/hbase-connectors/spark/hbase-spark/target/hbase-spark-1.0.1-SNAPSHOT.jar
   
   import org.apache.hadoop.hbase.spark.HBaseContext
   import org.apache.hadoop.hbase.HBaseConfiguration
   
   val conf = HBaseConfiguration.create()

## 1) First import the required classes:

   import org.apache.hadoop.hbase.spark.HBaseContext
   import org.apache.hadoop.hbase.HBaseConfiguration

## 2) Create HBase configurations

   val conf = HBaseConfiguration.create()
   conf.set("hbase.zookeeper.quorum", "127.0.0.1:10231")

## 3) Create HBase context

   // Instantiate HBaseContext that will be used by the following code
   new HBaseContext(spark.sparkContext, conf)

## 4) Create DataFrame
   val hbaseDF = (
   spark.read.format("org.apache.hadoop.hbase.spark")
   .option("hbase.columns.mapping",
   "rowKey STRING :key," +
   "firstName STRING Name:First, lastName STRING Name:Last," +
   "country STRING Address:Country, state STRING Address:State"
   )
   .option("hbase.table", "Person")
   ).load()

The columns mapping matches with the definition in the steps above.

## 5) Show DataFrame schema
   scala> hbaseDF.schema
   res2: org.apache.spark.sql.types.StructType = StructType(StructField(lastName,StringType,true), StructField(country,StringType,true), StructField(state,StringType,true), StructField(firstName,StringType,true), StructField(rowKey,StringType,true))

## 6) Show data
   hbaseDF.show()
   

# Use catalog

We can also define a catalog for the table Person created above and then use it to read data.

## 1) Define catalog

   def catalog = s"""{
   |"table":{"namespace":"default", "name":"Person"},
   |"rowkey":"key",
   |"columns":{
   |"rowkey":{"cf":"rowkey", "col":"key", "type":"string"},
   |"firstName":{"cf":"Name", "col":"First", "type":"string"},
   |"lastName":{"cf":"Name", "col":"Last", "type":"string"},
   |"country":{"cf":"Address", "col":"Country", "type":"string"},
   |"state":{"cf":"Address", "col":"State", "type":"string"}
   |}
   |}""".stripMargin

## 2) Use catalog

   Now the catalog can be directly passed into as tableCatalog option:
   
   import org.apache.hadoop.hbase.spark.datasources._
   
   (spark.read
   .options(Map(HBaseTableCatalog.tableCatalog->catalog))
   .format("org.apache.hadoop.hbase.spark")
   .load()).show()
   The code can also be simplified as:
   
   (spark.read.format("org.apache.hadoop.hbase.spark")
   .option("catalog",catalog)
   .load()).show()

## Output:
   scala> (spark.read
   | .options(Map(HBaseTableCatalog.tableCatalog->catalog))
   | .format("org.apache.hadoop.hbase.spark")
   | .load()).show()
   +--------+------+---------+-----+---------+
   |lastName|rowkey|  country|state|firstName|
   +--------+------+---------+-----+---------+
   |    Tang|     1|Australia|  VIC|  Raymond|
   |    Gnat|     2|      USA|   CA|  Dnomyar|
   +--------+------+---------+-----+---------+

## Summary
Unfortunately the connector packages for Spark 3.x are not published to Maven central repositories yet.

To save time for building hbase-connector project, you can download it from the ones I built using WSL: Release 1.0.1 HBase Connectors for Spark 3.0.1 · kontext-tech/hbase-connectors.



#
# PySpark & Hbase
#

## 1. copy hbase-site.xml to $SPARK_HOME/conf/ directory

   cp /usr/hdp/current/hbase-client/conf/hbase-site.xml spark2-client/conf/hbase-site.xml

## 2. 

   pyspark --packages com.hortonworks:shc-core:1.1.1-2.1-s_2.11 --repositories http://repo.hortonworks.com/content/groups/public/ --files /usr/hdp/current/hbase-master/conf/hbase-site.xml

## 3. Define Catalog for Hbase Table

   catalog = '{"table": {"namespace": "default", "name": "books"}, "rowkey": "key", "columns": {"title": {"cf": "rowkey", "col": "key", "type": "string"}, "author": {"cf": "info", "col": "author", "type": "string"} } }'

## 4. 

   df = spark.read.options(catalog=catalog).format('org.apache.spark.sql.execution.datasources.hbase').load()
   df.show()

   +--------------------+------------------+
   |               title|            author|
   +--------------------+------------------+
   | Godel, Escher, Bach|Douglas Hofstadter|
   |In Search of Lost...|     Marcel Proust|
   +--------------------+------------------+

## 5. 

   df.write\
   .options(catalog=catalog)\
   .format("org.apache.spark.sql.execution.datasources.hbase")\
   .save()