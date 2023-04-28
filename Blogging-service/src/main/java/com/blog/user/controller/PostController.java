package com.blog.user.controller;

import com.blog.user.model.BlogPostDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.BlogPostService;
import com.blog.user.utils.CommonUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CommonUtils.API_URL.ACCESS_URL+"/post")
public class PostController {
    @Autowired
    private BlogPostService service;

    @PostMapping(CommonUtils.API_URL.INSERT)
    public CommonControllerResponse<BlogPostDto> insert(@Valid @RequestBody BlogPostDto postDto
                        ,@RequestParam("categoryId")Integer categoryId,@RequestParam("userId")Integer userId){
        return  service.insertOrUpdate(postDto,categoryId,userId);
    }

    @GetMapping(CommonUtils.API_URL.FIND_BY_ID)
    public CommonControllerResponse<BlogPostDto> findById(@RequestParam("postId")Integer postId){
        return  service.findById(postId);
    }

    @GetMapping(CommonUtils.API_URL.DELETE)
    public CommonControllerResponse<String> deleteById(@RequestParam("postId") List<Integer> postIds){
        return  service.delete(postIds);
    }

    @GetMapping(CommonUtils.API_URL.FIND_ALL_WITH_PAGINATION)
    public CommonControllerResponse<List<BlogPostDto>> findAllWithPaginationAndSorting(@RequestParam (value="page",required = false) int page, @RequestParam (value="size",required = false)int size,
                                                                            @RequestParam (value="sortBy",defaultValue =CommonUtils.DEFAULT_CONSTANT.ID)String sortBy,
                                                                            @RequestParam (value="sortOrder",defaultValue = CommonUtils.DEFAULT_CONSTANT.ASC) String sortOrder){
        return  service.findAllWithPaginationAndSorting(page,size,sortBy,sortOrder);
    }
    @GetMapping(CommonUtils.API_URL.FIND_BY_CATEGORY_Id)
    public CommonControllerResponse<List<BlogPostDto>> findByCategoryTitle(@RequestParam("categoryId") Integer categoryId){
        return  service.findByCategoryId(categoryId);
    }
}
