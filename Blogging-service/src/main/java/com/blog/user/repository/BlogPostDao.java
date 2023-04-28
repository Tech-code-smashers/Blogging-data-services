package com.blog.user.repository;

import com.blog.user.entity.BlogCategory;
import com.blog.user.entity.BlogPost;
import com.blog.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogPostDao extends JpaRepository<BlogPost,Integer> {
    List<BlogPost> findByUser(Users user);

    List<BlogPost> findByCategoryId(Integer categoryId);

}
