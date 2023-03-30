package com.blog.user.utils;

public interface CommonUtils {

    interface StatusCode{
        String SUCCESS = "Success";
    }
    interface API_URL{
        String ACCESS_URL = "blog/user";
        String INSERT_USER = ACCESS_URL+"/add";
        String DELETE_USER = ACCESS_URL+"/delete";
        String FIND_ALL_WITH_PAGINATION= ACCESS_URL+"/findAll";
        String FIND_BY_USERNAME = ACCESS_URL+"/findByUserName";
    }

    interface RESPONSE_MESSAGE{
        String UPDATE_SUCCESS = "Updated successfully";
        String SAVE_SUCCESS = "Saved successfully";
        String DELETE_SUCCESS = "Deleted successfully";
        String FETCH_ALL_DETAILS = "All user data details";
        String SUCCESS = "Success";
    }

    interface  EXCEPTION_MESSAGE{
        String ID_NULL = "Id value must be greater that 0";
        String LIST_NULL = "List data either empty or null";
        String RESOURCE_NOT_FOUND = "Resource not found on the server";
        String NOT_FOUND = "Data not found";
    }

}
