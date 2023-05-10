package com.blog.user.serviceImpl;

import com.blog.user.entity.BlogPost;
import com.blog.user.entity.Comments;
import com.blog.user.entity.Users;
import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import com.blog.user.model.CommentsDto;
import com.blog.user.repository.BlogPostDao;
import com.blog.user.repository.CommentDao;
import com.blog.user.repository.UserDao;
import com.blog.user.responses.CommonControllerResponse;
import com.blog.user.responses.requests.CommentRequest;
import com.blog.user.service.BlogPostService;
import com.blog.user.service.CommentService;
import com.blog.user.service.UserService;
import com.blog.user.utils.DataTransformation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.blog.user.utils.CommonUtils.EXCEPTION_MESSAGE.ID_NOT_FOUND;
import static com.blog.user.utils.CommonUtils.RESPONSE_MESSAGE.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private BlogPostDao postRepo;
    @Autowired
    private CommentDao commentRepo;
    @Autowired
    private UserDao userRepo;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private BlogPostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private DataTransformation dataTransformation;
    public Comments mte(CommentsDto commentsDto) { return mapper.map(commentsDto, Comments.class);}
    public CommentsDto etm(Comments comments) {return mapper.map(comments, CommentsDto.class);}

    @Override
    public CommonControllerResponse<CommentsDto> insertOrUpdate(Integer userId,Integer postId, @Valid CommentRequest commentRequest) {
         CommonControllerResponse<CommentsDto> response = new CommonControllerResponse<>();
         BlogPost blogPost =  postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("postId"+" "+ID_NOT_FOUND));
         Users users = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("UserId"+" "+ID_NOT_FOUND));
         CommentsDto commentsDto = dataTransformation.commentsDtoDataTransformation(commentRequest);
         Comments commentObj = mte(commentsDto);
         commentObj.setPost(blogPost);
         commentObj.setUser(users);
         if(commentRequest.getId()>0 && commentRequest.getId()!=0){
             Comments commentsEntity = commentRepo.findById(commentRequest.getId()).orElseThrow(()->new ResourceNotFoundException("comment-id"+" "+ID_NOT_FOUND));
             commentObj = dataTransformation.commentsDtoDataTransformationWhileUpdating(commentsEntity,commentRequest);
             response.setMessage(UPDATE_SUCCESS);
            response.setData(etm(commentRepo.save(commentObj)));
         }else{
             response.setMessage(SAVE_SUCCESS);
             response.setData(etm(commentRepo.save(commentObj)));
         }
         return response;
    }

    @Override
    public CommonControllerResponse<String> deleteComments(Integer commentId) {
        CommonControllerResponse<String> response = new CommonControllerResponse<>();
        commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment-id" + " " + ID_NOT_FOUND));
        commentRepo.deleteById(commentId);
         response.setMessage(DELETE_SUCCESS);
         return response;
    }
}
