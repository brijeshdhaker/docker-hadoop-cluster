package org.examples.sb.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.examples.sb.helpers.RoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {

    
    @Bean(name = "region1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.region1")
    public DataSource region1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "region2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.region2")
    public DataSource region2DataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Primary
    @Bean
    public DataSource routingDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("db-R1", region1DataSource());
        targetDataSources.put("db-R2", region2DataSource());

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(region1DataSource());
        return routingDataSource;
    }

    @Primary
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("routingDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
