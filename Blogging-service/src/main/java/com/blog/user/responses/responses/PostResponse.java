package com.blog.user.responses.responses;

import com.blog.user.model.BlogPostDto;
import java.util.List;

public class PostResponse {
    private List<BlogPostDto> blogPostList;
    public List<BlogPostDto> getBlogPostList() {
        return blogPostList;
    }
    public void setBlogPostList(List<BlogPostDto> blogPostList) {
        this.blogPostList = blogPostList;
    }
}
