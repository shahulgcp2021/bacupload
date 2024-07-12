package com.bacuti.service;

import com.bacuti.service.dto.AggregateEnergyUsageDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AggregateEnergyUsageService extends UploadService {

    /**
     * Method used to create or update a AggregateEnergyUsage.
     * @param aggregateEnergyUsageDTO - AggregateEnergyUsage object to create or update.
     * @return - Returns created or updated AggregateEnergyUsage object.
     */
    AggregateEnergyUsageDTO saveAggregateEnergyUsage(AggregateEnergyUsageDTO aggregateEnergyUsageDTO);

    /**
     * Delete the AggregateEnergyUsage by id.
     * @param id aggregateEnergyUsage id.
     */
    void deleteEnergyUsageById(Long id);

    /**
     * Fetch list of AggregateEnergyUsages based on the given conditions.
     * @param pageNo - Page number.
     * @param pageSize - Page size.
     * @param sortBy - Sort by field.
     * @param sortDirection - Sort direction.
     * @return - Returns Page of AggregateEnergyUsage objects.
     */
    Page<AggregateEnergyUsageDTO> getAllEnergyUsage(int pageNo, int pageSize, String sortBy, String sortDirection, String search);

    /**
     * Fetch list of items from sheet and save
     * @param sheet
     * @return
     */
    List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet);
}
