package com.blog.user.service;

import com.blog.user.model.UserDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.responses.responses.CommonExcelResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface UserService {

    String SHEET_NAME = "Users";

    public CommonControllerResponse<UserDto> insertOrUpdate(UserDto userDto);
    public CommonControllerResponse<String> Delete(List<Integer> ids);
    public CommonControllerResponse<List<UserDto>> findAllWithPaginationAndSorting(int page, Integer size, String sortBy, String sortOrder);
    public CommonControllerResponse<UserDto> findByUserName(String userName);
    CommonControllerResponse<UserDto> findById(Integer id);
    ByteArrayInputStream getUserExcelTemplate(String excelType)throws IOException;
    List<CommonExcelResponse> uploadExcelData(InputStream fileData, String fileName) throws IOException;
    HttpServletResponse downloadCsvFile(HttpServletResponse response) throws IOException;
    Workbook uploadUserExcel(InputStream fileData, String fileName) throws Exception;
    CommonControllerResponse<List<UserDto>> searchItem(String keyword);
}
