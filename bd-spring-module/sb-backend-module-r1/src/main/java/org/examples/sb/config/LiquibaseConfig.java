package org.examples.sb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import liquibase.integration.spring.SpringLiquibase;

//@Configuration
public class LiquibaseConfig {

    @Autowired()
    @Qualifier("routingDataSource")
    DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/master.yml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    /* 
    @Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") TaskExecutor taskExecutor,
            @Qualifier("routingDataSource") DataSource dataSource, LiquibaseProperties liquibaseProperties) {

        // Use liquibase.integration.spring.SpringLiquibase if you don't want Liquibase to start asynchronously
        SpringLiquibase liquibase = new SpringLiquibase(taskExecutor, env);
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:liquibase/master.yml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            log.debug("Configuring Liquibase");
        }
        return liquibase;
    }
    */
}
