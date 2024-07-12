package com.bacuti.communication;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.config.AwsParameterStoreService;
import com.bacuti.multitenancy.bean.MultiTenantDataSourceSpringLiquibase;
import com.bacuti.multitenancy.property.DataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Service
public class InterService {

    private final Logger logger = LoggerFactory.getLogger(InterService.class);

    private final DataSourceProperties dataSourceProperties;

    @Qualifier("dataSources")
    private final Map<Object, Object> dataSources;

    @Qualifier("tenantRoutingDataSource")
    private final AbstractRoutingDataSource abstractRoutingDataSource;

    private final MultiTenantDataSourceSpringLiquibase multiTenantDataSourceSpringLiquibase;

    private final AwsParameterStoreService awsParameterStoreService;
    private final DataSource dataSource;

    @Autowired
    public InterService(
        AwsParameterStoreService awsParameterStoreService,
        DataSource dataSource,
        DataSourceProperties dataSourceProperties,
        @Qualifier("dataSources") Map<Object, Object> dataSources,
        @Qualifier("tenantRoutingDataSource") AbstractRoutingDataSource abstractRoutingDataSource,
        MultiTenantDataSourceSpringLiquibase multiTenantDataSourceSpringLiquibase
    ) {
        this.awsParameterStoreService = awsParameterStoreService;
        this.dataSource = dataSource;
        this.dataSourceProperties = dataSourceProperties;
        this.dataSources = dataSources;
        this.abstractRoutingDataSource = abstractRoutingDataSource;
        this.multiTenantDataSourceSpringLiquibase = multiTenantDataSourceSpringLiquibase;
    }
    public String createDBForCompany(String domain) {
        // Sanitize the database name
        String dbName = sanitizeDatabaseName(domain);
        if(dataSources.containsKey(domain)) {
            logger.info("The database {} is already created", domain);
            return domain;
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // Disable auto-commit mode to ensure no transaction is active
            connection.setAutoCommit(true);

            // Create the new database
            String createDbQuery = "CREATE DATABASE " + dbName;
            statement.executeUpdate(createDbQuery);

            // Update the AWS Parameter Store with the new database name
            String key = dataSourceProperties.getDataSources().getParamtenantkey();
            awsParameterStoreService.putParameter(key, dbName);
            updateDataSources(dbName);
        } catch (SQLException e) {
            throw new BusinessException("Error creating database: " + e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (LiquibaseException e) {
            throw new BusinessException("Error creating database: " + e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return dbName;
    }

    private void updateDataSources(String companyName) throws LiquibaseException, InterruptedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceProperties.getDataSources().getUrl() + companyName);
        dataSource.setUsername(dataSourceProperties.getDataSources().getUsername());
        dataSource.setPassword(dataSourceProperties.getDataSources().getPassword());
        dataSource.setAutoCommit(false);

        dataSourceProperties.getDataSources().getTenants().add(companyName);
        dataSources.put(companyName, dataSource);
        abstractRoutingDataSource.afterPropertiesSet();
        multiTenantDataSourceSpringLiquibase.runOnAllDataSources();
    }

    @Scheduled(fixedRate = 600000) // Every 10 minutes
    public void updateDataSources() throws LiquibaseException, InterruptedException {
        String key = dataSourceProperties.getDataSources().getParamtenantkey();
        String tenantList = awsParameterStoreService.getParameter(key);
        List<String> tenants = List.of(tenantList.split(","));
        for(String tenant : tenants) {
            if(!dataSources.containsKey(tenant)) {
                updateDataSources(tenant);
            }
        }
    }

    private String sanitizeDatabaseName(String companyName) {
        // Convert to lowercase, replace not allowed characters with underscore, and trim to 63 characters
        String sanitized = companyName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        return sanitized.length() > 63 ? sanitized.substring(0, 63) : sanitized;
    }




}
