package org.examples.spark;

import org.apache.spark.sql.SparkSession;

public class SparkAppWorkflow {


    public static void main(String[] arg){
        System.out.println("Hello");

        SparkSession spark = SparkSession
          .builder()
          .master("local[*]")  // // spark://spark-iceberg.sandbox.net:7077")
          .appName("Java Spark SQL basic example")
//          .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
          .config("spark.eventLog.enabled", "true")
          .config("spark.jars.ivy", "/apps/.ivy2")
          .config("spark.eventLog.dir", "/apps/var/logs/spark-events")
          .config("spark.history.fs.logDirectory", "/apps/var/logs/spark-events")
//
          .config("spark.hadoop.fs.s3a.endpoint", "http://minio.sandbox.net:9010")
          .config("spark.hadoop.fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO")
          .config("spark.hadoop.fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG")
          .config("spark.hadoop.fs.s3a.path.style.access", "true")
          .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
          .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
         .getOrCreate();


        spark.sparkContext().setLogLevel("ERROR");
        
    }

}
