package com.bacuti.service;


import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.PurchasedQtyDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchasedQtyService  extends UploadService {

    /**
     * Method used to create or update a PurchasedQty.
     *
     * @param purchasedQtyDTO - PurchasedQty object to create or update.
     * @return - Returns created or updated PurchasedQty object.
     */
    PurchasedQtyDTO savePurchasedQty(PurchasedQtyDTO purchasedQtyDTO);

    /**
     * Delete the PurchasedQty by id.
     *
     * @param id - PurchasedQty id.
     */
    void deletePurchasedQtyById(Long id);

    /**
     * Fetch list of PurchasedQtys based on the given conditions.
     *
     * @param pageable - Pageable.
     * @return - Returns Page of PurchasedQty objects.
     */
    Page<PurchasedQtyDTO> getAllPurchasedQties(Pageable pageable);


    List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet);
}
