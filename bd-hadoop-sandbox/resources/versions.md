#
#
#
Component	            Version
Apache Arrow	    0.8.0
Apache Atlas	    2.2.0
Apache Calcite	    1.19.0
Apache Avro	        1.8.2
Apache Hadoop 	    3.1.1
Apache HBase	    2.4.6
Apache Hive	        3.1.3000
Apache Impala	    4.0.0
Apache Kafka	    3.1.1
Apache Knox	        1.3.0
Apache Kudu	        1.15.0
Apache Livy	        0.6.0
Apache MapReduce	3.1.1
Apache Ozone	    1.2.0
Apache Oozie	    5.1.0
Apache ORC	        1.5.1
Apache Parquet	    1.10.99
Apache Phoenix	    5.1.1
Apache Ranger	    2.3.0
Apache Solr	        8.4.1
Apache Spark 2.x	2.4.8
Apache Spark 3.x	CDS
Apache Sqoop	    1.4.7
Apache Tez	        0.10.2
Apache Zeppelin	    0.8.2
Apache ZooKeeper	3.5.5

#
#
#
Filesystem	Path						                                User:Group	  Permissions
local		dfs.namenode.name.dir				                        hdfs:hadoop	    drwx------
local		dfs.datanode.data.dir				                        hdfs:hadoop	    drwx------
local		$HADOOP_LOG_DIR					                            hdfs:hadoop	    drwxrwxr-x
local		$YARN_LOG_DIR					                            yarn:hadoop	    drwxrwxr-x
local		yarn.nodemanager.local-dirs (/yarn/nm)		                yarn:hadoop	    drwxr-xr-x
local		yarn.nodemanager.log-dirs (/yarn/container-logs)			yarn:hadoop	    drwxr-xr-x
local		container-executor				                            root:hadoop	    --Sr-s--* (6050)
local		conf/container-executor.cfg			                        root:hadoop	    r-------* (0400)
hdfs		/						                                    hdfs:hadoop	    drwxr-xr-x
hdfs		/tmp						                                hdfs:hadoop	    drwxrwxrwxt
hdfs		/user						                                hdfs:hadoop	    drwxr-xr-x
hdfs		yarn.nodemanager.remote-app-log-dir		                    yarn:hadoop	    drwxrwxrwxt
hdfs		mapreduce.jobhistory.intermediate-done-dir	                mapred:hadoop	drwxrwxrwxt
hdfs		mapreduce.jobhistory.done-dir			                    mapred:hadoop	drwxr-x---