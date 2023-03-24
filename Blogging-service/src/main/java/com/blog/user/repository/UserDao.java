package com.blog.user.repository;

import com.blog.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserDao extends JpaRepository<Users, Integer> {

}
