package com.blog.user.responses;

import com.blog.user.utils.CommonUtils;

public class CommonControllerResponse<T> {

    String status = CommonUtils.StatusCode.SUCCESS;
    String message;
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if (data != null) {
            this.message = getMessage();
            this.status = getStatus();
        }
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
