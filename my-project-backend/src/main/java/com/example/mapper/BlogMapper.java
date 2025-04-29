package com.example.mapper;

import com.example.entity.dto.Blog;

import java.util.List;

public interface BlogMapper {
    // 取得所有blog
    List<Blog> findAll();

    // 取得所有AltBlog
    List<Blog> findAllAlt();

    List<Blog> findByCategory(String category);

    // 插入一个blog
    void insert(Blog blog);

    // 根据ID更新blog
    void update(Long id, Blog blog);

    // 根据ID查找Blog
    Blog getBlogById(Long id);
}
