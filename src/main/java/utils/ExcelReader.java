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
