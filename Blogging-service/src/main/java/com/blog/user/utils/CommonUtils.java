package com.blog.user.utils;

public interface CommonUtils {

    interface StatusCode{
        String SUCCESS = "Success";
    }
    interface API_URL{
        String ACCESS_URL = "blog/user";
        String INSERT_USER = ACCESS_URL+"/add";
    }
}
