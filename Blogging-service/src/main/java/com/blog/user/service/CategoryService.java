package com.blog.user.service;

import com.blog.user.model.CategoryDto;
import com.blog.user.responses.CommonControllerResponse;
import java.util.List;
public interface CategoryService {
    public CommonControllerResponse<CategoryDto> insertOrUpdate(CategoryDto categoryDto);
    public CommonControllerResponse<String> delete(List<Integer> ids);
    public CommonControllerResponse<List<CategoryDto>> findAllWithPaginationAndSorting(int page, int size, String sortBy, String sortOrder);
    public CommonControllerResponse<CategoryDto> findByCategoryTitle(String categoryTitle);
    CommonControllerResponse<CategoryDto> findById(Integer id);
}
