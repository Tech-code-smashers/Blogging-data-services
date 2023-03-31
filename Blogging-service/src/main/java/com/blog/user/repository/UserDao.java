package com.blog.user.repository;

import com.blog.user.entity.Users;
import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserDao extends JpaRepository<Users, Integer> {

    public Users findByUserName(String userName) throws ResourceNotFoundException;

}
