package com.blog.user.globalExceptions.exceptionAdvice;


import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import com.blog.user.responses.CommonControllerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.NOT_FOUND;

@RestControllerAdvice
public class BlogException {


    @ExceptionHandler(ResourceNotFoundException.class)
    public CommonControllerResponse<ResourceNotFoundException> resourceNotFoundException(ResourceNotFoundException  ex){
        CommonControllerResponse<ResourceNotFoundException>  res =  new CommonControllerResponse<>();
        String message =ex.getLocalizedMessage();
        res.setMessage(message);
        res.setStatus(NOT_FOUND);
        return res;

    }



}
