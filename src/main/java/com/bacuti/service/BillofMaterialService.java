package com.bacuti.service;

import com.bacuti.service.dto.BillofMaterialDTO;
import org.springframework.data.domain.Page;

public interface BillofMaterialService extends UploadService {

    BillofMaterialDTO saveBillofMaterial(BillofMaterialDTO billofMaterial);

    BillofMaterialDTO findById(Long id);

    Page<BillofMaterialDTO> getBillofMaterials(int pageNo, int pageSize, String sortBy, String sortDirection, String productName);

    Boolean deleteById(Long id);

}
