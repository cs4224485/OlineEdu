package com.online.edu.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;

public class ExcelImportUtil {

    private InputStream inputStream;
    private Workbook workbook;
    public ExcelImportUtil(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.workbook =  new HSSFWorkbook(this.inputStream);
    }

    public Sheet getSheet(int number){
        return this.workbook.getSheetAt(number);
    }
    public String getCellValue(Cell cell){
        return cell.getStringCellValue();
    }
}
