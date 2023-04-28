package com.blog.user.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "BlogPost")
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "post_title",length = 100,nullable = false)
    private String title;
    @Column(length = 10000,nullable = false)
    private String content;
    private String imageName;
    private Date updatedOn = new Date();
    @ManyToOne
    @JoinColumn(name = "category_Id")
    private BlogCategory category;
    @ManyToOne
    @JoinColumn(name = "user_Id")
    private Users user;

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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public BlogCategory getCategory() {
        return category;
    }

    public void setCategory(BlogCategory category) {
        this.category = category;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
