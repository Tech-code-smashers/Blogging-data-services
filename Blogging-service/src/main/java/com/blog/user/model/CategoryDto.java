package com.blog.user.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JacksonXmlRootElement(localName = "BlogCategory")
public class CategoryDto {

    private Integer id;

    @NotEmpty(message = "Category title field cannot be null")
    @Size(min = 5, max = 20, message = "category title sized in b/w 5 to 20 char")
    private String categoryTitle;

    @NotEmpty(message = "Description cannot be null")
    @Size(min = 5, max = 100, message = "category description sized in b/w 5 to 30 char")
    private String description;

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
}
