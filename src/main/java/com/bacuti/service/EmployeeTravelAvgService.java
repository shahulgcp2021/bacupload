package com.bacuti.service;

import com.bacuti.service.dto.EmployeeTravelAvgDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeTravelAvgService extends UploadService{

    /**
     * Method used to create or update an EmployeeTravelAvg.
     *
     * @param employeeTravelAvgDTO - EmployeeTravelAvg object to create or update.
     * @return - Returns created or updated EmployeeTravelAvg object.
     */
    EmployeeTravelAvgDTO saveEmployeeTravelAvg(EmployeeTravelAvgDTO employeeTravelAvgDTO);

    /**
     * Delete the EmployeeTravelAvg by id.
     *
     * @param id - EmployeeTravelAvg id.
     */
    void deleteEmployeeTravelAvgById(Long id);

    /**
     * Fetch list of EmployeeTravelAvgs based on the given conditions.
     *
     * @param pageable - Pageable.
     * @return - Returns Page of EmployeeTravelAvg objects.
     */
    Page<EmployeeTravelAvgDTO> getAllEmployeeTravelAvgs(Pageable pageable,String travelType, String siteName);
}
