HADOOP_CLASSPATH=`hadoop classpath` /home/foouser/flink-1.9.0/bin/flink run \
-m yarn-cluster \
-yqu some_yarn_queue \
-ynm "Flink on YARN" \
-yt /home/foouser/important_files \
-p 4 -yjm 1024m -ytm 1024m \
--class HelloWorld \
/home/foouser/foo.jar

#
#
#
./bin/flink run examples/streaming/WordCount.jar --input s3a://warehouse-flink/README.md --output s3a://warehouse-flink/output/3f7e50176f1221e967d3ca0d22ebaab1