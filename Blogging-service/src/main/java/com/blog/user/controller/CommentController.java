package com.blog.user.controller;

import com.blog.user.model.CommentsDto;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.responses.requests.CommentRequest;
import com.blog.user.service.CommentService;
import com.blog.user.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CommonUtils.API_URL.ACCESS_URL + "/comment")
public class CommentController {
    @Autowired
    private CommentService service;

    @PostMapping(CommonUtils.API_URL.INSERT)
    public CommonControllerResponse<CommentsDto>insertOrUpdate(
            @RequestParam("userId")Integer userId, @RequestParam("postId")Integer postId,
            @RequestBody CommentRequest commentRequest){
       return service.insertOrUpdate(userId,postId,commentRequest);
    }

    @DeleteMapping(CommonUtils.API_URL.DELETE)
    public CommonControllerResponse<String>insertOrUpdate(@RequestParam Integer commentId){
        return service.deleteComments(commentId);
    }


}
