package com.blog.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name ="Blog_Category")
public class BlogCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String categoryTitle;
    private String description;


    public BlogCategory() {
    }

    public BlogCategory(Integer id, String categoryTitle, String description) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BlogCategory{" +
                "id=" + id +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
