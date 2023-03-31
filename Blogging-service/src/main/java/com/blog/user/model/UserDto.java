package com.blog.user.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JacksonXmlRootElement(localName = "Users")
public class UserDto {
    private Integer id;

    @NotEmpty(message = "Name cannot be null")
    private String name;
    @NotEmpty(message = "Last name cannot be null")
    private String lastName;

    @NotNull(message = "User name cannot be null")
    @Size(min = 5,max = 10,message = "Length should be in b/w 5 to 10")
    private String userName;

    @NotEmpty(message = "Email cannot be null")
    @Email(message = "Email is not valid",regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotEmpty(message = "password cannot be null")
    @Size(min = 5,max = 15,message = "Length should be in b/w 5 to 15 letter")
    private String password;
    private String about;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
