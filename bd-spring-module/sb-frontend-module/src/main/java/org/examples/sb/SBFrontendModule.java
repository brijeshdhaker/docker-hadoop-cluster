package org.examples.sb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SBFrontendModule {

    public static void main(String[] args) {

        log.info("SB Frontend Module Starting...");
        SpringApplication.run(SBFrontendModule.class, args);
        log.info("SB Frontend Module Started...");

    }

}
