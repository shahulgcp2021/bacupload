package com.bacuti.multitenancy.tenant;

import com.bacuti.common.errors.InvalidTenantException;
import com.bacuti.multitenancy.property.DataSourceProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getHeader("X-TENANT-ID");

        if (tenantId == null || tenantId.isEmpty()) {
            throw new InvalidTenantException("Missing X-TENANT-ID header", HttpStatus.BAD_REQUEST.value());
        }
        System.out.println("The tenantId is "+tenantId);
        List<String> tenants = dataSourceProperties.getDataSources().getTenants();
        System.out.println("The tenant ids are "+ tenants);
        if (!tenants.contains(tenantId)) {
            throw new InvalidTenantException("Invalid tenant ID", HttpStatus.FORBIDDEN.value());
        }
        TenantContext.setCurrentTenant(tenantId);
        return true;
    }
}

