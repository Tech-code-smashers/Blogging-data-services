package com.blog.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogPostDto {
    private Integer id;
    private String title;
    private String content;
    private UserDto user;
    private CategoryDto blogCategory;
    private Set<CommentsDto> comments = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public UserDto getUser() {
        return user;
    }
    public void setUser(UserDto user) {
        this.user = user;
    }
    public CategoryDto getBlogCategory() {
        return blogCategory;
    }
    public void setBlogCategory(CategoryDto blogCategory) {
        this.blogCategory = blogCategory;
    }
    public Set<CommentsDto> getComments() {
        return comments;
    }
    public void setComments(Set<CommentsDto> comments) {
        this.comments = comments;
    }
}
