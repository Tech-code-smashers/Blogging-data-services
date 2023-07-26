package com.blog.user.repository;

import com.blog.user.entity.Users;
import com.blog.user.globalExceptions.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<Users, Integer> {
     Users findByUserName(String userName) throws ResourceNotFoundException;

    @Query(value = "SELECT * FROM users WHERE name LIKE %:keyword% or id LIKE %:keyword%", nativeQuery = true)
    public List<Users> searchUsers(String keyword);

}
