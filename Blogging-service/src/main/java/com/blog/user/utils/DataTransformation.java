package com.blog.user.utils;


import com.blog.user.entity.BlogPost;
import com.blog.user.entity.Users;
import com.blog.user.model.BlogPostDto;
import com.blog.user.model.UserDto;
import org.springframework.stereotype.Component;

@Component
public class DataTransformation {

    public Users userDataTransform(UserDto userDto){
        Users user = new Users();
        user.setUserName(userDto.getUserName());
        user.setAbout(userDto.getAbout());
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        return user;
    }

    public BlogPost BlogDataTransform(BlogPostDto blogPostDto,BlogPost blogPost){
        blogPost.setId(blogPostDto.getId());
        blogPost.setContent(blogPostDto.getContent());
        blogPost.setTitle(blogPostDto.getTitle());
        blogPost.setImageName("Default.png");
        return blogPost;
    }
}
