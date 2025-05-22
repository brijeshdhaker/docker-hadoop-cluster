package org.examples.sb.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class HeaderRegionResolver implements RegionResolver{

    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Value("${spring.application.instance-id}")
    protected String instanceId;

    @Override
    public String resolveCurrentRegion() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String tenantId = attributes.getRequest().getHeader(TENANT_HEADER);
            if (tenantId != null) {
                return tenantId;
            }
        }
        // Default tenant/region
        return instanceId.toUpperCase(); 
    }

}
