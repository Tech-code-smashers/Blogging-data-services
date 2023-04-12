package com.blog.user.service;
import com.blog.user.model.UserDto;
import com.blog.user.responses.CommonControllerResponse;
import java.util.List;

public interface UserService {

    public CommonControllerResponse<UserDto> insertOrUpdate(UserDto userDto);
    public CommonControllerResponse<String> Delete(List<Integer> ids);
    public CommonControllerResponse<List<UserDto>> findAllWithPaginationAndSorting(int page, Integer size, String sortBy, String sortOrder);
    public CommonControllerResponse<UserDto> findByUserName(String userName);
    CommonControllerResponse<UserDto> findById(Integer id);
}
