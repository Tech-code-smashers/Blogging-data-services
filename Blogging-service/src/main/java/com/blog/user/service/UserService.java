package com.blog.user.service;

import com.blog.user.model.UserDto;
import com.blog.user.responses.CommonControllerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    public CommonControllerResponse<UserDto> insertOrUpdate(UserDto userDto);
    public CommonControllerResponse<UserDto> Delete(List<Integer> ids);
    public CommonControllerResponse<List<UserDto>> findAllWithPaginationAndSorting(int page, int size, String sortBy,
                                                                             String sortOrder);





}
