package com.blog.user.entity;

import jakarta.persistence.*;

@Table
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String commentMsg;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private BlogPost post;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCommentMsg() {
        return commentMsg;
    }
    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }
    public BlogPost getPost() {
        return post;
    }
    public void setPost(BlogPost post) {
        this.post = post;
    }
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
