package com.blog.user.globalExceptions.exception;


import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.RESOURCE_NOT_FOUND;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super(RESOURCE_NOT_FOUND);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
