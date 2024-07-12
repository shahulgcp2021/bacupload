package com.bacuti.service;

import com.bacuti.service.dto.EnergySourceDTO;
import org.springframework.data.domain.Page;

public interface EnergySourceService extends UploadService {

    /**
     * Method used to create the Energy Source entity.
     * @param energySourceDTO - Energy Source dto from input params
     * @return - Created Energy Source DTO.
     */
    EnergySourceDTO createEnergySource(EnergySourceDTO energySourceDTO);

    /**
     * Method used to delete the Energy source entity by id.
     * @param id - Input energy source id.
     */
    void deleteById(Long id);

    /**
     * Method used to fetch the energy source details.
     *
     * @param pageNo - Page number to fetch data.
     * @param pageSize - Page size of screen
     * @param sortBy - Data sorting column
     * @param sortDirection - Data sort direction
     * @param energyType - Energy type column.
     * @return Returns Energy Source dto
     */
    Page<EnergySourceDTO> getEnergySource(int pageNo, int pageSize, String sortBy, String sortDirection, String energyType);

    /**
     * {@code GET  /energy-sources/:id} : get the "id" energySource.
     *
     * @return Returns Item supplier dto
     */
    Page<EnergySourceDTO> getEnergySourceTypes();
}
