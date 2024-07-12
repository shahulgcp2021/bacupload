package com.bacuti.web.rest;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.bacuti.common.enums.FileProcessStatus;
import com.bacuti.common.errors.BusinessException;
import com.bacuti.common.utils.ExcelUtil;
import com.bacuti.multitenancy.tenant.TenantContext;
import com.bacuti.service.FileUploadStatusService;
import com.bacuti.service.S3Service;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.FileUploadStatusDTO;
import com.bacuti.service.dto.ItemDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for Upload service.
 */
@RestController
@RequestMapping("/api/${app.api.version}/upload")
@Transactional
public class UploadResource {

    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private FileUploadStatusService fileUploadStatusService;

    @Value("${aws.s3.path.error}")
    private String errorPath;

    private static final String ENTITY_NAME = "uploadServiceFileUploadStatus";


    /**
     * {@code POST  } : process upload.
     *
     * @param service serviceName
     * @param file
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with File upload DTO, or with status {@code 400 (Bad Request)} if the billofMaterial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws URISyntaxException
     */
    @PostMapping
    public ResponseEntity<FileUploadStatusDTO> uploadFile(@RequestParam String service, @RequestParam String fileName, @RequestBody MultipartFile file) throws URISyntaxException {
        if (ExcelUtil.isExcelFileEmpty(file))
            throw new BusinessException("Invalid File", HttpStatus.BAD_REQUEST.value());
        FileUploadStatusDTO fileUploadStatusDTO = fileUploadStatusService.saveFileUploadStatus(fileName, FileProcessStatus.NEW.name(), "");
        String currentTenant = TenantContext.getCurrentTenant();
        log.debug("REST request to save file upload status for tenant: {}", currentTenant);
        fileUploadStatusService.processFileUpload(service, file, fileName, fileUploadStatusDTO.getId(), currentTenant);
        return ResponseEntity.created(new URI("/api/upload/:id" + fileUploadStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fileUploadStatusDTO.getId().toString())).body(fileUploadStatusDTO);

    }

    /**
     * {@code GET  /upload} : get the "fileName" FileUploadStatus.
     *
     * @param fileName the fileName of the fileUploadStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FileUploadStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping
    public ResponseEntity<FileUploadStatusDTO> getFileUploadStatusByName(@RequestParam("fileName") String fileName) {
        log.debug("REST request to get fileUploadStatus : {}", fileName);
        FileUploadStatusDTO fileUploadStatusDTO = fileUploadStatusService.getFileUploadStatusByName(fileName);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileUploadStatusDTO));
    }

    @GetMapping("/error-file")
    public ResponseEntity<Resource> getErrorFile(@RequestParam("fileName") String fileName) throws IOException {
        log.debug("REST request to get ErrorFile : {}", fileName);

        try {
            S3ObjectInputStream inputStream = s3Service.getWorkBookFromS3(errorPath + fileName);
            // Set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", fileName);

            // Return the Excel file as ResponseEntity
            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        }
        catch (Exception e) {
            log.error("Error File Processing exception {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * {@code GET  /upload/errors} : get the "fileName" FileUploadStatus.
     *
     * @param fileName the fileName of the fileUploadStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body List<ErrorDetailDTO>, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/errors")
    public ResponseEntity<List<ErrorDetailDTO>> getErrorDetails(@RequestParam("fileName") String fileName) throws IOException {
        log.debug("REST request to get ErrorFile : {}", fileName);

        try {
            XSSFSheet sheet = s3Service.readExcelSheetFromS3(errorPath + fileName);

            return ResponseUtil.wrapOrNotFound(Optional.of(ExcelUtil.parseErrors(sheet)));
        }
        catch (Exception e) {
            log.error("Error File Processing exception {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}



