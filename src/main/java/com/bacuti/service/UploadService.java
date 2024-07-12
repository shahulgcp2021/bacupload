package com.bacuti.service;

import com.bacuti.common.utils.CommonUtil;
import com.bacuti.domain.Company;
import com.bacuti.service.dto.CompanyDTO;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface UploadService {

    /**
     * Fetch list of items from sheet and save
     *
     * @param sheet
     * @return
     */
    public List<ErrorDetailDTO> validateAndSave(XSSFSheet sheet);

    default CompanyDTO getLoggedInCompany(CompanyService companyService) {
        LinkedTreeMap<String, String> userMetaData = CommonUtil.getLoggedInUserMeta();
        return companyService.findByName(userMetaData.get("companyName"));
    }
}
