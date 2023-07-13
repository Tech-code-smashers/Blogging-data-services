package com.blog.user.utils;

import com.blog.user.responses.responses.CommonExcelResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class BlogUtils {


    public static String createSafeSheetName(String sheetName) {
        if (sheetName != null)
            return WorkbookUtil.createSafeSheetName(sheetName);
        else
            return null;
    }

    public static int getHashCode(final String value) {
        if (value != null)
            return value.hashCode();
        else
            return -1;
    }

    public static List<String> getColumns(Class<?> dtoClass) {
        Field[] fields = dtoClass.getDeclaredFields();
        List<String> fieldList = new ArrayList<>();
        for (Field field : fields) {
            fieldList.add(field.getName());
        }
        return fieldList;
    }

    public static List<Map<String, Object>> excelDataMap(String SHEET_NAME, InputStream fileData, String fileName) throws IOException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Workbook workbook = null;
        Sheet sheet = null;
        if (fileData != null) {
            if (fileName.toUpperCase().endsWith(CommonUtils.WB_TYPE_XSSF))
                workbook = new XSSFWorkbook(fileData);
            else
                workbook = new HSSFWorkbook(fileData);
            if (workbook != null) {
                sheet = workbook
                        .getSheet(String.valueOf(BlogUtils.getHashCode(BlogUtils.createSafeSheetName(SHEET_NAME))));
                if (sheet != null) {
                    int noOfRows = sheet.getLastRowNum();
                    Row headerRow = sheet.getRow(0);
                    List<String> columns = new ArrayList<>();
                    if (headerRow != null && headerRow.getLastCellNum() > 0) {
                        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                            columns.add(headerRow.getCell(i).getStringCellValue());
                        }
                        for (int i = 1; i <= noOfRows; i++) {
                            Row row = sheet.getRow(i);
                            if (row != null) {
                                int lastColumn = row.getLastCellNum();
                                if (lastColumn > 0) {
                                    Map<String, Object> excelDataMap = new LinkedHashMap<>();
                                    for (int j = 0; j < lastColumn; j++) {
                                        String columnName = columns.get(j);
                                        Cell cell = row.getCell(j);
                                        Object cellValue = null;

                                        if (cell != null)
                                            cellValue = BlogUtils.getCellValue(cell, columnName);

                                        if (cellValue instanceof String) {
                                            String strCellVal = cellValue.toString();
                                            if (BlogUtils.maskStringToNull(strCellVal) == null)
                                                cellValue = null;
                                        }

//                                       ColumnMetaDataAPIResponse apiResponse = (ColumnMetaDataAPIResponse) HEADERS
//                                               .get(columnName);
//                                       excelDataMap.put(columnName, getData.apply(cellValue, apiResponse.getType()));
                                    }
                                    int index = i + 1;
                                    excelDataMap.put("columnexcel", index);
                                    dataList.add(excelDataMap);

                                }
                            }
                        }
                    }
                }
            }
        }
        return dataList;
    }


    public static Object getCellValue(Cell cell, final String COLUMN_NAME) {
        Object value = null;
        CellType cellType = cell.getCellTypeEnum();
        if (maskStringToNull(COLUMN_NAME) != null
                && COLUMN_NAME.toLowerCase().contains(CommonUtils.DEFAULT_CONSTANT.DATE_TIME)) {
            String stringDate = null;
            Date d1 = null;
            String s = null;
            try {
                try {
                    d1 = cell.getDateCellValue();
                    s = d1 + "";
                } catch (Exception e) {
                    try {
                        s = cell.getStringCellValue();
                    } catch (Exception er) {
                        er.printStackTrace();
                        return s;
                    }
                }

            } finally {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                Date d = null;
                try {
                    d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(s);

                } catch (ParseException e) {
                    e.printStackTrace();
                    return s;
                }
                stringDate = format.format(d);
            }
            return stringDate;
        }
        if (maskStringToNull(COLUMN_NAME) != null
                && COLUMN_NAME.toLowerCase().contains(CommonUtils.DEFAULT_CONSTANT.DATE)) {
            String stringDate = null;
            Date d1 = null;
            String s = null;
            try {

                try {
                    d1 = cell.getDateCellValue();
                    s = d1 + "";
                } catch (Exception e) {
                    try {
                        s = cell.getStringCellValue();
                    } catch (Exception er) {
                        er.printStackTrace();
                        return s;

                    }
                }

            } finally {

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                Date d = null;
                try {
                    d = new SimpleDateFormat("dd-MM-yyyy").parse(s);

                } catch (ParseException e) {
                    e.printStackTrace();
                    return s;
                }
                stringDate = format.format(d);
                return stringDate;
            }

        }

        if (maskStringToNull(COLUMN_NAME) != null
                && COLUMN_NAME.toLowerCase().contains(CommonUtils.DEFAULT_CONSTANT.TIME)) {

            String stringTime = null;
            Time d1 = null;
            String s = "";
            try {

                try {
                    d1 = (Time) cell.getDateCellValue();
                    s = d1 + "";
                } catch (Exception e) {
                    try {
                        s = cell.getStringCellValue();
                    } catch (Exception er) {
                        return s;
                    }
                }
            } finally {

                LocalTime lt = null;
                try {
                    lt = LocalTime.parse(s);

                } catch (Exception e) {
                    e.printStackTrace();
                    return s;
                }
                stringTime = lt.toString();

            }
            if (stringTime.split(":").length == 3)
                return stringTime;
            else
                return stringTime + ":00";
        }


        switch (cellType) {
            case NUMERIC:
                value = cell.getNumericCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case BLANK:
                value = cell.getStringCellValue();
                break;
            case _NONE:
                value = cell.getStringCellValue();
                break;
            default:
                value = cell.getStringCellValue();
                break;

        }
        return value;
    }

    public static String maskStringToNull(String value) {
        if (value == null || value.trim().length() == 0)
            return null;
        else
            return value.trim();
    }

    public static ByteArrayInputStream prepareResponse(List<CommonExcelResponse> commonExcelUploadResponses,
                                                       InputStream fileData, final String SHEET_NAME, final String FILE_NAME) throws IOException {
        ByteArrayInputStream result = null;
        Workbook readWorkbook = null;
        Workbook writeWorkbook = null;
        ByteArrayOutputStream out = null;
        Sheet readSheet = null;
        Sheet writeSheet = null;
        try {
            if (fileData != null) {
                if (FILE_NAME.toUpperCase().endsWith(CommonUtils.WB_TYPE_XSSF)) {
                    readWorkbook = new XSSFWorkbook(fileData);
                    writeWorkbook = new XSSFWorkbook();
                } else {
                    readWorkbook = new HSSFWorkbook(fileData);
                    writeWorkbook = new HSSFWorkbook();
                }
                if (readWorkbook != null) {
                    readSheet = readWorkbook
                            .getSheet(String.valueOf(BlogUtils.getHashCode(BlogUtils.createSafeSheetName(SHEET_NAME))));

                    Map<String, CellStyle> columns = new LinkedHashMap<>();

                    Map<Integer, List<ColumnData>> excelData = new LinkedHashMap<>();

                    int lastColumnIndex = 0;

                    if (readSheet != null) {
                        int noOfRows = readSheet.getLastRowNum();
                        Row headerRow = readSheet.getRow(0);

                        if (headerRow != null && headerRow.getLastCellNum() > 0) {
                            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                                Cell headerCell = headerRow.getCell(i);
                                columns.put(headerCell.getStringCellValue(), headerCell.getCellStyle());
                            }

                            int dataRow = 1;
                            List<String> COLUMNS = new ArrayList<>(columns.keySet());
                            for (int i = 1; i <= noOfRows; i++) {
                                Row row = readSheet.getRow(i);
                                if (row != null) {
                                    int lastColumn = Math.max(row.getLastCellNum(), COLUMNS.size());
                                    if (lastColumn > 0) {
                                        dataRow = dataRow + 1;
                                        lastColumnIndex = lastColumn;
                                        List<ColumnData> columnData = new ArrayList<>();
                                        for (int j = 0; j < lastColumn; j++) {
                                            String columnName = COLUMNS.get(j);
                                            Cell cell = row.getCell(j);
                                            Object cellValue = null;

                                            if (cell != null)
                                                cellValue = BlogUtils.getCellValue(cell, columnName);

                                            ColumnData data = new ColumnData(cellValue, null);
                                            columnData.add(data);

                                        }
                                        excelData.put(dataRow, columnData);
                                    }
                                }
                            }
                        }
                    }
                    // Font and Header Style
                    Font headerFont = writeWorkbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setColor(IndexedColors.WHITE.getIndex());
                    CellStyle headerCellStyle = writeWorkbook.createCellStyle();
                    headerFont.setFontName("Arial");
                    headerCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
                    headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    headerFont.setBold(true);
                    headerCellStyle.setFont(headerFont);
                    // Failure cell Style
                    Font failureFont = writeWorkbook.createFont();
                    failureFont.setBold(true);
                    failureFont.setColor(IndexedColors.RED.getIndex());
                    CellStyle failureHeaderStyle = writeWorkbook.createCellStyle();
                    failureFont.setFontName("Arial");
                    failureHeaderStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
                    failureHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    failureFont.setBold(true);
                    failureHeaderStyle.setFont(failureFont);
                    // Success cell style
                    Font successFont = writeWorkbook.createFont();
                    successFont.setBold(true);
                    successFont.setColor(IndexedColors.GREEN.getIndex());
                    CellStyle successCellStyle = writeWorkbook.createCellStyle();
                    successFont.setFontName("Arial");
                    successCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
                    successCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    successFont.setBold(true);
                    successCellStyle.setFont(successFont);

                    if (commonExcelUploadResponses != null && columns.size() > 0) {
                        columns.put("Status", headerCellStyle);
                        columns.put("Code", headerCellStyle);
                        columns.put("Description", headerCellStyle);

                        commonExcelUploadResponses.forEach(commonExcelUploadResponse -> {
                            Integer rowNum = commonExcelUploadResponse.getRowNumber();

                            if (excelData != null && excelData.size() > 0) {
                                if (excelData.containsKey(rowNum)) {
                                    List<ColumnData> columnData = excelData.get(rowNum);

                                    String code = commonExcelUploadResponse.getCode();
                                    if (!code.equals(CommonUtils.RESPONSE_MESSAGE.SUCCESS)) {
                                        ColumnData data = new ColumnData("Fail", failureHeaderStyle);
                                        ColumnData data1 = new ColumnData(commonExcelUploadResponse.getCode(),
                                                failureHeaderStyle);
                                        ColumnData data2 = new ColumnData(commonExcelUploadResponse.getDescription(),
                                                failureHeaderStyle);
                                        columnData.add(data);
                                        columnData.add(data1);
                                        columnData.add(data2);
                                    } else {
                                        ColumnData data = new ColumnData("Success", successCellStyle);
                                        ColumnData data1 = new ColumnData(commonExcelUploadResponse.getCode(),
                                                successCellStyle);
                                        ColumnData data2 = new ColumnData(commonExcelUploadResponse.getDescription(),
                                                successCellStyle);
                                        columnData.add(data);
                                        columnData.add(data1);
                                        columnData.add(data2);
                                    }

                                    excelData.put(rowNum, columnData);
                                }
                            } else {
                                List<ColumnData> columnData = new ArrayList<>(columns.size());
                                columns.forEach((K, V) -> {
                                    if (BlogUtils.trimString(K).length() > 0) {
                                        if (!K.equals("Status") && !K.equals("Code") && !K.equals("Description")) {
                                            ColumnData data = new ColumnData("", null);
                                            columnData.add(data);
                                        }
                                    }
                                });

                                ColumnData data = new ColumnData("Fail", failureHeaderStyle);
                                ColumnData data1 = new ColumnData(commonExcelUploadResponse.getCode(),
                                        failureHeaderStyle);
                                ColumnData data2 = new ColumnData(commonExcelUploadResponse.getDescription(),
                                        failureHeaderStyle);
                                columnData.add(data);
                                columnData.add(data1);
                                columnData.add(data2);
                                excelData.put(rowNum, columnData);
                            }
                        });

                    } else {
                        columns.put("Status", headerCellStyle);
                        columns.put("Code", headerCellStyle);
                        columns.put("Description", headerCellStyle);
                        List<ColumnData> columnData = new ArrayList<>(columns.size());
                        columns.forEach((K, V) -> {
                            if (BlogUtils.trimString(K).length() > 0) {
                                if (!K.equals("Status") && !K.equals("Code") && !K.equals("Description")) {
                                    ColumnData data = new ColumnData("", null);
                                    columnData.add(data);
                                }
                            }
                        });

                        ColumnData data = new ColumnData("Fail", failureHeaderStyle);
                        ColumnData data1 = new ColumnData(CommonUtils.EXCEPTION_MESSAGE.EMPTY_FILE, failureHeaderStyle);
                        ColumnData data2 = new ColumnData(CommonUtils.EXCEPTION_MESSAGE.EMPTY_FILE, failureHeaderStyle);
                        columnData.add(data);
                        columnData.add(data1);
                        columnData.add(data2);
                        excelData.put(2, columnData);
                    }

                    out = new ByteArrayOutputStream();
                    writeSheet = writeWorkbook.createSheet(
                            String.valueOf(BlogUtils.getHashCode(BlogUtils.createSafeSheetName(SHEET_NAME))));

                    int totalRow = 0;
                    // Row for Header
                    Row writeHeaderRow = writeSheet.createRow(totalRow);

                    int count = 0;

                    if (columns != null && excelData != null) {

                        Set<Map.Entry<String, CellStyle>> set = columns.entrySet();
                        Iterator<Map.Entry<String, CellStyle>> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, CellStyle> entry = iterator.next();
                            String KEY = entry.getKey();
                            CellStyle cellStyle = entry.getValue();
                            Cell cell = writeHeaderRow.createCell(count);
                            cell.setCellValue(KEY);
                            if (cellStyle != null) {
                                CellStyle newStyle = writeWorkbook.createCellStyle();
                                newStyle.cloneStyleFrom(cellStyle);
                                cell.setCellStyle(newStyle);
                            }

                            count++;
                        }

                        totalRow = totalRow + 1;

                        Set<Map.Entry<Integer, List<ColumnData>>> dataSet = excelData.entrySet();
                        Iterator<Map.Entry<Integer, List<ColumnData>>> dataIterator = dataSet.iterator();
                        while (dataIterator.hasNext()) {
                            Map.Entry<Integer, List<ColumnData>> entry = dataIterator.next();
                            List<ColumnData> datas = entry.getValue();
                            Row row = writeSheet.createRow(totalRow++);
                            if (datas != null) {
                                CellStyle newStyle = writeWorkbook.createCellStyle();

                                for (int i = 0; i < datas.size(); i++) {
                                    ColumnData columnData = datas.get(i);
                                    Cell ageCell = row.createCell(i);
                                    ageCell.setCellValue(BlogUtils.num(columnData.getCellValue()));
                                    if (i >= lastColumnIndex) {
                                        CellStyle cellStyle = columnData.getCellStyle();
                                        if (cellStyle != null) {
                                            //	CellStyle newStyle = writeWorkbook.createCellStyle();
                                            newStyle.cloneStyleFrom(cellStyle);
                                            ageCell.setCellStyle(newStyle);
                                        }
                                    }
                                }
                            }

                        }

                        writeWorkbook.write(out);
                        result = new ByteArrayInputStream(out.toByteArray());
                    }
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        return result;
    }

    public static String trimString(String arg) {
        if (arg != null)
            return arg.trim();
        else
            return "";
    }

    public static String num(Object value) throws Exception {
        String result = null;
        int sign = 0;// flag to find if expo is positive or negative
        int flag = 0;// flag to identify negative number
        if (value instanceof String)
            result = value.toString();
        else if (value instanceof StringBuilder)
            result = (String) value.toString();
        else if (value instanceof StringBuffer)
            result = (String) value.toString();
        if (isOfNumber(result)) {
            if (result != null) {
                result = result.toUpperCase();
                try {
                    char s = result.charAt(0);
                    if (s == '-') {// if contains negative number flag will be one
                        flag = 1;
                        result = result.substring(1);
                    }
                    if (result.contains("E")) {// if it contains e
                        String array[] = result.split("E");

                        if (array[1].contains("-")) {// if has negative exponent
                            int length = array[0].length();
                            if (array[0].contains(".")) {
                                length = length - 1;// to round off
                            }
                            sign = 1;// since negative so sign is 1
                            int expon = Integer.parseInt(array[1].substring(1));
                            double base = Double.parseDouble(array[0]);
                            result = big(expon, base, length, result, sign);// calling the function to find the
                            // exponent
                            result = dot(result); // rounding the decimal

                        } else {
                            int expon = Integer.parseInt(array[1]);
                            double base = Double.parseDouble(array[0]);
                            int length = array[0].length();
                            if (array[0].contains(".")) {
                                length = length - 1;
                            }
                            result = big(expon, base, length, result, sign);
                            result = dot(result);
                        }
                    } else {
                        result = dot(result);
                    }
                } catch (Exception e) {
                    if (value instanceof String)
                        result = value.toString();
                    else if (value instanceof StringBuilder)
                        result = (String) value.toString();
                    else if (value instanceof StringBuffer)
                        result = (String) value.toString();
                }

            } else {
                result = String.valueOf(value);
                result = dot(result);
            }
            if (flag == 1) {
                result = "-" + result;
            }
        } else {
            result = String.valueOf(value);
            result = dot(result);
        }
        return result;

    }
    public static boolean isOfNumber(final String VALUE) {
        boolean flag = false;
        if (trimString(VALUE).length() > 0) {
            try {
                Double.parseDouble(VALUE);
                flag = true;
            } catch (Exception e) {
                try {
                    Float.parseFloat(VALUE);
                    flag = true;
                } catch (Exception ee) {
                    try {
                        Long.parseLong(VALUE);
                        flag = true;
                    } catch (Exception eee) {
                        try {
                            Integer.parseInt(VALUE);
                            flag = true;
                        } catch (Exception eeee) {
                            try {
                                Short.parseShort(VALUE);
                                flag = true;
                            } catch (Exception eeeee) {
                                try {
                                    Byte.parseByte(VALUE);
                                    flag = true;
                                } catch (Exception eeeeee) {
                                    try {
                                        new BigDecimal(VALUE);
                                        flag = true;
                                    } catch (Exception eeeeeee) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    private static String dot(String result) throws Exception {// function to round off
        if (result.contains(".")) {
            int pos = result.indexOf(".");
            int size = result.length();
            int len = size - pos - 1;
            int count = 0;
            if (len < 3) {
                for (int i = 1; i < len + 1; i++) {
                    char c = result.charAt(pos + i);
                    if (c == '0') {
                        count++;
                    }
                }
            }
            if (count == len) {
                result = result.substring(0, pos);
            }
        }
        return result;
    }
    private static String big(int expon, double base, int length, String result, int sign) throws Exception {
        double power = 0.0;
        if (sign == 0) {
            power = Math.pow(10, expon);
        } else
            power = Math.pow(10, -expon);
        double l = base * power;
        BigDecimal bd = new BigDecimal(l);

        if (expon > length) {
            MathContext mc = new MathContext(expon);
            bd = bd.round(mc);
        } else {
            MathContext mc = new MathContext(length);
            bd = bd.round(mc);
        }
        result = bd.toPlainString();

        return result;
    }


}
