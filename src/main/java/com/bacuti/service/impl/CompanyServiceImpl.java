package com.bacuti.service.impl;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.config.AwsParameterStoreService;
import com.bacuti.domain.Company;
import com.bacuti.multitenancy.bean.MultiTenantDataSourceSpringLiquibase;
import com.bacuti.multitenancy.property.DataSourceProperties;
import com.bacuti.multitenancy.tenant.TenantContext;
import com.bacuti.repository.CompanyRepository;
import com.bacuti.service.dto.CompanyDTO;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.exception.LiquibaseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Service;
import com.bacuti.service.mapper.CompanyMapper;
import com.bacuti.service.CompanyService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final DataSourceProperties dataSourceProperties;

    @Qualifier("dataSources")
    private final Map<Object, Object> dataSources;

    @Qualifier("tenantRoutingDataSource")
    private final AbstractRoutingDataSource abstractRoutingDataSource;

    private final MultiTenantDataSourceSpringLiquibase multiTenantDataSourceSpringLiquibase;

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final AwsParameterStoreService awsParameterStoreService;
    private final DataSource dataSource;

    @Autowired
    public CompanyServiceImpl(
        CompanyRepository companyRepository,
        CompanyMapper companyMapper,
        AwsParameterStoreService awsParameterStoreService,
        DataSource dataSource,
        DataSourceProperties dataSourceProperties,
        @Qualifier("dataSources") Map<Object, Object> dataSources,
        @Qualifier("tenantRoutingDataSource") AbstractRoutingDataSource abstractRoutingDataSource,
        MultiTenantDataSourceSpringLiquibase multiTenantDataSourceSpringLiquibase
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.awsParameterStoreService = awsParameterStoreService;
        this.dataSource = dataSource;
        this.dataSourceProperties = dataSourceProperties;
        this.dataSources = dataSources;
        this.abstractRoutingDataSource = abstractRoutingDataSource;
        this.multiTenantDataSourceSpringLiquibase = multiTenantDataSourceSpringLiquibase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompanyDTO findByName(String companyName) {
        Company company = companyRepository.findByName(companyName);
        if (company == null) {
            throw new BusinessException("Company Not found " + companyName, HttpStatus.BAD_REQUEST.value());
        }
        return companyMapper.companyToCompanyDTO(company);
    }

    @Override
    public CompanyDTO saveCompany(CompanyDTO companyDTO) {
        if (companyDTO.getId() != null) {
            // Check if the Company exists when id is provided
            companyDTO.setName(
                companyRepository.findById(companyDTO.getId())
                    .orElseThrow(() -> new BusinessException("Company Not found", HttpStatus.BAD_REQUEST.value())).getName()
            );
        }
        if (companyRepository.existsByName(companyDTO.getName()) && ((companyDTO.getId() == null) || !Objects.equals(companyRepository.getIdByName(companyDTO.getName()), companyDTO.getId()))) {
            // Check if Company name already exists when creating and updating a Item
            throw new BusinessException("Company with the name '" + companyDTO.getName() + "' already exists.", HttpStatus.BAD_REQUEST.value());
        }

        if (companyRepository.existsByDomain(companyDTO.getDomain()) && ((companyDTO.getId() == null) || !Objects.equals(companyRepository.getIdByName(companyDTO.getName()), companyDTO.getId()))) {
            throw new BusinessException("Company with the domain '" + companyDTO.getDomain() + "' already exists.", HttpStatus.BAD_REQUEST.value());
        }

        validateCompanyDTO(companyDTO);
        Company toBeSavedCompany = companyMapper.companyDTOToCompany(companyDTO);

        // Update audit columns
        companyMapper.updateAuditColumns(toBeSavedCompany);
        Company company = companyRepository.save(toBeSavedCompany);
        // Reset tenant context to default or previous tenant
        return companyMapper.companyToCompanyDTO(toBeSavedCompany);
    }

    private String createDBForCompany(String companyName) {
        // Sanitize the database name
        String dbName = sanitizeDatabaseName(companyName);
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

        dataSources.put(companyName, dataSource);
        TenantContext.setCurrentTenant(companyName);
        abstractRoutingDataSource.afterPropertiesSet();
        multiTenantDataSourceSpringLiquibase.runOnAllDataSources();
    }

    private String sanitizeDatabaseName(String companyName) {
        // Convert to lowercase, replace not allowed characters with underscore, and trim to 63 characters
        String sanitized = companyName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        return sanitized.length() > 63 ? sanitized.substring(0, 63) : sanitized;
    }

    private void validateCompanyDTO(CompanyDTO companyDTO) {
        if (StringUtils.isBlank(companyDTO.getName())) {
            throw new BusinessException("Company Name should not be Empty", HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    public void deleteCompanyById(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new BusinessException("Invalid Company Id", HttpStatus.BAD_REQUEST.value());
        }
        companyRepository.deleteById(id);
    }

    @Override
    public Page<CompanyDTO> getCompanies(int pageNo, int pageSize, String sortBy, String sortDirection, String companyName) {
        Page<Company> pageSite;
        if (StringUtils.isEmpty(companyName)) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            pageSite = companyRepository.findAll(pageable);
        } else {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            pageSite = companyRepository.findByNameContaining(companyName, pageable);
        }

        return pageSite.map(companyMapper::companyToCompanyDTO);
    }

    @Override
    public CompanyDTO findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new BusinessException("Company Not found", HttpStatus.BAD_REQUEST.value()));
        return companyMapper.companyToCompanyDTO(company);
    }
}
