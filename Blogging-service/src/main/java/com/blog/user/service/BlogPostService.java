package com.blog.user.service;

import com.blog.user.model.BlogPostDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.responses.responses.PostResponse;
import java.util.List;

public interface BlogPostService {
    public CommonControllerResponse<BlogPostDto> insertOrUpdate(BlogPostDto postDto,Integer categoryId, Integer userId);
    public CommonControllerResponse<String> delete(List<Integer> ids);
    public CommonControllerResponse<List<BlogPostDto>> findAllWithPaginationAndSorting(int page, int size, String sortBy, String sortOrder);
    public CommonControllerResponse<List<BlogPostDto>> findByCategoryId(Integer categoryId);
    public CommonControllerResponse<PostResponse> findBlogsByUserId(Integer userId);
    CommonControllerResponse<BlogPostDto> findById(Integer id);
}
