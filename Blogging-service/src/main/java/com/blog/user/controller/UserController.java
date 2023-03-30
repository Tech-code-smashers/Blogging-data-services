package com.blog.user.controller;


import com.blog.user.model.UserDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.UserService;
import com.blog.user.utils.CommonUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(CommonUtils.API_URL.ACCESS_URL)
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping(CommonUtils.API_URL.FIND_ALL_WITH_PAGINATION)
    public CommonControllerResponse<List<UserDto>> fetchAllPaginationAndSorting
            (@RequestParam Integer page, @RequestParam Integer  size, @RequestParam String sortBy, @RequestParam String sortOrder ){
        return  userService.findAllWithPaginationAndSorting(page,size,sortBy,sortOrder);
    }

    @GetMapping(CommonUtils.API_URL.FIND_BY_USERNAME)
    public CommonControllerResponse<UserDto> findByUserName(@RequestParam(value = "userName")String userName){
        return userService.findByUserName(userName);
    }

    @PostMapping(CommonUtils.API_URL.INSERT_USER)
    public CommonControllerResponse<UserDto> insert(@RequestBody UserDto userDto){
      return  userService.insertOrUpdate(userDto);
    }

    @DeleteMapping(CommonUtils.API_URL.DELETE_USER)
    public CommonControllerResponse<String> deleteByIds(@RequestParam List<Integer> ids){
        return userService.Delete(ids);
    }



}
