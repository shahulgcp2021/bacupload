package com.bacuti.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.bacuti.service.dto.ErrorDetailDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface S3Service {

    // upload given file as objectName to S3 bucket
    void uploadFile(String fileName, InputStream fileInputStream, long contentLength, String contentType) throws IOException;


    XSSFSheet readExcelSheetFromS3(String key) throws IOException;

    void moveFile(String sourceKey, String destinationKey);

    void updateExcelWithErrorDetails(String fileName, List<ErrorDetailDTO> errorDetails) throws IOException;

    S3ObjectInputStream getWorkBookFromS3(String key) throws IOException;
}
