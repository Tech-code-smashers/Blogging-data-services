package com.blog.user.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

public interface ExcelRowProcessor {
    void initTask(Row row, CountDownLatch latch, Workbook workbook , ApplicationContext сontext) throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException;

}
