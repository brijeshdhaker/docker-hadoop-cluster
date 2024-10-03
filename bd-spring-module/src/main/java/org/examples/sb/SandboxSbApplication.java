package org.examples.sb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
@Slf4j
@EnableKafka
@SpringBootApplication
public class SandboxSbApplication {

	public static void main(String[] args) {

		log.info("DemoApplication Starting...");
		SpringApplication.run(SandboxSbApplication.class, args);
		log.info("DemoApplication Started...");

	}

}
