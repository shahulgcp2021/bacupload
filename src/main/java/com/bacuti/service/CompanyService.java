package com.bacuti.service;

import com.bacuti.service.dto.CompanyDTO;
import org.springframework.data.domain.Page;

/**
 * @author Shahul.Buhari
 * @version v1
 */
public interface CompanyService {

    /**
     * Gets Company by name.
     *
     * @param companyName of the company.
     * @return companyDTO.
     */
    CompanyDTO findByName(String companyName);

    /**
     * Method used to create or update a Company.
     *
     * @param companyDTO - Company Object to create.
     * @return - Returns Created or updated company object.
     */
    CompanyDTO saveCompany(CompanyDTO companyDTO);

    /**
     * Delete the company by id.
     *
     * @param id - Company id.
     */
    void deleteCompanyById(Long id);

    /**
     * Retreives all company details based on the given conditions along with pagination
     * @param pageNo page number
     * @param pageSize size of the returned object
     * @param sortBy sort to be used for the object fetched
     * @param sortDirection sort direction to be used for the object fetched
     * @param companyName filter condition
     * @return - Returns list of companies
     */
    Page<CompanyDTO> getCompanies(int pageNo, int pageSize, String sortBy, String sortDirection, String companyName);


    /**
     * Retreives companydto based on id
     * @param id - Company id.
     * @return company returned based on request id
     */
    CompanyDTO findById(Long id);

}
