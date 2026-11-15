/*
package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {
    public static List<Map<String, String>> getData(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> excelData = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            int totalCols = headerRow.getLastCellNum();

            for (int i = 1; i < totalRows; i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;

                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int j = 0; j < totalCols; j++) {
                    String header = formatter.formatCellValue(headerRow.getCell(j));
                    String value = formatter.formatCellValue(currentRow.getCell(j));
                    rowMap.put(header, value);
                }
                excelData.add(rowMap);
            }
        }
        return excelData;
    }
}
*/
package utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    public static synchronized List<Map<String, String>> getData(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> excelData = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return excelData;
            }

            int totalRows = sheet.getLastRowNum();
            int totalCols = headerRow.getLastCellNum();

            for (int i = 1; i <= totalRows; i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) {
                    continue;
                }

                // Skip trailing blank rows that retain empty Excel cell formatting
                String firstCellCheck = formatter.formatCellValue(currentRow.getCell(0)).trim();
                if (firstCellCheck.isEmpty()) {
                    continue;
                }

                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int j = 0; j < totalCols; j++) {
                    String header = formatter.formatCellValue(headerRow.getCell(j)).trim();
                    String value = formatter.formatCellValue(currentRow.getCell(j)).trim();
                    rowMap.put(header, value);
                }
                excelData.add(rowMap);
            }
        }
        return excelData;
    }
}

