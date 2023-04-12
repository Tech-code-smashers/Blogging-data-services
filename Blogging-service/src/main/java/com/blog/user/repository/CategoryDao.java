package com.blog.user.repository;

import com.blog.user.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<BlogCategory,Integer> {
    public BlogCategory findByCategoryTitle(String categoryTitle);
}
