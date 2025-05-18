package org.examples.sb.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DataSourceRouter {

    private final DataSource region1DataSource;
    private final DataSource region2DataSource;

    public DataSourceRouter(@Qualifier("region1DataSource") DataSource region1DataSource,
                            @Qualifier("region2DataSource") DataSource region2DataSource) {
        this.region1DataSource = region1DataSource;
        this.region2DataSource = region2DataSource;
    }

   public DataSource getDataSource(String region) {
        if ("region1".equals(region)) {
            return region1DataSource;
        } else if ("region2".equals(region)) {
            return region2DataSource;
        } else {
            throw new IllegalArgumentException("Invalid region: " + region);
        }
    }
}
