package org.examples.sb.helpers;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> currentDataSource = new ThreadLocal<>();

    public static void setCurrentDataSource(String dataSourceName) {
        currentDataSource.set(dataSourceName);
    }

    public static void clearCurrentDataSource() {
            currentDataSource.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        //return DataSourceContextHolder.getCurrentRegion();
        return currentDataSource.get();
    }
}
