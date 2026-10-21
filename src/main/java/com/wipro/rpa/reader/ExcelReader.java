package com.wipro.rpa.reader;

import com.wipro.rpa.model.SalesRecord;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the offline "SalesData.xls" sheet using the JXL API.
 * Sheet layout (row 0 = header):
 *   Col 0: S.No (S1, S2, S3 ...)
 *   Col 1: Date (dd-MM-yyyy)
 *   Col 2: Product Sold
 */
public class ExcelReader {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public List<SalesRecord> readAll(String filePath) {
        List<SalesRecord> records = new ArrayList<>();

        try {
            Workbook workbook = Workbook.getWorkbook(new File(filePath));
            Sheet sheet = workbook.getSheet(0);

            int totalRows = sheet.getRows();
            for (int row = 1; row < totalRows; row++) {   // skip header row 0
                String sNo = sheet.getCell(0, row).getContents().trim();
                String dateStr = sheet.getCell(1, row).getContents().trim();
                String product = sheet.getCell(2, row).getContents().trim();

                if (sNo.isEmpty() || dateStr.isEmpty() || product.isEmpty()) {
                    continue; // skip incomplete / not-yet-updated rows
                }

                Date sqlDate = new Date(sdf.parse(dateStr).getTime());
                records.add(new SalesRecord(sNo, sqlDate, product));
            }

            workbook.close();

        } catch (BiffException | java.io.IOException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return records;
    }
}
