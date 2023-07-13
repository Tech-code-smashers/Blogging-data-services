package com.blog.user.controller;

import com.blog.user.model.CategoryDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.service.CategoryService;
import com.blog.user.utils.CommonUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping(CommonUtils.API_URL.ACCESS_URL + "/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping(CommonUtils.API_URL.INSERT)
    public CommonControllerResponse<CategoryDto> insert(@Valid @RequestBody CategoryDto categoryDto) {
        return service.insertOrUpdate(categoryDto);
    }
    @GetMapping(CommonUtils.API_URL.FIND_ALL_WITH_PAGINATION)
    public CommonControllerResponse<List<CategoryDto>> fetchAllPaginationAndSorting
            (@RequestParam (value="page",required = false) int page,
             @RequestParam (value="size",required = false)int size,
             @RequestParam (value="sortBy",defaultValue =CommonUtils.DEFAULT_CONSTANT.ID)String sortBy,
             @RequestParam (value="sortOrder",defaultValue = CommonUtils.DEFAULT_CONSTANT.ASC) String sortOrder){
        return  service.findAllWithPaginationAndSorting(page,size,sortBy,sortOrder);
    }

    @GetMapping(CommonUtils.API_URL.FIND_BY_CATEGORY_Id)
    public CommonControllerResponse<CategoryDto> findCategoryByTitle(
            @RequestParam (value = "categoryTitle") String categoryTitle){
         return service.findByCategoryTitle(categoryTitle);
    }
    @GetMapping(CommonUtils.API_URL.FIND_BY_ID)
    public CommonControllerResponse<CategoryDto> findCategoryByTitle(
            @PathVariable(value = "id") Integer id){
        return service.findById(id);
    }

    @DeleteMapping(CommonUtils.API_URL.DELETE)
    public CommonControllerResponse<String> delete(@RequestParam List<Integer> ids) {
        return service.delete(ids);
    }




}


