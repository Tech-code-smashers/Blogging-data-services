package com.blog.user.repository;

import com.blog.user.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostDao extends JpaRepository<BlogPost,Integer> {
    List<BlogPost> findByUserId(Integer userId);
    List<BlogPost> findByCategoryId(Integer categoryId);
}
