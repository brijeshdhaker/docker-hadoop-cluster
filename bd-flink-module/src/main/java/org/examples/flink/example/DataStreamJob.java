package org.examples.flink.example;

import org.examples.flink.example.models.Person;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.api.common.functions.FilterFunction;

import java.io.File;
import java.net.URL;


public class DataStreamJob {

	public static void main(String[] args) throws Exception {

		URL url = ClassLoader.getSystemResource(".");
		File file = (url != null) ? new File(url.getFile()) : new File(".");

		final org.apache.flink.configuration.Configuration config = GlobalConfiguration.loadConfiguration(file.getAbsolutePath());
		FileSystem.initialize(config);
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(2, config);

		DataStream<Person> flintstones = env.fromElements(
				new Person("Fred", 35),
				new Person("Wilma", 35),
				new Person("Pebbles", 2));

		DataStream<Person> adults = flintstones.filter(new FilterFunction<Person>() {
			@Override
			public boolean filter(Person person) throws Exception {
				return person.age >= 18;
			}
		});


		adults.print();

		/*
		URI path = new URI("s3a://defaultfs/README.md");
		FileSystem fs = FileSystem.get(path);
		Path hd = fs.getHomeDirectory();
		Path wd = fs.getWorkingDirectory();
		Boolean isExists = fs.exists(new Path(path));
		*/
		env.execute();
	}
}