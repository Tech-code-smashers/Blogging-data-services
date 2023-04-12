package com.blog.user.globalExceptions.exception;

import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.ILLEGAL_ARGUMENT;

public class IllegalArgumentException extends  RuntimeException{

    public IllegalArgumentException() {
        super(ILLEGAL_ARGUMENT);
    }
    public IllegalArgumentException(String message) {
        super(message);
    }
}
