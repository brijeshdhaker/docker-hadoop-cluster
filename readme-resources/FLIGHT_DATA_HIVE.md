#
## Hive Flight Database 
#

![hive-airlines-tables.png](images%2Fhive-airlines-tables.png)

```

CREATE DATABASE IF NOT EXISTS flightsdb;

CREATE EXTERNAL TABLE IF NOT EXISTS flights (
	year string, 
	month string, 
	dayofmonth string, 
	dayofweek string, 
	deptime string,
	crsdeptime string, 
	arrtime string, 
	crsarrtime string, 
	uniquecarrier string,
	flightnum string, 
	tailnum string, 
	actualelapsedtime int, 
	crselapsedtime int,
	airtime int, 
	arrdelay int, 
	depdelay int, 
	origin string, 
	dest string,
	distance int, 
	taxiin int, 
	taxiout int, 
	cancelled string, 
	cancellationcode string, 
	diverted string, 
	carrierdelay int, 
	weatherdelay int, 
	nasdelay int, 
	securitydelay int,
	lateaircraftdelay int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY ‘,’ 
STORED AS TEXTFILE LOCATION ‘hdfs://namenode:9000/user/cloudera/flight_data’
tblproperties(“skip.header.line.count”=”1");

CREATE EXTERNAL TABLE IF NOT EXISTS airlines (
	code STRING, 
	description STRING
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY ‘,’ 
STORED AS TEXTFILE LOCATION ‘hdfs://namenode:9000/user/cloudera/airlines’ 
tblproperties(“skip.header.line.count”=”1");


CREATE EXTERNAL TABLE IF NOT EXISTS airports (
	iata STRING, 
	airport STRING, 
	city STRING, 
	state STRING, 
	country STRING, 
	lat STRING, 
	lon STRING
)

ROW FORMAT DELIMITED FIELDS TERMINATED BY ‘,’ 
STORED AS TEXTFILE LOCATION ‘hdfs://namenode:9000/user/cloudera/airports’
tblproperties(“skip.header.line.count”=”1");

CREATE EXTERNAL TABLE IF NOT EXISTS planes (
	tailnum STRING, 
	type STRING, 
	manufacturer STRING,
	issue_date STRING, 
	model STRING, 
	status STRING,
	aircraft_type STRING, 
	engine_type STRING, 
	year INT
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY “,” ESCAPED BY ‘\\’
STORED AS TEXTFILE LOCATION ‘hdfs://namenode:9000/user/cloudera/planes’
tblproperties(“skip.header.line.count”=”1");

```


.schema(airlineSchema)\

airlinesDF = spark.read.format("csv") \
.option("header", True) \
.option("delimiter", ',') \
.load("file:///apps/hostpath/datasets/flight_data/airlines.csv")

airlinesDF.printSchema()
airlinesDF.write.saveAsTable(name='default.airlines', format='hive', mode='append')

airportsDF = spark.read.format("csv") \
.option("header", True) \
.option("delimiter", ',') \
.load("file:///apps/hostpath/datasets/flight_data/airports.csv")

airportsDF.printSchema()
airportsDF.show(truncate=False)
airportsDF.write.saveAsTable(name='default.airports', format='hive', mode='append')


planesDF = spark.read.format("csv") \
.option("header", True) \
.option("delimiter", ',') \
.load("file:///apps/hostpath/datasets/flight_data/planes.csv")

planesDF.printSchema()
planesDF.show(truncate=False)
planesDF.write.saveAsTable(name='default.planes', format='hive', mode='append')


flightsDF = spark.read.format("csv") \
.option("header", True) \
.option("delimiter", ',') \
.load("file:///apps/hostpath/datasets/flight_data/flight_2008.csv")

flightsDF.printSchema()
flightsDF.show(truncate=False)
flightsDF.write.saveAsTable(name='default.flights', format='hive', mode='append')

ticketsDF = spark.read.format("csv") \
.option("header", True) \
.option("delimiter", ',') \
.load("file:///apps/hostpath/datasets/flight_data/passengertickets.csv")

ticketsDF.printSchema()
ticketsDF.show(truncate=False)
ticketsDF.write.saveAsTable(name='default.passenger_tickets', format='hive', mode='append')