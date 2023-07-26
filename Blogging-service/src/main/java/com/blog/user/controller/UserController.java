package com.blog.user.controller;

import com.blog.user.model.UserDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.UserService;
import com.blog.user.utils.CommonUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping(CommonUtils.API_URL.ACCESS_URL+"/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping(CommonUtils.API_URL.FIND_ALL_WITH_PAGINATION)
    public CommonControllerResponse<List<UserDto>> fetchAllPaginationAndSorting
            (@RequestParam (value="page",required = false) int page,
             @RequestParam (value="size",required = false)int size,
             @RequestParam (value="sortBy",defaultValue =CommonUtils.DEFAULT_CONSTANT.ID)String sortBy,
             @RequestParam (value="sortOrder",defaultValue = CommonUtils.DEFAULT_CONSTANT.ASC) String sortOrder){
        return  userService.findAllWithPaginationAndSorting(page,size,sortBy,sortOrder);
    }

    @GetMapping(CommonUtils.API_URL.FIND_BY_USERNAME)
    public CommonControllerResponse<UserDto> findByUserName(@RequestParam(value = "userName")String userName){
        return userService.findByUserName(userName);
    }

    @GetMapping(CommonUtils.API_URL.FIND_BY_ID)
    public CommonControllerResponse<UserDto> findById(@PathVariable Integer id){
        return userService.findById(id);
    }

    @PostMapping(CommonUtils.API_URL.INSERT)
    public CommonControllerResponse<UserDto> insert(@Valid @RequestBody UserDto userDto){
      return  userService.insertOrUpdate(userDto);
    }

    @DeleteMapping(CommonUtils.API_URL.DELETE)
    public CommonControllerResponse<String> deleteByIds(@RequestParam List<Integer> ids){
        return userService.Delete(ids);
    }

    @GetMapping(value = CommonUtils.API_URL.GET_EXCEL_TEMPLATE,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> getExcelTemplate(@RequestParam String excelType) throws IOException {
        InputStream in =userService.getUserExcelTemplate(excelType);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Users." + excelType);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @PostMapping(value =CommonUtils.API_URL.UPLOAD_EXCEL, produces =  MediaType.APPLICATION_OCTET_STREAM_VALUE,consumes = "multipart/form-data")
    public void uploadExcel(@RequestPart("excelFile") MultipartFile excelFile, HttpServletResponse response) throws Exception {
        String fileName;
        Workbook workbook = null;

        if(excelFile!=null && !excelFile.isEmpty()){
            InputStream fileData = excelFile.getInputStream();
            fileName= excelFile.getOriginalFilename();
            workbook =  userService.uploadUserExcel(fileData,fileName);
        }
        response.setContentType("application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition","attachment; filename=" + "user.xlsx");
        workbook.write(response.getOutputStream());
    }

    @GetMapping(value = CommonUtils.API_URL.DOWNLOAD_CSV)
    public ResponseEntity<byte[]> downloadGenericExcelCsv() {
        return userService.downloadCsvFile();
    }

    @GetMapping(value = CommonUtils.API_URL.SEARCH)
    public CommonControllerResponse<List<UserDto>> searchRecord(@RequestParam String keyword){
        return userService.searchItem(keyword);
    }

    @GetMapping(value = CommonUtils.API_URL.DROPDOWN)
    public CommonControllerResponse<List<UserDto>> dropDown(@RequestParam String keyword){
        return userService.searchItem(keyword);
    }


}
