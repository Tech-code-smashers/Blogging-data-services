package com.blog.user.repository;

import com.blog.user.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CommentDao extends JpaRepository<Comments,Integer> {
}
