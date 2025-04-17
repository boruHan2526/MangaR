package com.example.mapper;

import com.example.entity.dto.Blog;

import java.util.List;

public interface BlogMapper {
    List<Blog> findAll();
    List<Blog> findByCategory(String category);

}
