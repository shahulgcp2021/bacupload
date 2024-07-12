package com.bacuti.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.Objects.isNull;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bacuti.service.dto.ErrorDetailDTO;

public class FileValidationUtil {

    private static final Logger log = LoggerFactory.getLogger(FileValidationUtil.class);

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    /**
     * Method used to validate a String Cell
     *
     * @param cell    Sheet cells
     * @param length  length of a string
     * @param mandate mandatory or not
     * @return Optional<String>
     */
    public static Optional<String> validateString(Cell cell, int length, Boolean mandate) {
        cell = checkBlankCell(cell);
        if (mandate && Objects.isNull(cell))
            return Optional.of("Should not be null or empty");
        if (!(cell == null || cell.getCellType() == CellType.BLANK)) {
            if (cell.getCellType() != CellType.STRING)
                return Optional.of("Provide a valid String");
            if (cell.getStringCellValue().trim().length() >= length)
                return Optional.of("Should not be greater than " + length + " length");
        }
        return Optional.empty();
    }


    /**
     * Method used to validate a Date Cell
     *
     * @param cell    Sheet cell
     * @param mandate mandatory or not
     * @return Optional<String>
     */

    public static Optional<String> validateDate(Cell cell, Boolean mandate) {
        cell = checkBlankCell(cell);
        if (mandate && Objects.isNull(cell)) {
            return Optional.of("Date should not be null or empty");
        }
        String cellValue = cell.getCellType().equals(CellType.STRING)
            ? cell.getStringCellValue()
            : Double.toString(cell.getNumericCellValue());
        try {
            if (Objects.nonNull(cell)) {
                DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd/MMM/yyyy")
                    .toFormatter(Locale.ENGLISH);
                LocalDate.parse(cellValue, inputFormatter);
            }
            return Optional.empty(); // Date is valid
        } catch (Exception e) {
            return Optional.of("Not a valid Date Format, expected format is DD/MMM/YYYY");
        }
    }

    /**
     * Method used to validate a Numeric Decimal Cell
     *
     * @param cell             Sheet Cell
     * @param min              min range
     * @param max              max range
     * @param mandate          mandatory
     * @param decimalPrecision decimal value
     * @return Optional<String>
     */
    public static Optional<String> validateDouble(Cell cell, double min, double max, Boolean mandate, int decimalPrecision) {
        cell = checkBlankCell(cell);
        if (mandate && Objects.isNull(cell)) {
            return Optional.of("Expected non-null decimal value");
        }
        if (!(cell == null || cell.getCellType() == CellType.BLANK)) {
            if (cell.getCellType() != CellType.NUMERIC)
                return Optional.of("Provide a valid Number");
            Double numericValue = cell.getNumericCellValue();
            if (numericValue < min || numericValue > max) {
                return Optional.of("Range should be " + min + " to " + max);
            }
            String numericValueStr = String.valueOf(numericValue);
            int indexOfDecimal = numericValueStr.indexOf(".");
            if (indexOfDecimal >= 0) {
                int precision = numericValueStr.length() - indexOfDecimal - 1;
                if (precision > decimalPrecision) {
                    return Optional.of("Decimal precision should not exceed " + decimalPrecision + " places");
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Method used to validate a Numeric Integer Cell
     *
     * @param cell    Sheet cell
     * @param min     range min
     * @param max     range max
     * @param mandate mandatory
     * @return Optional<String>
     */
    public static Optional<String> validateInteger(Cell cell, int min, int max, Boolean mandate) {
        cell = checkBlankCell(cell);
        if (mandate && Objects.isNull(cell)) {
            return Optional.of("Expected non-null numeric value for integer");
        }
        if (Objects.nonNull(cell)) {
            if (cell.getCellType() != CellType.NUMERIC)
                return Optional.of("Provide a valid Number");
            double numericValue = cell.getNumericCellValue();
            int intValue = (int) numericValue;
            if (numericValue % 1 != 0 && (intValue <= min || intValue >= max)) {
                return Optional.of("Value is not an integer");
            }
            if (intValue < min || intValue > max) {
                return Optional.of("Range should be " + min + " to " + max);
            }
        }
        return Optional.empty();
    }

    /**
     * Method used to validate enum Cell
     *
     * @param cell      Sheet cell
     * @param enumClass enum type instance
     * @param mandate   mandatory
     * @param <E>
     * @return Optional<String>
     */
    public static <E extends Enum<E>> Optional<String> validateEnum(Cell cell, Class<E> enumClass, Boolean mandate) {
        cell = checkBlankCell(cell);
        if (mandate && Objects.isNull(cell)) {
            return Optional.of("Enum value is required and should not be empty");
        }
        if (!(cell == null || cell.getCellType() == CellType.BLANK)) {
            try {
                if (cell.getCellType() != CellType.STRING)
                    return Optional.of("Provide a valid String");
                Enum.valueOf(enumClass, cell.getStringCellValue().trim());
            } catch (IllegalArgumentException e) {
                return Optional.of("Invalid value for enum " + enumClass.getSimpleName() + ": " + cell.getStringCellValue().trim());
            }
        }
        return Optional.empty();
    }

    /**
     * Method used to validate a Boolean Cell
     *
     * @param cell    Sheet cell
     * @param mandate mandatory
     * @return Optional<String>
     */
    public static Optional<String> validateBoolean(Cell cell, Boolean mandate) {
        cell = checkBlankCell(cell);
        if (mandate && Objects.nonNull(cell)) {
            switch (cell.getCellType()) {
                case BOOLEAN:
                    return Optional.empty();
                case NUMERIC:
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == 1 || numericValue == 0) {
                        return Optional.empty();
                    }
                    break;
                default:
                    break;
            }
            return Optional.of("Boolean value is required and should be of boolean type, or 1 for true and 0 for false");
        }
        if (!(cell == null || cell.getCellType() == CellType.BLANK) && (cell.getCellType() != CellType.NUMERIC)&& (cell.getCellType() != CellType.BOOLEAN)) {
            return Optional.of("value should be of boolean type");
        }

        return Optional.empty();
    }

    public static Optional<String> validateInstantDate(Cell cell, Boolean mandate) {
        if (mandate && Objects.isNull(cell)) {
            return Optional.of("Date should not be null or empty");
        }
        try {
            Instant instant = Instant.parse(cell.getStringCellValue());
            return Optional.empty(); // Date is valid
        } catch (Exception e) {
            return Optional.of("Not a valid Date Format, expected format is valid Instant date");
        }
    }

    public static boolean convertNumericToBoolean(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            throw new IllegalArgumentException("Cell should be numeric.");
        }

        // Read numeric value from cell
        double numericValue = cell.getNumericCellValue();

        // Convert numeric value to boolean
        return numericValue != 0; // Returns true for non-zero (1) and false for zero (0)
    }

    /**
     * Method used to construct ErrorDetailDTO
     *
     * @param message message
     * @param row     row number
     * @param column  column number
     * @return ErrorDetailDTO
     */
    public static ErrorDetailDTO generateErrorDTO(String message, int row, int column) {
        return new ErrorDetailDTO(row, column, message);
    }

    private static Cell checkBlankCell(Cell cell) {
        if (Objects.nonNull(cell) && cell.getCellType() == CellType.BLANK) {
            return null;
        }
        return cell;
    }

    public static boolean isEmptyRow(Row row, Integer columns) {
        boolean isEmpty = true;
        for (int rowIndex = 0; rowIndex < columns; rowIndex++) {
            if (!isNull(FileValidationUtil.checkBlankCell(row.getCell(rowIndex)))) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    /**
     * Method used to validate a Email.
     *
     * @param cell    Sheet cells
     * @param length  length of a string
     * @param mandate mandatory or not
     * @return Optional<String>
     */
    public static Optional<String> validateEmail(Cell cell, int length, Boolean mandate) {
        cell = checkBlankCell(cell);
        if (mandate && isNull(cell))
            return Optional.of("Should not be null or empty");
        if (!(isNull(cell) || cell.getCellType() == CellType.BLANK)) {
            if (cell.getCellType() != CellType.STRING)
                return Optional.of("Provide a valid String");
            if (cell.getStringCellValue().trim().length() >= length)
                return Optional.of("Should not be greater than " + length + " length");
            if (!isValidEmail(cell.getStringCellValue()))
                return Optional.of("Enter a valid email");
        }
        return Optional.empty();
    }

    /**
     * Validates if the given email is valid as per the defined regex pattern.
     *
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Boolean getBooleanValue(Cell cell) {
        boolean valueToSet = false;
        if (cell.getCellType() == CellType.BOOLEAN) {
            valueToSet = cell.getBooleanCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            double numericValue = cell.getNumericCellValue();
            if (numericValue == 1) {
                valueToSet = true;
            }
        }
        return valueToSet;
    }
}
