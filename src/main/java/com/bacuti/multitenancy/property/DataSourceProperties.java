package com.bacuti.multitenancy.property;

import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring")
public class DataSourceProperties {

    private DataSources dataSources;

    public DataSources getDataSources() {
        return dataSources;
    }

    public void setDataSources(DataSources dataSources) {
        this.dataSources = dataSources;
    }

    public static class DataSources {
        private List<String> tenants;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String paramtenantkey;
        private LiquibaseProperties liquibase;

        public List<String> getTenants() {
            return tenants;
        }

        public void setTenants(List<String> tenants) {
            this.tenants = tenants;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public LiquibaseProperties getLiquibase() {
            return liquibase;
        }

        public void setLiquibase(LiquibaseProperties liquibase) {
            this.liquibase = liquibase;
        }

        public String getParamtenantkey() {
            return paramtenantkey;
        }

        public void setParamtenantkey(String paramtenantkey) {
            this.paramtenantkey = paramtenantkey;
        }
    }
}
