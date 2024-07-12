package com.bacuti.service;

import com.bacuti.service.dto.MachineDTO;
import org.springframework.data.domain.Page;

public interface MachineService {

    /**
     * Method used to create the Machine entity.
     * @param machineDTO - Machine dto from input params
     * @return - Created Machine Entity.
     */
    MachineDTO createMachine(MachineDTO machineDTO);

    /**
     * Method used to delete the machine entity by id
     * @param id - Input machine entity id.
     */
    void deleteMachineById(Long id);

    /**
     * Method used to fetch the Machine entity details.
     *
     * @param pageNo - Page number to fetch data.
     * @param pageSize - Page size of screen
     * @param sortBy - Data sorting column
     * @param sortDirection - Data sort direction
     * @param machineName - Machine name column.
     * @return Returns Machine dto
     */
    Page<MachineDTO> getMachineDetails(int pageNo, int pageSize, String sortBy, String sortDirection, String machineName);
}
