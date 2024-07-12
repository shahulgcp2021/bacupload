package com.bacuti.service.impl;

import com.bacuti.common.enums.FileProcessStatus;
import com.bacuti.common.errors.BusinessException;
import com.bacuti.domain.FileUploadStatus;
import com.bacuti.multitenancy.tenant.TenantContext;
import com.bacuti.repository.FileUploadStatusRepository;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.FileUploadStatusService;
import com.bacuti.service.S3Service;
import com.bacuti.service.UploadService;
import com.bacuti.service.dto.ErrorDetailDTO;
import com.bacuti.service.dto.FileUploadStatusDTO;
import com.bacuti.service.mapper.FileUploadStatusMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileUploadStatusServiceImpl implements FileUploadStatusService {
    private final Logger log = LoggerFactory.getLogger(com.bacuti.service.impl.FileUploadStatusServiceImpl.class);

    @Autowired
    private FileUploadStatusRepository fileUploadStatusRepository;

    @Autowired
    S3Service s3Service;

    @Autowired
    private FileUploadStatusMapper fileUploadStatusMapper;
    @Autowired
    ApplicationContext applicationContext;
    @Value("${aws.s3.path.in}")
    private String inPath;

    @Value("${aws.s3.path.out}")
    private String outPath;

    @Value("${aws.s3.path.error}")
    private String errorPath;

    @Transactional
    public FileUploadStatusDTO saveFileUploadStatus(String fileName, String status, String message) {
        FileUploadStatus fileUpload = new FileUploadStatus();
        fileUpload.setFileName(fileName);
        fileUpload.setStatus(status);
        fileUpload.setMessage(message);
        fileUploadStatusMapper.updateAuditColumns(fileUpload);
        return fileUploadStatusMapper.fileUploadStatusToFileUploadStatusDTO(fileUploadStatusRepository.save(fileUpload));
    }

    public FileUploadStatusDTO getFileUploadStatusByName(String fileName) {
        FileUploadStatus dbBOM = fileUploadStatusRepository.findFirstByFileNameOrderByLastModifiedDateDesc(fileName)
            .orElseThrow(() -> new BusinessException("Invalid Name", HttpStatus.BAD_REQUEST.value()));
        return fileUploadStatusMapper.fileUploadStatusToFileUploadStatusDTO(dbBOM);
    }


    @Async("taskpoolExecutor")
    @Override
    public void processFileUpload(String service, MultipartFile file, String fileName, Long id, String currentTenant) {
        byte[] fileContent = null;
        try {
            fileContent = file.getBytes(); // Read the file content into a byte array
        } catch (Exception e) {
            updateFileUploadStatus(id, FileProcessStatus.ERROR.name(), e.getMessage());
            log.error("Error reading file content: ", e);
            return;
        }
        // 1. S3 File Upload
        try {
            TenantContext.setCurrentTenant(currentTenant);
            String tenant = TenantContext.getCurrentTenant();
            log.debug("REST request to save file upload status for tenant: {}", tenant);
            s3Service.uploadFile(inPath + fileName, new ByteArrayInputStream(fileContent), file.getSize(), file.getContentType());
            log.info("File uploaded successfully in S3: {} ", fileName);
        } catch (Exception ex) {
            updateFileUploadStatus(id, FileProcessStatus.ERROR.name(), ex.getMessage());
            log.error("File upload exception: " + ex);
        }

        // 2. S3 File download  and extract Excel Sheet
        XSSFSheet sheet = null;
        try {
            sheet = s3Service.readExcelSheetFromS3(inPath + fileName);
            updateFileUploadStatus(id, FileProcessStatus.IN_PROCESS.name(), "");

        } catch (Exception e) {
            updateFileUploadStatus(id, FileProcessStatus.ERROR.name(), e.getMessage());

            log.error("File download exception: " + e);

        }
        // 3. Call specific business service for validation and save
        UploadService uploadservice = applicationContext.getBean(service, UploadService.class);

        List<ErrorDetailDTO> errorDetailDTOS = null;
        try {
            errorDetailDTOS = uploadservice.validateAndSave(sheet);
        }
        catch(Exception e){
            updateFileUploadStatus(id, FileProcessStatus.ERROR.name(), e.getMessage());
        }

        if (CollectionUtils.isNotEmpty(errorDetailDTOS)) {
            try {
                log.info("uploaded File  has processing Errors in {} rows", errorDetailDTOS.size());
                updateFileUploadStatus(id, FileProcessStatus.ERROR.name(), "");
                s3Service.updateExcelWithErrorDetails(fileName, errorDetailDTOS);
            } catch (Exception e) {
                log.error("File upload exception: " + e);
            }
        } else {

            try {
                updateFileUploadStatus(id, FileProcessStatus.COMPLETED.name(), "");
                s3Service.moveFile(inPath + fileName, outPath + fileName);
                log.info("uploaded File has been processed with no Errors ");
            } catch (Exception e) {
                log.error("File upload exception: " + e);

            }
        }

    }

    @Transactional
    public void updateFileUploadStatus(Long id, String newStatus, String newMessage) {
        Optional<FileUploadStatus> optionalStatus = fileUploadStatusRepository.findById(id);
        if (optionalStatus.isPresent()) {
            FileUploadStatus fileUploadStatus = optionalStatus.get();
            fileUploadStatus.setStatus(newStatus);
            fileUploadStatus.setMessage(newMessage);
            fileUploadStatusMapper.updateAuditColumns(fileUploadStatus);
            FileUploadStatusDTO fileUploadStatusDTO = fileUploadStatusMapper.fileUploadStatusToFileUploadStatusDTO(fileUploadStatusRepository.save(fileUploadStatus));
            log.info("FileUploadStatus : {}", fileUploadStatusDTO.toString());
        } else {
            throw new BusinessException("File upload status with id " + id + " not found", HttpStatus.BAD_REQUEST.value());

        }
    }
}
