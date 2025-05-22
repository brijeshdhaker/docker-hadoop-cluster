package org.examples.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SbDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbDiscoveryServiceApplication.class, args);
	}

}
