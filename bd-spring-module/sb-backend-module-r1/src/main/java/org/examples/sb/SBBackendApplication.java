package org.examples.sb;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.examples.sb.utils.YamlLoaderInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.Random;

@Slf4j
@EnableKafka
@EnableMethodSecurity
@SpringBootApplication
public class SBBackendApplication {

	public static void main(String[] args) {

		log.info("SB Backend Application Starting...");
		/*
		SpringApplication application = new SpringApplication(SBBackendApplication.class);
		ApplicationContextInitializer<ConfigurableApplicationContext> yamlInitializer = new YamlLoaderInitializer("application.yml");
		application.addInitializers(yamlInitializer);
		application.run(args);
		*/
		SpringApplication.run(SBBackendApplication.class, args);
		log.info("SB Backend Application Started...");

	}

	@PostConstruct
	public void  init(){

		Random random = new Random();
		for(int i= 0; i< 1000; i++){
			random.nextInt();
		}
	}

}
