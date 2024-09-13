package org.examples.spark.utils

object Utils {

  // a regular expression which matches commas but not commas within double quotations
  val COMMA_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"

  def isWindowsOS(): Boolean ={
    val os = System.getProperty("os.name").toUpperCase();
    return (os.indexOf("WIN") >= 0);
  }

  def setHadoopHomeDir(): Unit ={

    System.setProperty("HADOOP_HOME","");
    System.setProperty("hadoop.home.dir","");

  }

}
