package com.blog.user.serviceImpl;

import com.blog.user.model.UserDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl  implements UserService {
    @Override
    public CommonControllerResponse<UserDto> insertOrUpdate(UserDto userDto) {
        return null;
    }

    @Override
    public CommonControllerResponse<UserDto> Delete(List<Integer> ids) {
        return null;
    }

    @Override
    public CommonControllerResponse<List<UserDto>> findAllWithPaginationAndSorting(int page, int size, String sortBy, String sortOrder) {
        return null;
    }
}
