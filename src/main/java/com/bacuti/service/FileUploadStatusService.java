package com.bacuti.service;

import com.bacuti.service.dto.FileUploadStatusDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUploadStatusService {

    FileUploadStatusDTO getFileUploadStatusByName(String fileName);

    void processFileUpload(String service, MultipartFile file, String fileName, Long id, String tenant);

    FileUploadStatusDTO saveFileUploadStatus(String fileName, String status, String message);


}
