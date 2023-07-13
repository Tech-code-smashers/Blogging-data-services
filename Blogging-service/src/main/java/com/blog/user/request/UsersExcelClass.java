package com.blog.user.request;

import com.blog.user.entity.Users;
import com.blog.user.model.UserDto;
import com.blog.user.repository.UserDao;
import com.blog.user.responses.responses.CommonExcelResponse;
import com.blog.user.utils.BlogExcelUtils;
import com.blog.user.utils.CommonUtils;
import com.blog.user.utils.DataTransformation;
import com.blog.user.utils.ExcelRowProcessor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class UsersExcelClass implements Callable, ExcelRowProcessor {

    Row row;

    @Autowired
    ApplicationContext сontext;
    CountDownLatch latch;
    Workbook workbook;

    @Autowired
    @Qualifier("excelLib")
    private BlogExcelUtils blogExcelUtils;

    @Override
    public void initTask(Row row, CountDownLatch latch, Workbook workbook, ApplicationContext сontext) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.row = row;
        this.latch = latch;
        this.workbook = workbook;
        this.сontext = сontext;
    }

    @Override
    public Object call() throws Exception {
        try {
            processExcelRow(row, workbook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        latch.countDown();
        return null;
    }

    public void processExcelRow(Row row1, Workbook workbook)  {
        int[] row = {2};
        boolean error = false;
        BlogExcelUtils excelUtils =сontext.getBean(BlogExcelUtils.class);
        int index = row1.getRowNum() + 1;
        CommonExcelResponse uploadResponse = new CommonExcelResponse(
                CommonUtils.EXCEPTION_MESSAGE.UNPROCESSED_DATA, null,
                CommonUtils.DEFAULT_CONSTANT.UNPROCESSED_DATA, row[0]);
        UserDto dto = new UserDto();

        try {
            Integer id = row1.getCell(0) != null ? (int) row1.getCell(0).getNumericCellValue() : null;
            String name = row1.getCell(1) != null ? row1.getCell(1).getStringCellValue() : null;
            String lastName = row1.getCell(2) != null ?  row1.getCell(2).getStringCellValue() : null;
            String userName = row1.getCell(3) != null ?  row1.getCell(3).getStringCellValue() : null;
            String email = row1.getCell(4) != null ?  row1.getCell(4).getStringCellValue() : null;
            String password = row1.getCell(5) != null ?  row1.getCell(5).getStringCellValue() : null;
            String about = row1.getCell(6) != null ?  row1.getCell(6).getStringCellValue() : null;
            dto.setId(id);
            dto.setName(name);
            dto.setLastName(lastName);
            dto.setUserName(userName);
            dto.setEmail(email);
            dto.setPassword(password);
            dto.setAbout(about);
            bulkSaveOrUpdate(dto);
        } catch (DataIntegrityViolationException ex) {
            error=true;
            uploadResponse.setCode("H101");
            uploadResponse.setDescription(ex.getCause().getCause().getMessage());
        }catch (Exception ex){
            error=true;
            uploadResponse.setCode("H101");
            uploadResponse.setDescription(ex.getMessage());
        }
        if(!error){
            uploadResponse.setCode("M200");
            uploadResponse.setRowNumber(index);
            uploadResponse.setDescription("Success");
        }
        excelUtils.excelPrepareResponse(row1,uploadResponse);

    }


    @Transactional
    public void bulkSaveOrUpdate(UserDto userDto){
        Users user;
        UserDao repo =сontext.getBean(UserDao.class);
        DataTransformation dataTransformation =сontext.getBean(DataTransformation.class);
        user = dataTransformation.userDataTransform(userDto);
            if (userDto != null && userDto.getId() != null) {
                Users userUpdate = repo.findById(userDto.getId()).get();
                repo.save(dataTransformation.userDataUpdateTransform(userUpdate,userDto));
            }else{
                repo.save(user);
            }

    }

}
