package com.blog.user.globalExceptions.exceptionAdvice;


import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import com.blog.user.responses.CommonControllerResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonControllerResponse<Map<String,String>> resourceNotFoundException(MethodArgumentNotValidException  ex){
        CommonControllerResponse<Map<String,String>>  res =  new CommonControllerResponse<>();
        Map<String, String> map = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
           String fieldName = ((FieldError)error).getField();
           String msg = error.getDefaultMessage();
           map.put(fieldName,msg);
        });
        res.setStatus(HttpStatus.BAD_REQUEST.name());
        res.setData(map);
        return res;

    }
    @ExceptionHandler(DataIntegrityViolationException.class )
    public CommonControllerResponse<DataIntegrityViolationException> constraintViolationException(DataIntegrityViolationException  exMsg){
        CommonControllerResponse<DataIntegrityViolationException>  res =  new CommonControllerResponse<>();
        res.setMessage(exMsg.getMessage());
        res.setStatus(HttpStatus.CONFLICT.name());
        return res;
    }

    @ExceptionHandler(IllegalArgumentException.class )
    public CommonControllerResponse<IllegalArgumentException> constraintViolationException(IllegalArgumentException  exMsg){
        CommonControllerResponse<IllegalArgumentException>  res =  new CommonControllerResponse<>();
        res.setMessage(exMsg.getMessage());
        res.setStatus(HttpStatus.BAD_REQUEST.name());
        return res;
    }


}
