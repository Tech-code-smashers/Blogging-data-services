package com.blog.user.utils;

public interface CommonUtils {

    interface StatusCode{
        String SUCCESS = "B200";
        String FAILURE = "B201";
    }
    interface API_URL{
        String ACCESS_URL = "blog";
        String INSERT = ACCESS_URL+"/add";
        String DELETE = ACCESS_URL+"/delete";
        String FIND_ALL_WITH_PAGINATION= ACCESS_URL+"/findAll";
        String FIND_BY_USERNAME = ACCESS_URL+"/findByUserName";
        String FIND_BY_ID = ACCESS_URL+"/findById/{id}";
        String FIND_BY_CATEGORY_TITLE = ACCESS_URL+"/findByCategoryTitle";
    }

    interface RESPONSE_MESSAGE{
        String UPDATE_SUCCESS = "Updated successfully";
        String SAVE_SUCCESS = "Saved successfully";
        String DELETE_SUCCESS = "Deleted successfully";
        String FETCH_ALL_DETAILS = "All user data details";
        String SUCCESS = "Success";
        String FETCH_BY_CATEGORY_TITLE= "Details of BlogCategory";
    }

    interface  EXCEPTION_MESSAGE{
        String ID_NULL = "Id value must be greater that 0";
        String LIST_NULL = "List data either empty or null";
        String RESOURCE_NOT_FOUND = "Resource not found on the server";
        String NOT_FOUND = "Data not found";
        String ILLEGAL_ARGUMENT= "Illegal argument passed";
    }

    interface DEFAULT_CONSTANT{
        String ASC = "Asc";
        String DESC = "Desc";
        String ID = "Id";
    }

}
