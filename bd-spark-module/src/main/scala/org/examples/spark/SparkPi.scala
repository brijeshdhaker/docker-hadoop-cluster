/*

mvn exec:exec@run-local -Drunclass=org.examples.spark.SparkPi -Dparams="50"

$SPARK_HOME/bin/spark-submit \
--class org.examples.spark.SparkPi \
--name "SparkPi" \
--master local[4] \
--packages org.apache.spark:spark-avro_2.12:3.4.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 \
--conf "spark.driver.extraJavaOptions='-Divy.cache.dir=/apps/.ivy2/cache -Divy.home=/apps/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf "spark.executor.extraJavaOptions='-Divy.cache.dir=/apps/.ivy2/cache -Divy.home=/apps/.ivy2 -Djava.security.krb5.conf=/etc/krb5.conf'" \
--conf spark.executorEnv.SPARK_LOG_DIR=/apps/var/logs \
--conf spark.executorEnv.SPARK_LOG_FILE=SparkPi \
bd-spark-module/dist/bd-spark-module-1.0-SNAPSHOT.jar

*/


package org.examples.spark
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}
import org.examples.config.AbstractAppConfig

import scala.math.random

object SparkPi {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Spark Pi").setMaster("local[4]")
    val spark = new SparkContext(conf)
    val slices = if (args.length > 0) args(0).toInt else 2

    val n = math.min(1000L * slices, Int.MaxValue).toInt // avoid overflow

    val count = spark.parallelize(1 until n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y < 1) 1 else 0
    }.reduce(_ + _)

    println("Pi is roughly " + 4.0 * count / n)


    val fs = FileSystem.get(spark.hadoopConfiguration)
    fs.listStatus(new Path("/")).foreach(x => println(x.getPath))
    println("fs.getHomeDirectory() " + fs.getHomeDirectory())
    println("fs.getWorkingDirectory() " + fs.getWorkingDirectory())
    println("fs.getClass.getCanonicalName) " + fs.getClass.getCanonicalName)
    println("s.getCanonicalServiceName " + fs.getCanonicalServiceName)

    //FileSystem.get(spark.hadoopConfiguration).append()




  }

}
