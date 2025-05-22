package org.examples.sb;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SBFrontendModule {

    public static void main(String[] args) {

        log.info("SB Frontend Module Starting...");
        SpringApplication.run(SBFrontendModule.class, args);
        log.info("SB Frontend Module Started...");

        // Load properties file and set properties used throughout the sample
        //Properties properties = new Properties();
        //properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("graph.properties"));

    }

}
