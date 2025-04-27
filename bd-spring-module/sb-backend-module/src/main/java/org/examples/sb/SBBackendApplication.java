package org.examples.sb;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Random;

@Slf4j
@EnableKafka
@SpringBootApplication
public class SBBackendApplication {

	public static void main(String[] args) {

		log.info("SB Backend Application Starting...");
		SpringApplication.run(SBBackendApplication.class, args);
		log.info("SB Backend Application Started...");

	}

	@PostConstruct
	public void  init(){

		Random random = new Random();
		for(int i= 0; i< 1000; i++){

		}
	}

}
