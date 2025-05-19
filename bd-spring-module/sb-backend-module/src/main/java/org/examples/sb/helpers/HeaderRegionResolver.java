package org.examples.sb.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class HeaderRegionResolver implements RegionResolver{

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public String resolveCurrentRegion() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String tenantId = attributes.getRequest().getHeader(TENANT_HEADER);
            if (tenantId != null) {
                return tenantId;
            }
        }

        return "db-R2"; // Default tenant/region
    }

}
