#
#

spark-shell --jars /home/brijeshdhaker/git-repos/hbase-connectors/spark/hbase-spark/target/hbase-spark-1.0.1-SNAPSHOT.jar -c spark.ui.port=11111

import org.apache.hadoop.hbase.spark.HBaseContext
import org.apache.hadoop.hbase.HBaseConfiguration

val conf = HBaseConfiguration.create()

