package com.blog.user.utils;

import org.apache.poi.ss.usermodel.CellStyle;

public class ColumnData {
    private Object cellValue;
    private CellStyle cellStyle;

    public ColumnData(Object cellValue, CellStyle cellStyle) {
        this.cellValue = cellValue;
        this.cellStyle = cellStyle;
    }

    public Object getCellValue() {
        return cellValue;
    }

    public void setCellValue(Object cellValue) {
        this.cellValue = cellValue;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }
}
