package com.blog.user.controller;


import com.blog.user.model.UserDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.UserService;
import com.blog.user.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonUtils.API_URL.ACCESS_URL)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(CommonUtils.API_URL.INSERT_USER)
    public CommonControllerResponse<UserDto> insert(@RequestBody UserDto userDto){
        CommonControllerResponse<UserDto> response = new CommonControllerResponse<>();
        return response;
    }

}
