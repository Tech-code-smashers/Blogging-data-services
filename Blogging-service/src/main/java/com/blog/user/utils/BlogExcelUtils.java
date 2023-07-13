package com.blog.user.utils;

import com.blog.user.responses.responses.CommonExcelResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BlogExcelUtils {

    @Autowired
    private ApplicationContext сontext;

    public static ByteArrayInputStream getExcelTemplate(final String templateType, List<String> headers,
                                                 final String excelType){
        ByteArrayInputStream result = null;
        ByteArrayOutputStream out;
        Workbook workbook;
        Sheet sheet;

        try{
            if(templateType!=null && headers!=null){
                int totalRow = 0;
                if(excelType.equalsIgnoreCase(CommonUtils.WB_TYPE_XSSF)){
                    workbook = new XSSFWorkbook();
                }else{
                    workbook = new HSSFWorkbook();
                }
                out = new ByteArrayOutputStream();
                sheet =workbook.createSheet(String.valueOf(BlogUtils.getHashCode(BlogUtils.createSafeSheetName(templateType))));
                /*
                      Styling the Header column with colour
                 */
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setColor(IndexedColors.RED.getIndex());
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerFont.setFontName("Arial");
                headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
                headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerFont.setBold(true);
                headerCellStyle.setFont(headerFont);
                Row headerRow =sheet.createRow(totalRow++);
                AtomicInteger count= new AtomicInteger();
                headers.stream().forEach(key->{
                    Cell cell = headerRow.createCell(count.get());
                    if(key instanceof  String) {
                        cell.setCellValue(key.toString());
                    }
                    cell.setCellStyle(headerCellStyle);
                    count.getAndIncrement();
                });
                workbook.write(out);
                    result = new ByteArrayInputStream(out.toByteArray());   
            }
        }catch (Exception e){
        }
        return result;
    }
    public Workbook submitExcelToProcess(final InputStream fileData, final String SHEET_NAME, final String FILE_NAME,
                                         Class<? extends ExcelRowProcessor> class1) throws Exception {
        Workbook workbook = null;
        CountDownLatch latch = null;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Sheet sheet;
        if (fileData != null) {
            if (FILE_NAME.toUpperCase().endsWith(CommonUtils.WB_TYPE_XSSF))
                workbook = new XSSFWorkbook(fileData);
            else
                workbook = new HSSFWorkbook(fileData);
            if (workbook != null) {
                sheet = workbook
                        .getSheet(String.valueOf(BlogUtils.getHashCode(BlogUtils.createSafeSheetName(SHEET_NAME))));
                if (sheet != null) {
                    int noOfRows = sheet.getLastRowNum();
                    Row headerRow = sheet.getRow(0);
                    int rowIndex = sheet.getPhysicalNumberOfRows() - 1;
                    latch = new CountDownLatch(rowIndex);
                    Map<String, CellStyle> columns = new LinkedHashMap<>();
                    int i = 0;
                    if (headerRow != null && headerRow.getLastCellNum() > 0) {
                        for (i = 0; i < headerRow.getLastCellNum(); i++) {
                            Cell headerCell = headerRow.getCell(i);
                            columns.put(headerCell.getStringCellValue(), createDescriptionStyleFont(workbook));
                        }
                    }
                    int cellid = i;
                    Row row1 = sheet.getRow(0);
                    Cell cell0 = row1.createCell(cellid);
                    cell0.setCellValue("status");
                    cell0.setCellStyle(createDescriptionStyleFont(workbook));
                    cellid = cellid + 1;
                    Cell cell1 = row1.createCell(cellid);
                    cell1.setCellValue("code");
                    cell1.setCellStyle(createDescriptionStyleFont(workbook));
                    cellid = cellid + 1;
                    Cell cell2 = row1.createCell(cellid);
                    cell2.setCellValue("description");
                    cell2.setCellStyle(createDescriptionStyleFont(workbook));
//                    try {
//                        con = JDBCMSqlConnectionFactory.getConnection(databaseURL, databaseUserName, databasePassword);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e.getMessage(), e);
//                    }
                    for (int k = 1; k <= rowIndex; k++) {
                        Row row = sheet.getRow(k);
                        if (row != null) {
                            Constructor<?> cons = class1.getConstructor();
                            ExcelRowProcessor newInstancePCObj = (ExcelRowProcessor) cons.newInstance();
                            newInstancePCObj.initTask(row, latch, workbook, сontext);
                            executorService.submit((Callable<T>) newInstancePCObj);
                        }
                    }
                }
                latch.await();
            }
        }
        return workbook;
    }

    public void excelPrepareResponse(Row row1, CommonExcelResponse uploadResponse){
        int cellid = row1.getLastCellNum();
        if (cellid != -1) {
            Cell cell = row1.createCell(cellid);
            if (uploadResponse.getCode() != "M200") {
                cell.setCellValue("FAIL");
            } else {
                cell.setCellValue("SUCCESS");
            }
            cellid = cellid + 1;
            Cell cell1 = row1.createCell(cellid);
            cell1.setCellValue(uploadResponse.getCode());
            cellid = cellid + 1;
            Cell cell2 = row1.createCell(cellid);
            cell2.setCellValue(uploadResponse.getDescription());
        }

    }


    public CellStyle createFailureWarningFont(Workbook writeWorkbook) {
        org.apache.poi.ss.usermodel.Font failureFont = writeWorkbook.createFont();
        failureFont.setBold(true);
        failureFont.setColor(IndexedColors.RED.getIndex());
        CellStyle failureHeaderStyle = writeWorkbook.createCellStyle();
        failureFont.setFontName("Arial");
        failureHeaderStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        failureHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        failureFont.setBold(true);
        failureHeaderStyle.setFont(failureFont);
        return failureHeaderStyle;
    }

    public CellStyle createSuccessFont(Workbook writeWorkbook) {
        org.apache.poi.ss.usermodel.Font successFont = writeWorkbook.createFont();
        successFont.setBold(true);
        successFont.setColor(IndexedColors.GREEN.getIndex());
        CellStyle successCellStyle = writeWorkbook.createCellStyle();
        successFont.setFontName("Arial");
        successCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        successCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        successFont.setBold(true);
        successCellStyle.setFont(successFont);
        return successCellStyle;
    }

    public CellStyle createDescriptionStyleFont(Workbook writeWorkbook) {
        org.apache.poi.ss.usermodel.Font headerFont = writeWorkbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        CellStyle headerCellStyle = writeWorkbook.createCellStyle();
        headerFont.setFontName("Arial");
        headerCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);
        return headerCellStyle;
    }


}
