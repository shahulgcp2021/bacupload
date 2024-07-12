package com.bacuti.service.impl;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.bacuti.service.S3Service;
import com.bacuti.service.dto.ErrorDetailDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class S3ServiceImpl implements S3Service {


    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.path.in}")
    private String inPath;

    @Value("${aws.s3.path.out}")
    private String outPath;

    @Value("${aws.s3.path.error}")
    private String errorPath;

    private final Logger log = LoggerFactory.getLogger(com.bacuti.service.impl.S3ServiceImpl.class);

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void uploadFile(String fileName, InputStream fileInputStream, long contentLength, String contentType) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);

        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, fileInputStream, metadata);
        amazonS3.putObject(request);
    }


    public XSSFSheet readExcelSheetFromS3(String key) throws IOException {
        // Download Excel file from S3
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, key));
        InputStream inputStream = object.getObjectContent();

        // Load Excel workbook and iterate through sheets and rows
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet worksheet = workbook.getSheetAt(0);

        workbook.close();
        inputStream.close();

        return worksheet;
    }

    public void updateExcelWithErrorDetails(String fileName, List<ErrorDetailDTO> errorDetails) throws IOException {
       // XSSFSheet worksheet = workbook.getSheetAt(0);
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, inPath+fileName));
        InputStream inputStream = object.getObjectContent();

        // Load Excel workbook and iterate through sheets and rows
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet worksheet = workbook.getSheetAt(0);

        // Add a new column header for error details
        Row headerRow = worksheet.getRow(0);
        Cell errorDetailHeaderCell = headerRow.createCell(headerRow.getLastCellNum());
        errorDetailHeaderCell.setCellValue("Error Details");
        Map<Integer, List<ErrorDetailDTO>> errorDetailMap = errorDetails.stream()
            .collect(Collectors.groupingBy(ErrorDetailDTO::getRowNo));


        for (Map.Entry<Integer, List<ErrorDetailDTO>> entry : errorDetailMap.entrySet()) {


            Row row = worksheet.getRow(entry.getKey());
            Cell errorDetailCell = row.createCell(row.getLastCellNum());
            errorDetailCell.setCellValue(entry.getValue().toString());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
            byte[] bytes = bos.toByteArray();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            PutObjectRequest request = new PutObjectRequest(bucketName, errorPath+fileName,
                new ByteArrayInputStream(bytes) ,metadata);
            amazonS3.putObject(request);
        } finally {
            workbook.close();
            inputStream.close();

           bos.close();
        }
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, inPath+fileName));

    }

    @Override
    public S3ObjectInputStream getWorkBookFromS3(String key) throws IOException {
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, key));
        return object.getObjectContent();
    }

    public void moveFile(String  sourceKey,String destinationKey){

            // Copy the object within the same bucket
            CopyObjectRequest copyRequest = new CopyObjectRequest(
                bucketName, sourceKey, bucketName, destinationKey);
            amazonS3.copyObject(copyRequest);

            // Delete the original object
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, sourceKey));

        log.info("File moved successfully from " + sourceKey + " to " + destinationKey);

    }
}



