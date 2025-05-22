package org.examples.sb.helpers;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiRegionDataSourceRouter extends AbstractRoutingDataSource {

    private RegionResolver regionResolver;

    @Override
    protected Object determineCurrentLookupKey() {
       return regionResolver.resolveCurrentRegion();
    }

    public void setTenantResolver(RegionResolver tenantResolver) {
        this.regionResolver = tenantResolver;
    }
}