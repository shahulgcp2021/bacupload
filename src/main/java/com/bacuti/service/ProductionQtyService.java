package com.bacuti.service;

import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.ProductionQtyDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductionQtyService extends UploadService {

    /**
     * Method used to create or update a ProductionQty.
     *
     * @param productionQtyDTO - ProductionQty object to create or update.
     * @return - Returns created or updated ProductionQty object.
     */
    ProductionQtyDTO saveProductionQty(ProductionQtyDTO productionQtyDTO);

    /**
     * Delete the ProductionQty by id.
     *
     * @param id - ProductionQty id.
     */
    void deleteProductionQtyById(Long id);

    /**
     * Fetch list of ProductionQties based on the given conditions.
     *
     * @param pageable - Pageable.
     * @return - Returns Page of ProductionQty objects.
     */
    Page<ProductionQtyDTO> getAllProductionQties(Pageable pageable);

    List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet);
}
