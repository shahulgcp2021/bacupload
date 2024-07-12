package com.bacuti.multitenancy.config;

import com.bacuti.multitenancy.property.DataSourceProperties;
import com.bacuti.multitenancy.tenant.TenantRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = { "com.bacuti" })
@EnableJpaRepositories(basePackages = { "com.bacuti.repository" })
public class DataSourceConfiguration {

    @Bean(name = "dataSources")
    @Primary
    @DependsOn("dataSourceProperties")
    public Map<Object, Object> getDataSources(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.getDataSources().getTenants().stream().map(tenant -> {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setPoolName("HikariPool-" + tenant);
            dataSource.setJdbcUrl(dataSourceProperties.getDataSources().getUrl()+tenant);
            dataSource.setUsername(dataSourceProperties.getDataSources().getUsername());
            dataSource.setPassword(dataSourceProperties.getDataSources().getPassword());
            dataSource.setAutoCommit(false);
            // Configure HikariCP properties as needed
            return new TenantIdDataSource(tenant, dataSource);
        }).collect(Collectors.toMap(TenantIdDataSource::getTenantId, TenantIdDataSource::getDataSource));
    }

    @Bean(name = "tenantRoutingDataSource")
    @DependsOn("dataSources")
    public DataSource dataSource(Map<Object, Object> dataSources) {
        AbstractRoutingDataSource tenantRoutingDataSource = new TenantRoutingDataSource();
        tenantRoutingDataSource.setTargetDataSources(dataSources);
        tenantRoutingDataSource.setDefaultTargetDataSource(dataSources.get("defaultdb"));
        tenantRoutingDataSource.afterPropertiesSet();
        return tenantRoutingDataSource;
    }

    private class TenantIdDataSource {
        private Object tenantId;
        private Object dataSource;

        public TenantIdDataSource(Object tenantId, Object dataSource) {
            this.tenantId = tenantId;
            this.dataSource = dataSource;
        }

        public Object getTenantId() {
            return tenantId;
        }

        public void setTenantId(Object tenantId) {
            this.tenantId = tenantId;
        }

        public Object getDataSource() {
            return dataSource;
        }

        public void setDataSource(Object dataSource) {
            this.dataSource = dataSource;
        }
    }
}
