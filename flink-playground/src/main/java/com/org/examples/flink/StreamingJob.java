/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.org.examples.flink;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="https://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.*;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StreamingJob {

	private static final Logger LOG = LoggerFactory.getLogger(StreamingJob.class);

	private SourceFunction<Long> source;
	private SinkFunction<Long> sink;

	public StreamingJob(SourceFunction<Long> source, SinkFunction<Long> sink) {
		this.source = source;
		this.sink = sink;
	}

	public void execute() throws Exception {

		Properties props = new Properties();
		props.put("metrics.reporter.jmx.factory.class", "org.apache.flink.metrics.jmx.JMXReporterFactory");
		Configuration conf = ConfigurationUtils.createConfiguration(props);

		/*
		List<String> filesToAccesslist = new ArrayList();
		filesToAccesslist.add("hdfs://namenode.sandbox.net:9000");
		//TaskManagerOptions
		conf.set(SecurityOptions.KERBEROS_HADOOP_FILESYSTEMS_TO_ACCESS, filesToAccesslist);
		conf.set(SecurityOptions.DELEGATION_TOKEN_PROVIDER_ENABLED, Boolean.TRUE);
		conf.set(SecurityOptions.KERBEROS_KRB5_PATH, "/etc/krb5.conf");
		conf.set(SecurityOptions.KERBEROS_LOGIN_KEYTAB, "/apps/security/keytabs/users/flink.keytab");
		conf.set(SecurityOptions.KERBEROS_LOGIN_PRINCIPAL, "flink@SANDBOX.NET");
		conf.set(SecurityOptions.KERBEROS_LOGIN_USETICKETCACHE, Boolean.FALSE);
		//conf.set()
		conf.set(ConfigOptions.key("fs.default-scheme").stringType().defaultValue("hdfs://namenode.sandbox.net:9000/"), "hdfs://namenode.sandbox.net:9000/");
		conf.set(ConfigOptions.key("env.hadoop.conf.dir").stringType().defaultValue("/apps/sandbox/hadoop-3.3.4/client/secure/"), "/apps/sandbox/hadoop-3.3.4/client/secure/");
		*/
		//
		/*
		conf.set(CheckpointingOptions.CHECKPOINT_STORAGE, "filesystem");
		conf.set(CheckpointingOptions.CHECKPOINTS_DIRECTORY, "s3a://flink/checkpoints");
		conf.set(CheckpointingOptions.SAVEPOINT_DIRECTORY, "s3a://flink/savepoints");
		*/

		//
		conf.set(ConfigOptions.key("s3.access-key").stringType().defaultValue("ffaJ6a2MOj8mZ5lI3P6h"), "ffaJ6a2MOj8mZ5lI3P6h");
		conf.set(ConfigOptions.key("s3.secret-key").stringType().defaultValue("9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs"), "9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs");
		conf.set(ConfigOptions.key("s3.endpoint").stringType().defaultValue("http://minio.sandbox.net:9010"), "http://minio.sandbox.net:9010");
		conf.set(ConfigOptions.key("s3.path-style").booleanType().defaultValue(Boolean.TRUE), Boolean.TRUE);
		conf.set(ConfigOptions.key("s3.path.style.access").booleanType().defaultValue(Boolean.TRUE), Boolean.TRUE);

		//
		String CLUSTER_TYPE = System.getenv("CLUSTER_TYPE");

		// Local Environment Without GUI
		// StreamExecutionEnvironment.createLocalEnvironment();

		// Local Environment With GUI
		StreamExecutionEnvironment env;
		if(CLUSTER_TYPE.equals("SANDBOX3")) {
			env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
		}else{
			env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
		}

		LOG.debug("Start Flink example job");

//        DataStreamSink<Long> logTestStream = env.fromElements(0L, 1L, 2L)
//            .map(new IncrementMapFunction())
//            .addSink(sink);
		// file:///apps/hostpath/datasets/airports.text
		// hdfs://namenode.sandbox.net:9000/apps/hostpath/datasets/airports.text
		DataStream<String> text = env.readTextFile("s3://openlake/flight-data/airlines.csv");
		text.print();

		/*
		DataStream<Long> longStream = env.addSource(source).returns(TypeInformation.of(Long.class));
		longStream.map(new IncrementMapFunction()).addSink(sink);
		*/

		LOG.debug("Stop Flink example job");


		env.execute();
	}

	public static void main(String[] args) throws Exception {
		StreamingJob job = new StreamingJob(new RandomLongSource(), new PrintSinkFunction<>());
		job.execute();
	}

}