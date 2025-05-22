package org.examples.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableWebFluxSecurity
public class SbGatewayModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbGatewayModuleApplication.class, args);
	}

}
