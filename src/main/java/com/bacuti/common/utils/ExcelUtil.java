package com.bacuti.common.utils;

import com.bacuti.service.dto.ErrorDetailDTO;
import com.google.gson.reflect.TypeToken;
import com.nimbusds.jose.shaded.gson.Gson;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    static Gson gson = new Gson();

    public static Workbook filterRows(Workbook workbook, int[] rowNumbers) {
        Workbook filteredWorkbook = new XSSFWorkbook();

        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Sheet filteredSheet = filteredWorkbook.createSheet(sheet.getSheetName());

            List<Row> rowsToCopy = new ArrayList<>();
            for (int rowNum : rowNumbers) {
                Row row = sheet.getRow(rowNum - 1); // -1 because POI uses 0-based index
                if (row != null) {
                    rowsToCopy.add(row);
                }
            }

            // Copy filtered rows to the new sheet
            for (int i = 0; i < rowsToCopy.size(); i++) {
                Row sourceRow = rowsToCopy.get(i);
                Row destRow = filteredSheet.createRow(i);
                for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
                    Cell sourceCell = sourceRow.getCell(j);
                    Cell destCell = destRow.createCell(j);
                    if (sourceCell != null) {
                        switch (sourceCell.getCellType()) {
                            case STRING:
                                destCell.setCellValue(sourceCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                destCell.setCellValue(sourceCell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                destCell.setCellValue(sourceCell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                destCell.setCellValue(sourceCell.getCellFormula());
                                break;
                            default:
                                destCell.setCellValue("");
                        }
                    }
                }
            }
        }

        return filteredWorkbook;
    }

    public static boolean isExcelFileEmpty(MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming you are interested in the first sheet
            return sheet.getPhysicalNumberOfRows() <= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Assume empty if an error occurs (handle this according to your requirements)
        }
    }

    public static List<ErrorDetailDTO> parseErrors(XSSFSheet sheet) {

        List<ErrorDetailDTO> errorItemDetails = new ArrayList<>();
        int lastRowNum = sheet.getLastRowNum();
        int lastColumnIndex = -1;

        // Find the last column index with data
        Row firstRow = sheet.getRow(1);
        if (firstRow != null) {
            int lastCellNum = firstRow.getLastCellNum();
            if (lastCellNum > lastColumnIndex) {
                lastColumnIndex = lastCellNum - 1;  // Convert to 0-based index
            }
        }

        // Read values from the last column
        if (lastColumnIndex >= 0) {
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(lastColumnIndex);
                    if (cell != null) {
                        String cellValue = cell.toString();  // Get cell value as String
                        if (!cellValue.isEmpty()) {
                            // Convert JSON to List of ErrorDetailDTO objects
                            List<ErrorDetailDTO> errorDetailDTOS = gson.fromJson(cellValue, new TypeToken<List<ErrorDetailDTO>>() {
                            }.getType());
                            errorItemDetails.addAll(errorDetailDTOS);
                        }
                    }
                }
            }
        }

        return errorItemDetails;
    }

}

