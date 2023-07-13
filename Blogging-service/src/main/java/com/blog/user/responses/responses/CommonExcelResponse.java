package com.blog.user.responses.responses;

public class CommonExcelResponse {
    private String code;
    private String tableName;
    private String description;
    private Integer rowNumber;

    public CommonExcelResponse(String code, String tableName, String description) {
        this.code = code;
        this.tableName = tableName;
        this.description = description;
    }

    public CommonExcelResponse(String code, String tableName, String description, Integer rowNumber) {
        this.code = code;
        this.tableName = tableName;
        this.description = description;
        this.rowNumber = rowNumber;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

