package com.blog.user.service;

import com.blog.user.model.CommentsDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.responses.requests.CommentRequest;

public interface CommentService {

    public CommonControllerResponse<CommentsDto> insertOrUpdate(Integer userId,Integer postId, CommentRequest commentRequest);
    public CommonControllerResponse<String> deleteComments(Integer postId);
}
