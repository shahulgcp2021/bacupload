package com.bacuti.multitenancy.bean;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StopWatch;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MultiTenantDataSourceSpringLiquibase implements InitializingBean, ResourceLoaderAware {

    public static final String DISABLED_MESSAGE = "Liquibase is disabled";
    public static final String STARTING_ASYNC_MESSAGE = "Starting Liquibase asynchronously, your database might not be ready at startup!";
    public static final String STARTING_SYNC_MESSAGE = "Starting Liquibase synchronously";
    public static final String STARTED_MESSAGE = "Liquibase has updated your database in {} ms";
    public static final String EXCEPTION_MESSAGE = "Liquibase could not start correctly, your database is NOT ready: {}";

    public static final long SLOWNESS_THRESHOLD = 5; // seconds
    public static final String SLOWNESS_MESSAGE = "Warning, Liquibase took more than {} seconds to start up!";

    private static final Logger log = LoggerFactory.getLogger(MultiTenantDataSourceSpringLiquibase.class);

    @Autowired
    private final Map<Object, Object> dataSources = new HashMap<>();

    private final Map<String, LiquibaseProperties> propertiesDataSources = new HashMap<>();

    @lombok.Setter
    private ResourceLoader resourceLoader;

    private String changeLog;
    private String contexts;
    private String labels;
    private Map<String, String> parameters;
    private String defaultSchema;
    private String liquibaseSchema;
    private String liquibaseTablespace;
    private String databaseChangeLogTable;
    private String databaseChangeLogLockTable;
    private boolean dropFirst;
    private boolean shouldRun = true;
    private File rollbackFile;

    private final TaskExecutor taskExecutor;

    public MultiTenantDataSourceSpringLiquibase(@Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public MultiTenantDataSourceSpringLiquibase() {
        this.taskExecutor = null;
    }

    public void addDataSource(String tenant, DataSource dataSource) {
        this.dataSources.put(tenant, dataSource);
    }

    public void addLiquibaseProperties(String tenant, LiquibaseProperties properties) {
        this.propertiesDataSources.put(tenant, properties);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DataSources based multiTenancy enabled");
        runOnAllDataSources();
    }

    public void runOnAllDataSources() throws LiquibaseException, InterruptedException {
        // Create an executor for asynchronous tasks
        Executor executor = Executors.newCachedThreadPool();

        // Use CountDownLatch to wait for all tasks to complete
        CountDownLatch latch = new CountDownLatch(dataSources.size());

        dataSources.forEach((tenant, dataSource) -> {
            log.info("Initializing Liquibase for data source " + tenant);
            final LiquibaseProperties lProperty = propertiesDataSources.get(tenant);
            SpringLiquibase liquibase = lProperty != null ? getSpringLiquibase((DataSource) dataSource, lProperty) : getSpringLiquibase((DataSource) dataSource);

            // Execute tasks asynchronously
            executor.execute(() -> {
                try {
                    log.warn("Starting asynchronous initialization for " + tenant);
                    initDb(liquibase);
                } catch (LiquibaseException e) {
                    log.error("Exception occurred during Liquibase initialization for " + tenant + ": " + e.getMessage(), e);
                } finally {
                    latch.countDown(); // Signal completion of this task
                }
            });

            log.info("Liquibase initialization started for " + tenant);
        });

        // Wait for all tasks to complete
        latch.await();

        log.info("All Liquibase initialization tasks completed successfully.");
    }

    private void initDb(SpringLiquibase liquibase) throws LiquibaseException {
        StopWatch watch = new StopWatch();
        watch.start();
        liquibase.afterPropertiesSet();
        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
            log.warn(SLOWNESS_MESSAGE, SLOWNESS_THRESHOLD);
        }
    }

    private SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changeLog);
        liquibase.setChangeLogParameters(parameters);
        liquibase.setContexts(contexts);
        liquibase.setDropFirst(dropFirst);
        liquibase.setShouldRun(shouldRun);
        liquibase.setRollbackFile(rollbackFile);
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setLiquibaseSchema(liquibaseSchema);
        liquibase.setLiquibaseTablespace(liquibaseTablespace);
        liquibase.setDatabaseChangeLogTable(databaseChangeLogTable);
        liquibase.setDatabaseChangeLogLockTable(databaseChangeLogLockTable);
        return liquibase;
    }

    private SpringLiquibase getSpringLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setRollbackFile(properties.getRollbackFile());
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setLiquibaseSchema(properties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(properties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogTable(properties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(properties.getDatabaseChangeLogLockTable());
        return liquibase;
    }

    // Getters and setters for all fields

    public Map<Object, Object> getDataSources() {
        return dataSources;
    }

    public Map<String, LiquibaseProperties> getPropertiesDataSources() {
        return propertiesDataSources;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public String getLiquibaseSchema() {
        return liquibaseSchema;
    }

    public void setLiquibaseSchema(String liquibaseSchema) {
        this.liquibaseSchema = liquibaseSchema;
    }

    public String getLiquibaseTablespace() {
        return liquibaseTablespace;
    }

    public void setLiquibaseTablespace(String liquibaseTablespace) {
        this.liquibaseTablespace = liquibaseTablespace;
    }

    public String getDatabaseChangeLogTable() {
        return databaseChangeLogTable;
    }

    public void setDatabaseChangeLogTable(String databaseChangeLogTable) {
        this.databaseChangeLogTable = databaseChangeLogTable;
    }

    public String getDatabaseChangeLogLockTable() {
        return databaseChangeLogLockTable;
    }

    public void setDatabaseChangeLogLockTable(String databaseChangeLogLockTable) {
        this.databaseChangeLogLockTable = databaseChangeLogLockTable;
    }

    public boolean isDropFirst() {
        return dropFirst;
    }

    public void setDropFirst(boolean dropFirst) {
        this.dropFirst = dropFirst;
    }

    public boolean isShouldRun() {
        return shouldRun;
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    public File getRollbackFile() {
        return rollbackFile;
    }

    public void setRollbackFile(File rollbackFile) {
        this.rollbackFile = rollbackFile;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }
}
