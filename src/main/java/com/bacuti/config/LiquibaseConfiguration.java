package com.bacuti.config;

import com.bacuti.multitenancy.bean.MultiTenantDataSourceSpringLiquibase;
import com.bacuti.multitenancy.property.DataSourceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(LiquibaseProperties.class)
public class LiquibaseConfiguration {

    private final LiquibaseProperties properties;
    private final DataSourceProperties dataSourceProperties;

    public LiquibaseConfiguration(LiquibaseProperties properties, DataSourceProperties dataSourceProperties) {
        this.properties = properties;
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    @DependsOn("tenantRoutingDataSource")
    public MultiTenantDataSourceSpringLiquibase liquibaseMultiTenancy(Map<Object, Object> dataSources,
                                                                      @Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        // to run changeSets of the liquibase asynchronous
        MultiTenantDataSourceSpringLiquibase liquibase = new MultiTenantDataSourceSpringLiquibase(taskExecutor);
        dataSources.forEach((tenant, dataSource) -> liquibase.addDataSource((String) tenant, (DataSource) dataSource));
        dataSourceProperties.getDataSources().getTenants().forEach(tenant -> {
            if (dataSourceProperties.getDataSources().getLiquibase() != null) {
                liquibase.addLiquibaseProperties(tenant, dataSourceProperties.getDataSources().getLiquibase());
            }
        });

        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setLiquibaseSchema(properties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(properties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(properties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(properties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }

}
