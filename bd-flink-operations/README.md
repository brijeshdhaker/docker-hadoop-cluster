# Flink Operations Playground Image

The image defined by the `Dockerfile` in this folder is required by the Flink operations playground.

The image is based on the official Flink image and adds a demo Flink job (Click Event Count) and a corresponding data generator. The code of the application is located in the `./java/flink-ops-playground` folder.

### https://www.galiglobal.com/blog/2021/20210130-Flink-setup.html

mvn archetype:generate                               \
-DarchetypeGroupId=org.apache.flink \
-DarchetypeArtifactId=flink-quickstart-java \
-DarchetypeVersion=1.19.0 \
-DgroupId=com.org.examples \
-DartifactId=flink-playground \
-Dversion=1.0.0 \
-Dpackage=com.org.examples.flink \
-DinteractiveMode=false


mvn clean package -Pbuild-jar

$FLINK_HOME/bin/flink run target/flink-playground-0.1.jar

grep -R "example job" $FLINK_HOME/log/

export HADOOP_CLASSPATH=`hadoop classpath`

```java
public class StreamingJob {

	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		/*
		 * Here, you can start creating your execution plan for Flink.
		 *
		 * Start with getting some data from the environment, like
		 * 	env.readTextFile(textPath);
		 *
		 * then, transform the resulting DataStream<String> using operations
		 * like
		 * 	.filter()
		 * 	.flatMap()
		 * 	.join()
		 * 	.coGroup()
		 *
		 * and many more.
		 * Have a look at the programming guide for the Java API:
		 *
		 * https://flink.apache.org/docs/latest/apis/streaming/index.html
		 *
		 */

		// execute program
		env.execute("Flink Streaming Java API Skeleton");
	}
}
```