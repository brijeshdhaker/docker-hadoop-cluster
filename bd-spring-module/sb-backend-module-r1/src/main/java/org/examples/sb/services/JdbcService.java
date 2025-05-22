package org.examples.sb.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class JdbcService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcService(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getDataFromDb1() {

        //RoutingDataSource.setCurrentDataSource("db-R1");
        return jdbcTemplate.queryForList("SELECT * FROM USERS");

    }

    public List<Map<String, Object>> getDataFromDb2() {
        
        //RoutingDataSource.setCurrentDataSource("db-R2");
        return jdbcTemplate.queryForList("SELECT * FROM USERS");

    }

}
